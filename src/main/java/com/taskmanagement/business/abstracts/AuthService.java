package com.taskmanagement.business.abstracts;

import com.taskmanagement.dtos.SignupRequest;
import com.taskmanagement.dtos.UserDto;

public interface AuthService {

    UserDto signupUser(SignupRequest signupRequest);

    boolean hasUserWithEmail(String email);
}
