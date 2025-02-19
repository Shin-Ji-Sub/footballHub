package com.mezzala.mapper;

import com.mezzala.dto.UserDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AccountMapper {
    UserDto selectUser(String userId);

    void insertAccount(@Param("userId") String userId, @Param("nickname") String nickname, @Param("socialMethod") String socialMethod);
}
