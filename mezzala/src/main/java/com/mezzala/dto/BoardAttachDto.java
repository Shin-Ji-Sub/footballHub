package com.mezzala.dto;

import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
public class BoardAttachDto {

    private int attachId;
    private String userFileName;
    private String savedFileName;

    // board
    private int boardId;

}
