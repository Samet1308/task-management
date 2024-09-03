package com.taskmanagement.business.abstracts;

import com.taskmanagement.dtos.CommentDto;
import com.taskmanagement.dtos.TaskDto;

import java.util.List;

public interface EmployeeService {

    List<TaskDto> getTasksByUserId();

    TaskDto updateTask(Long id, String status);

    TaskDto getTaskById(Long id);

    CommentDto createComment(Long taskId, String content);

    List<CommentDto> getAllCommentsByTaskId(Long taskId);
}
