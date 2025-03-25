package com.mezzala.mapper;

import com.mezzala.dto.BoardDto;
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
}
