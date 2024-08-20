package com.taskmanagement.business.abstracts;

import com.taskmanagement.dtos.UserDto;

import java.util.List;

public interface AdminService {

    List<UserDto> getUsers();
}
