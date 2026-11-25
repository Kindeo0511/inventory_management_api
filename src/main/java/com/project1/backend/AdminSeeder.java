package com.project1.backend;

import com.project1.backend.User.*;
import com.project1.backend.User.dto.CreateUserRequest;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Builder
public class AdminSeeder implements ApplicationListener<ContextRefreshedEvent> {
    private final UserRepository userRepository;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        this.createOwnerAdminstrator();
    }

    private void createOwnerAdminstrator(){
        CreateUserRequest request = CreateUserRequest.builder()
                .firstName("test")
                .middleName("test")
                .lastName("test")
                .email("admin@gmail.com")
                .password("admin123")
                .role(Role.ADMIN)
                .build();

        if(!userRepository.findAll().isEmpty()){
            return;
        }


        Users user = Users.builder()
                .firstName(request.getFirstName())
                .middleName(request.getMiddleName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(encoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();

        userRepository.save(user);
    }
}
