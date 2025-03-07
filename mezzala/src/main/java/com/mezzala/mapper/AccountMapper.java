package com.mezzala.mapper;

import com.mezzala.dto.UserDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AccountMapper {
    List<UserDto> selectUser(String userId);

    void insertAccount(@Param("userId") String userId, @Param("nickname") String nickname, @Param("socialMethod") String socialMethod);
}
