package com.mezzala.service;

import com.mezzala.dto.UserDto;

import java.util.List;

public interface AccountService {
    List<UserDto> addAccount(String userId, String nickname, String socialMethod);
}
