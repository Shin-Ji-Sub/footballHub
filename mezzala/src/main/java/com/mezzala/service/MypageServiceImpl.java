package com.mezzala.service;

import com.mezzala.dto.BoardDto;
import com.mezzala.dto.CommentDto;
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

    @Override
    public int findCommentCountWithUserId(String userId) {
        return mypageMapper.selectCommentCountWithUserId(userId);
    }

    @Override
    public List<CommentDto> findCommentWithUserId(int start, String userId) {
        return mypageMapper.selectCommentWithUserId(start, userId);
    }

    @Override
    public int findLikedBoardCountWithUserId(String userId) {
        return mypageMapper.selectLikedBoardCountWithUserId(userId);
    }

    @Override
    public List<BoardDto> findLikedBoardWithUserId(int start, String userId) {
        return mypageMapper.selectLikedBoardWithUserId(start, userId);
    }

    @Override
    public int findLikedCommentCountWithUserId(String userId) {
        return mypageMapper.selectLikedCommentCountWithUserId(userId);
    }

    @Override
    public List<CommentDto> findLikedCommentWithUserId(int start, String userId) {
        return mypageMapper.selectLikedCommentWithUserId(start, userId);
    }

    @Override
    public int findBookmarkedBoardCountWithUserId(String userId) {
        return mypageMapper.selectBookmarkedBoardCountWithUserId(userId);
    }

    @Override
    public List<BoardDto> findBookmarkedBoards(int start, String userId) {
        return mypageMapper.selectBookmarkedBoards(start, userId);
    }

}
