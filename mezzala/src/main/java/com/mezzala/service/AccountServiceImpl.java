package com.mezzala.service;

import com.mezzala.dto.UserDto;
import com.mezzala.mapper.AccountMapper;
import lombok.Setter;

import java.util.List;

public class AccountServiceImpl implements AccountService{
    @Setter
    private AccountMapper accountMapper;

    @Override
    public List<UserDto> addAccount(String userId, String nickname, String socialMethod) {
        List<UserDto> user = accountMapper.selectUser(userId);
        if (user == null) {
            accountMapper.insertAccount(userId, nickname, socialMethod);
            user = accountMapper.selectUser(userId);
        }

        return user;
    }
}
