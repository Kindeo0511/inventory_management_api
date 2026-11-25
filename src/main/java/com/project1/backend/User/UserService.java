package com.project1.backend.User;

import com.project1.backend.User.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    Page<UserResponse> getAllUsers(String name, String role, Pageable pageable);
    UserResponse createUser(CreateUserRequest request);
    UserResponse updateUser(UpdateUserRequest request, Long id);
    void deleteUserById(Long id);
    UserResponse getUserById(Long id);
    UserResponse updateUserProfile(UpdateUserRequest request);
    UserResponse updateUserEmail(ChangeEmailRequest request);
    void updateUserPassword(ChangePasswordRequest request);
}
