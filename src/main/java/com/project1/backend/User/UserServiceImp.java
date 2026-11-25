package com.project1.backend.User;

import com.project1.backend.User.Specification.UserSpecification;
import com.project1.backend.User.dto.*;
import com.project1.backend.User.mapper.UserMapper;
import com.project1.backend.security.JWTService;
import com.project1.backend.exception.BadRequestException;
import com.project1.backend.exception.DuplicateResourceException;
import com.project1.backend.exception.ResourceNotFoundException;
import com.project1.backend.util.SecurityUtils;
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService{
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Page<UserResponse> getAllUsers(String name, String role, Pageable pageable) {
        log.debug("Fetching all users");

        Specification<Users> specification = Specification.allOf(
                UserSpecification.hasName(name),
                UserSpecification.hasRole(role)
        );

        Page<Users> users = userRepository.findAll(specification, pageable);
        log.info("Successfully fetched {} user", users.getSize());
        return users.map(userMapper::toResponse);

    }


    @Override
    public UserResponse createUser(CreateUserRequest userRequest) {
        log.debug("Creating new user");


        if(userRepository.existsByEmail(userRequest.getEmail())){
            throw new DuplicateResourceException("Email already exists.");
        }

        if (!userRequest.getPassword().equals(userRequest.getConfirmPassword())) {
            throw  new BadRequestException("Password does not match.");
        }


        Users users = userMapper.toEntity(userRequest);
        users.setPassword(passwordEncoder.encode(users.getPassword()));
        Users savedUsers = userRepository.save(users);

        log.info("Successfully created a user with id: {}",savedUsers.getId());
        return userMapper.toResponse(savedUsers);

    }

    @Override
    public UserResponse updateUser(UpdateUserRequest userRequest, Long id) {
        log.debug("Updating a user");
        Users existing = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if(!existing.getVersion().equals(userRequest.getVersion())){
            throw new OptimisticLockException("User has been updated by another user");
        }
        if (userRepository.existsByEmailAndIdNot(userRequest.getEmail(), id)){
            throw new DuplicateResourceException("Email already exists.");
        };

        existing.setFirstName(userRequest.getFirstName());
        existing.setMiddleName(userRequest.getMiddleName());
        existing.setLastName(userRequest.getLastName());
        existing.setEmail(userRequest.getEmail());
        existing.setRole(userRequest.getRole());

        Users updatedUsers = userRepository.save(existing);

        log.info("Successfully updated a user with id: {}",updatedUsers.getId());
        return userMapper.toResponse(updatedUsers);
    }

    @Override
    public void deleteUserById(Long id) {
        log.debug("Deleting a user");
        Users user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        user.setDeleted(true);
        log.info("Successfully deleted a user with id: {}",id);
        userRepository.save(user);
    }

    @Override
    public UserResponse getUserById(Long id) {
        log.debug("Fetching user");

        Users user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        log.info("Successfully with user with id: {}",id);
        return userMapper.toResponse(user);

    }

    @Override
    public UserResponse updateUserProfile(UpdateUserRequest request) {
        log.debug("Updating user profile");
        Users user = userRepository.findByEmail(SecurityUtils.getCurrentUser())
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));
        user.setFirstName(request.getFirstName());
        user.setMiddleName(request.getMiddleName());
        user.setLastName(request.getLastName());

        Users updatedUser = userRepository.save(user);
        log.info("Successfully updated user profile with id: {}", updatedUser.getId());
        return userMapper.toResponse(updatedUser);

    }

    @Override
    public UserResponse updateUserEmail(ChangeEmailRequest request) {
        log.debug("Updating user email");
        Users user = userRepository.findByEmail(SecurityUtils.getCurrentUser())
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));

        if (userRepository.existsByEmailAndIdNot(request.getEmail(), user.getId())){
            throw new DuplicateResourceException("Email already exists.");
        };

        user.setEmail(request.getEmail());
        Users updatedUserEmail = userRepository.save(user);
        log.info("Successfully updated user email");
        return userMapper.toResponse(updatedUserEmail);
    }

    @Override
    public void updateUserPassword(ChangePasswordRequest request) {
        log.debug("Updating user password");
        Users user = userRepository.findByEmail(SecurityUtils.getCurrentUser())
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));


        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new BadRequestException("Wrong current password.");
        }
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw  new BadRequestException("Password does not match.");
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        log.info("Successfully updated user password");
        userRepository.save(user);
    }
}
