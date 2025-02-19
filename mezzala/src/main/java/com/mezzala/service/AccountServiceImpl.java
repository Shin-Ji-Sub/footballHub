package com.mezzala.service;

import com.mezzala.dto.UserDto;
import com.mezzala.mapper.AccountMapper;
import lombok.Setter;

public class AccountServiceImpl implements AccountService{
    @Setter
    private AccountMapper accountMapper;

    @Override
    public UserDto addAccount(String userId, String nickname, String socialMethod) {
        UserDto user = accountMapper.selectUser(userId);
        if (user == null) {
            accountMapper.insertAccount(userId, nickname, socialMethod);
            user = accountMapper.selectUser(userId);
        }

        return user;
    }
}
