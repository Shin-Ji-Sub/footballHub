package com.mezzala.service;

import com.mezzala.dto.BoardLargeCategoryDto;
import com.mezzala.mapper.BoardMapper;
import lombok.Setter;

import java.util.List;

public class BoardServiceImpl implements BoardService{
    @Setter
    BoardMapper boardMapper;

    @Override
    public List<BoardLargeCategoryDto> findAllCategory() {
        return boardMapper.selectAllCategory();
    }
}
