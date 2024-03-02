package com.movierec.demo.auth;

import com.movierec.demo.domain.User;
import com.movierec.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PrincipalService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> findUser = userRepository.findByUsername(username);
        if (findUser.isPresent()) {
            return new PrincipalDetails(findUser.get());
        }

        return null;
    }
}
