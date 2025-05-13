package com.mezzala.service;

import com.mezzala.dto.UserDto;

import java.util.List;

public interface AccountService {
    List<UserDto> addAccount(String userId, String nickname, String socialMethod);

    void addBlockUser(String blockUser, String userId);

    void deleteBlockUser(String blockUserId, String userId);

    void addReport(String reportCategory, int contentId, String userId);

    void deleteUser(String userId);
}
