package com.mezzala.service;

import com.mezzala.dto.UserDto;

public interface AccountService {
    UserDto addAccount(String userId, String nickname, String socialMethod);
}
