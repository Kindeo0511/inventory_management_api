package com.project1.backend.User.mapper;

import com.project1.backend.User.Users;
import com.project1.backend.User.dto.CreateUserRequest;
import com.project1.backend.User.dto.UserResponse;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponse toResponse(Users user){
        return UserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .middleName(user.getMiddleName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .createdAt(user.getUpdatedAt())
                .build();

    }
    public Users toEntity(CreateUserRequest request){
        return Users.builder()
                .firstName(request.getFirstName())
                .middleName(request.getMiddleName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(request.getPassword())
                .role(request.getRole())
                .build();


    }


}
