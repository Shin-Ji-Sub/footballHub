package com.mezzala.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class BoardLargeCategoryDto {
    private int largeCategoryId;
    private String largeCategoryName;
    List<BoardSmallCategoryDto> smallCategories;
}
