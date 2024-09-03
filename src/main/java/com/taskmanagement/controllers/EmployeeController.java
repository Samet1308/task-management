package com.taskmanagement.controllers;

import com.taskmanagement.business.abstracts.EmployeeService;
import com.taskmanagement.dtos.CommentDto;
import com.taskmanagement.dtos.TaskDto;
import com.taskmanagement.entities.Comment;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/employee")
public class EmployeeController {

    private final EmployeeService employeeService;


    @GetMapping("/tasks")
    public ResponseEntity<List<TaskDto>> getTasksByUserId(){
        return ResponseEntity.ok(employeeService.getTasksByUserId());
    }

    @GetMapping("/task/{id}/{status}")
    public ResponseEntity<TaskDto> updateTask(@PathVariable Long id, @PathVariable String status){
        TaskDto uptadeTaskDto = employeeService.updateTask(id, status);
        if(uptadeTaskDto == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        return ResponseEntity.ok(uptadeTaskDto);
    }

    @GetMapping("/task/{id}")
    public ResponseEntity<TaskDto> getTaskById(@PathVariable Long id){
        return ResponseEntity.ok(employeeService.getTaskById(id));
    }

    @PostMapping("/task/comment/{taskId}")
    public ResponseEntity<CommentDto> createComment(@PathVariable Long taskId, @RequestParam String content){
        CommentDto createdCommentDto = employeeService.createComment(taskId, content);
        if(createdCommentDto==null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(createdCommentDto);
    }

    @GetMapping("/comments/{taskId}")
    public ResponseEntity<List<CommentDto>> getAllCommentsByTaskId(@PathVariable Long taskId){
        return ResponseEntity.ok(employeeService.getAllCommentsByTaskId(taskId));
    }


}
