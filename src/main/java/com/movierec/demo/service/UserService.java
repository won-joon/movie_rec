package com.movierec.demo.service;

import com.movierec.demo.domain.Role;
import com.movierec.demo.domain.User;
import com.movierec.demo.dto.UserDto;
import com.movierec.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;

    public void join(UserDto userDto) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        userRepository.findByUsername(userDto.getUsername()).ifPresent(e -> {throw new IllegalStateException("이미 존재하는 회원입니다");});
        String pw = encoder.encode(userDto.getPassword());
        userDto.setPassword(pw);
        userDto.setRole(Role.USER);
        userRepository.save(userDto.toEntity());
    }

}
