package com.mezzala.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class UserDto {

    private String userId;
    private String nickname;
    private String socialMethod;
    // default (now())
    private Date joinDate;
    // default (2)
    private int roleId;
    // default (true)
    private boolean state;
    private String accessToken;

    // 쓴 글 수
    private int writeBoardCount;
    // 쓴 댓글 수
    private int writeCommentCount;
    // 신고 당한 수
    private int reportCount;
    // 총 점수
    private int userScore;

    // UserAction - like
    List<UserActionDto> likeUserActions;

    // UserAction - bookmark
    List<UserActionDto> bookmarkUserActions;

    // user_role
    UserRoleDto userRole;

}
