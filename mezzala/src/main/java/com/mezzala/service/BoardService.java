package com.mezzala.service;

import com.mezzala.dto.BoardLargeCategoryDto;

import java.util.List;

public interface BoardService {
    List<BoardLargeCategoryDto> findAllCategory();
}
