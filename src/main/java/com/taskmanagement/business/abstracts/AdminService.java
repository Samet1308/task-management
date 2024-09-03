package com.taskmanagement.business.abstracts;

import com.taskmanagement.dtos.CommentDto;
import com.taskmanagement.dtos.TaskDto;
import com.taskmanagement.dtos.UserDto;
import com.taskmanagement.entities.Task;

import java.util.List;

public interface AdminService {

    List<UserDto> getUsers();

    TaskDto createTask(TaskDto taskDto);

    List<TaskDto> getAllTasks();

    void deleteTask(Long id);

    TaskDto getTaskById(Long id);

    TaskDto updateTask(Long id, TaskDto taskDto);

    List<TaskDto> searchTaskByTitle(String title);

    CommentDto createComment(Long taskId, String content);

    List<CommentDto> getAllCommentsByTaskId(Long taskId);
}
