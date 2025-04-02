package com.mezzala.service;

import com.mezzala.dto.BoardDto;
import com.mezzala.dto.BoardSmallCategoryDto;

import java.util.List;

public interface NormalhubService {

    List<BoardSmallCategoryDto> findAllBoardSmallCategory();

    int findAllBoardCount(String userId);

    List<BoardDto> findBoardWithPaging(int start, String category, String searchValue, String userId, String sortValue);
}
