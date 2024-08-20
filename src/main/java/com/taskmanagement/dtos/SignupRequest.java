package com.taskmanagement.dtos;

import com.taskmanagement.enums.UserRole;
import lombok.Data;

@Data
public class SignupRequest {

    private String name;
    private String email;
    private String password;
    private UserRole userRole;
}
