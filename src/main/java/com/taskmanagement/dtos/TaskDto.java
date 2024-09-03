package com.taskmanagement.dtos;

import com.taskmanagement.enums.TaskStatus;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.Date;

@Data
public class TaskDto {

    private Long id;

    private String title;

    private  String description;

    private Date dueDate;

    private  String priority;

    private TaskStatus taskStatus;

    private Long employeeId;

    private String employeeName;
}
