package com.mezzala.service;

import com.mezzala.dto.BoardDto;
import com.mezzala.dto.CommentDto;
import com.mezzala.dto.UserDto;

import java.util.List;

public interface MypageService {

    UserDto modifyUserNickname(String nickname, String userId);

    List<BoardDto> findBoardWithUserId(int start, String userId);

    int findBoardCountWithUserId(String userId);

    int findCommentCountWithUserId(String userId);

    List<CommentDto> findCommentWithUserId(int start, String userId);

    int findLikedBoardCountWithUserId(String userId);

    List<BoardDto> findLikedBoardWithUserId(int start, String userId);

    int findLikedCommentCountWithUserId(String userId);

    List<CommentDto> findLikedCommentWithUserId(int start, String userId);

    int findBookmarkedBoardCountWithUserId(String userId);

    List<BoardDto> findBookmarkedBoards(int start, String userId);

    int findBlockUserCount(String userId);

    List<UserDto> findBlockUserWithUserId(int start, String userId);
}
