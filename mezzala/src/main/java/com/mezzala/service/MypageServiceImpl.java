package com.mezzala.service;

import com.mezzala.dto.BoardDto;
import com.mezzala.dto.UserDto;
import com.mezzala.mapper.AccountMapper;
import com.mezzala.mapper.MypageMapper;
import lombok.Setter;

import java.util.List;

public class MypageServiceImpl implements MypageService{
    @Setter
    private MypageMapper mypageMapper;

    @Override
    public UserDto modifyUserNickname(String nickname, String userId) {
        mypageMapper.updateUserNickname(nickname, userId);
        return mypageMapper.selectUserWithUserId(userId);
    }

    @Override
    public int findBoardCountWithUserId(String userId) {
        return mypageMapper.selectBoardCountWithUserId(userId);
    }

    @Override
    public List<BoardDto> findBoardWithUserId(int start, String userId) {
        return mypageMapper.selectBoardWithUserId(start, userId);
    }

}
