package com.mezzala.dto;

import lombok.Data;

import java.util.Date;

@Data
public class UserActionDto {

    private Integer actionId;
    private String actionCategory;
    // User
    private String userId;
    // Board
    private int boardId;

}
