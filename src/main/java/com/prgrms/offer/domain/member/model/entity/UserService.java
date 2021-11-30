package com.prgrms.offer.domain.member.model.entity;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public User login(String principal, String credentials) {
        checkArgument(isNotEmpty(principal), "principal must be provided.");
        checkArgument(isNotEmpty(credentials), "credentials must be provided.");

        User user = userRepository.findByLoginId(principal)
                .orElseThrow(() -> new UsernameNotFoundException("Could not found user for " + principal));
        user.checkPassword(passwordEncoder, credentials);
        return user;
    }

    @Transactional(readOnly = true)
    public Optional<User> findByLoginId(String loginId) {
        checkArgument(isNotEmpty(loginId), "loginId must be provided.");
        return  userRepository.findByLoginId(loginId);
    }

}