package com.taskmanagement.business.concretes;

import com.taskmanagement.business.abstracts.AdminService;
import com.taskmanagement.business.core.utils.JWTUtil;
import com.taskmanagement.dtos.CommentDto;
import com.taskmanagement.dtos.TaskDto;
import com.taskmanagement.dtos.UserDto;
import com.taskmanagement.entities.Comment;
import com.taskmanagement.entities.Task;
import com.taskmanagement.entities.User;
import com.taskmanagement.enums.TaskStatus;
import com.taskmanagement.enums.UserRole;
import com.taskmanagement.repository.CommentRepository;
import com.taskmanagement.repository.TaskRepository;
import com.taskmanagement.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService  {

    private final UserRepository userRepository;

    private final TaskRepository taskRepository;

    private final JWTUtil jwtUtil;

    private final CommentRepository commentRepository;

    @Override
    public List<UserDto> getUsers() {
        return userRepository.findAll()
        .stream()
        .filter(user -> user.getUserRole() == UserRole.EMPLOYEE)
                .map(User::getUserDto)
        .collect(Collectors.toList());
    }

    @Override
    public TaskDto createTask(TaskDto taskDto) {
        Optional<User> optionalUser = userRepository.findById(taskDto.getEmployeeId());
        if(optionalUser.isPresent()){
            LocalDate today = LocalDate.now();

            // Assuming taskDto.getDueDate() returns a java.util.Date
            Date dueDateUtil = taskDto.getDueDate(); // This should return a Date object
            LocalDate dueDate = dueDateUtil.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(); // Convert to LocalDate

            // Check if the due date is before today
            if (dueDate.isBefore(today)) {
                throw new IllegalArgumentException("Görev bitiş tarihi bugünden önce olamaz.");
            }

            Task task = new Task();
            task.setTitle(taskDto.getTitle());
            task.setTaskStatus(TaskStatus.BEKLEMEDE);
            task.setDescription(taskDto.getDescription());
            task.setPriority(taskDto.getPriority());
            task.setDueDate(taskDto.getDueDate());
            task.setUser(optionalUser.get());
            return taskRepository.save(task).getTaskDto();
        }
        return null;
    }

    @Override
    public List<TaskDto> getAllTasks() {

        return taskRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(Task::getDueDate).reversed())
                .map(Task::getTaskDto).collect(Collectors.toList());
    }

    @Override
    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    @Override
    public TaskDto getTaskById(Long id) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        return optionalTask.map(Task::getTaskDto).orElse(null);
    }

    @Override
    public TaskDto updateTask(Long id, TaskDto taskDto) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        Optional<User> optionalUser = userRepository.findById(taskDto.getEmployeeId());
        if(optionalTask.isPresent() && optionalUser.isPresent()){
            Task existingTask = optionalTask.get();
            existingTask.setTitle(taskDto.getTitle());
            existingTask.setDescription(taskDto.getDescription());
            existingTask.setDueDate(taskDto.getDueDate());
            existingTask.setPriority(taskDto.getPriority());
            existingTask.setTaskStatus(mapStringToTaskStatus(String.valueOf(taskDto.getTaskStatus())));
            existingTask.setUser(optionalUser.get());
            return taskRepository.save(existingTask).getTaskDto();

        }
        return null;
    }

    @Override
    public List<TaskDto> searchTaskByTitle(String title) {
        return taskRepository.findAllByTitleContaining(title)
                .stream()
                .sorted(Comparator.comparing(Task:: getDueDate).reversed())
                .map(Task::getTaskDto)
                .collect(Collectors.toList());
    }

    @Override
    public CommentDto createComment(Long taskId, String content) {
        Optional<Task> optionalTask = taskRepository.findById(taskId);
        User user = jwtUtil.getLoggedInUser();
        if(optionalTask.isPresent() && user != null){
            Comment comment = new Comment();
            comment.setCreatedAt(new Date());
            comment.setTask(optionalTask.get());
            comment.setContent(content);
            comment.setUser(user);
            return commentRepository.save(comment).getCommentDto();
        }
        throw new EntityNotFoundException("User or Task is not found");
    }

    @Override
    public List<CommentDto> getAllCommentsByTaskId(Long taskId) {
        return commentRepository.findAllByTaskId(taskId).stream().map(Comment::getCommentDto).collect(Collectors.toList());
    }

    private TaskStatus mapStringToTaskStatus(String status){
        return switch (status){
            case "BEKLEMEDE" -> TaskStatus.BEKLEMEDE;
            case "BASLANDI" -> TaskStatus.BASLANDI;
            case "TAMAMLANDI" -> TaskStatus.TAMAMLANDI;
            case "ERTELENDI" -> TaskStatus.ERTELENDI;
            default -> TaskStatus.IPTAL;
        };
    }
}
