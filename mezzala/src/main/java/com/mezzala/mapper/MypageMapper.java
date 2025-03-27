package com.mezzala.mapper;

import com.mezzala.dto.BoardDto;
import com.mezzala.dto.CommentDto;
import com.mezzala.dto.UserDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MypageMapper {

    void updateUserNickname(@Param("nickname") String nickname, @Param("userId") String userId);

    UserDto selectUserWithUserId(String userId);

    List<BoardDto> selectBoardWithUserId(@Param("start") int start, @Param("userId") String userId);

    int selectBoardCountWithUserId(String userId);

    int selectCommentCountWithUserId(String userId);

    List<CommentDto> selectCommentWithUserId(@Param("start") int start, @Param("userId") String userId);

    int selectLikedBoardCountWithUserId(String userId);

    List<BoardDto> selectLikedBoardWithUserId(@Param("start") int start, @Param("userId") String userId);

    int selectLikedCommentCountWithUserId(String userId);

    List<CommentDto> selectLikedCommentWithUserId(@Param("start") int start, @Param("userId") String userId);

    int selectBookmarkedBoardCountWithUserId(String userId);

    List<BoardDto> selectBookmarkedBoards(@Param("start") int start, @Param("userId") String userId);
}
