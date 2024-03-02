package com.movierec.demo.dto;

import com.movierec.demo.domain.Role;
import com.movierec.demo.domain.User;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter @Setter
public class UserDto {
    @NotEmpty(message = "회원 이름은 필수 입니다")
    private String username;

    @NotEmpty(message = "비밀번호는 필수 입니다")
    private String password;

    private Role role;

    public User toEntity() {
        return User.builder().username(username).password(password).role(role).build();
    }
}
