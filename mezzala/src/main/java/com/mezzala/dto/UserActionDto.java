package com.mezzala.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class UserActionDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer actionId;
    private String actionCategory;
    // User
    private String userId;
    // Board
    private int boardId;

}
