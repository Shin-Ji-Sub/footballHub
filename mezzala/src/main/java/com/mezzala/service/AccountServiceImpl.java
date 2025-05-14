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
        if (user == null || user.isEmpty()) {
            accountMapper.insertAccount(userId, nickname, socialMethod);
            user = accountMapper.selectUser(userId);
        }

        return user;
    }

    @Override
    public void addBlockUser(String blockUser, String userId) {
        accountMapper.insertBlockUSer(blockUser, userId);
    }

    @Override
    public void deleteBlockUser(String blockUserId, String userId) {
        accountMapper.deleteBlockUser(blockUserId, userId);
    }

    @Override
    public void addReport(String reportCategory, int contentId, String userId) {
        accountMapper.insertReport(reportCategory, contentId, userId);
    }

    @Override
    public void deleteUser(String userId) {
        accountMapper.updateUserState(userId);
    }
}
