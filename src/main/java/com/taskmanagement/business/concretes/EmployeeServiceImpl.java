package com.taskmanagement.business.concretes;

import com.taskmanagement.business.abstracts.EmployeeService;
import com.taskmanagement.business.core.utils.JWTUtil;
import com.taskmanagement.dtos.CommentDto;
import com.taskmanagement.dtos.TaskDto;
import com.taskmanagement.entities.Comment;
import com.taskmanagement.entities.Task;
import com.taskmanagement.entities.User;
import com.taskmanagement.enums.TaskStatus;
import com.taskmanagement.repository.CommentRepository;
import com.taskmanagement.repository.TaskRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final TaskRepository taskRepository;

    private final CommentRepository commentRepository;

    private final JWTUtil jwtUtil;


    @Override
    public List<TaskDto> getTasksByUserId() {
        User user = jwtUtil.getLoggedInUser();
        if (user != null) {

        return taskRepository.findAllByUserId(user.getId()).stream()
                .sorted(Comparator.comparing(Task::getDueDate).reversed())
                .map(Task::getTaskDto)
                .collect(Collectors.toList());
        }

        throw new EntityNotFoundException("User not found");
    }

    @Override
    public TaskDto updateTask(Long id, String status) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        if(optionalTask.isPresent()){
            Task existingTask = optionalTask.get();
            existingTask.setTaskStatus(mapStringToTaskStatus(status));
            return taskRepository.save(existingTask).getTaskDto();
        }
        throw new EntityNotFoundException("Task not found");
    }

    @Override
    public TaskDto getTaskById(Long id) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        return optionalTask.map(Task::getTaskDto).orElse(null);
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
