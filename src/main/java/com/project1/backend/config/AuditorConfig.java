package com.project1.backend.config;

import com.project1.backend.User.UserRepository;
import com.project1.backend.User.Users;
import com.project1.backend.exception.ResourceNotFoundException;
import com.project1.backend.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("auditorConfig")
@RequiredArgsConstructor
public class AuditorConfig implements AuditorAware<String> {
    private final UserRepository userRepository;
    @Override
    public Optional<String> getCurrentAuditor() {
//        Users user = userRepository.findById(SecurityUtils.getCurrentUserId())
//                .orElseThrow(() -> new ResourceNotFoundException("User not found."));
//        String fullName = user.getFirstName()+" "+user.getLastName();
        return Optional.of("Admin");
    }
}
