package com.mezzala.dto;

import lombok.Data;

import java.util.Date;

@Data
public class UserDto {

    private String userId;
    private String nickname;
    private String socialMethod;
    // default (now())
    private Date joinDate;
    // default (false)
    private boolean isAdmin;
    // default (true)
    private boolean state;

}
