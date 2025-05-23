package com.mezzala.mapper;

import com.mezzala.dto.UserDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface AccountMapper {
    List<UserDto> selectUser(String userId);

    void insertAccount(@Param("userId") String userId, @Param("nickname") String nickname,
                       @Param("socialMethod") String socialMethod , @Param("accessToken") String accessToken,
                       @Param("expiresAt") LocalDateTime expiresAt);

    void insertBlockUSer(@Param("blockUser") String blockUser, @Param("userId") String userId);

    void deleteBlockUser(@Param("blockUserId") String blockUserId, @Param("userId") String userId);

    void insertReport(@Param("reportCategory") String reportCategory, @Param("contentId") int contentId, @Param("userId") String userId);

    void updateUserState(String userId);

    void updateUserToken(@Param("userId") String userId, @Param("accessToken") String accessToken,
                         @Param("expiresAt") LocalDateTime expiresAt);
}
