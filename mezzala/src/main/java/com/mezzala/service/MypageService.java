package com.mezzala.service;

import com.mezzala.dto.BoardDto;
import com.mezzala.dto.UserDto;

import java.util.List;

public interface MypageService {

    UserDto modifyUserNickname(String nickname, String userId);

    List<BoardDto> findBoardWithUserId(int start, String userId);

    int findBoardCountWithUserId(String userId);
}
