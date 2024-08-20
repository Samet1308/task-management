package com.taskmanagement.business.concretes;

import com.taskmanagement.business.abstracts.AdminService;
import com.taskmanagement.dtos.UserDto;
import com.taskmanagement.entities.User;
import com.taskmanagement.enums.UserRole;
import com.taskmanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    @Override
    public List<UserDto> getUsers() {
        return userRepository.findAll()
        .stream()
        .filter(user -> user.getUserRole() == UserRole.EMPLOYEE)
                .map(User::getUserDto)
        .collect(Collectors.toList());
    }
}
