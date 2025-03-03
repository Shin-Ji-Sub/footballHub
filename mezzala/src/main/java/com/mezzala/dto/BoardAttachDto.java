package com.mezzala.dto;

import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
public class BoardAttachDto {

    private int attachId;
    private String userFileName;
    private String savedFileName;
    // default(0)
    private int downloadCount;

    // board
    private int boardId;

}
