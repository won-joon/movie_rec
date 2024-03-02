package com.movierec.demo.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class UserTokens {

    private final String refreshToken;
    private final String accessToken;
}
