package com.mezzala.dto;

import lombok.Data;

@Data
public class BoardSmallCategoryDto {

    private int smallCategoryIndex;
    private String smallCategoryName;
    //Foreign Key (board_large_category)
    private int largeCategoryId;

}
