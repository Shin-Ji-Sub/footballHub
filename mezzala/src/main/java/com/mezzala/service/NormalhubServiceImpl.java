package com.mezzala.service;

import com.mezzala.dto.BoardDto;
import com.mezzala.dto.BoardSmallCategoryDto;
import com.mezzala.mapper.MypageMapper;
import com.mezzala.mapper.NormalhubMapper;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class NormalhubServiceImpl implements NormalhubService {
    @Setter
    private NormalhubMapper normalhubMapper;

    @Override
    public List<BoardSmallCategoryDto> findAllBoardSmallCategory() {
        return normalhubMapper.selectAllBoardSmallCategory();
    }

    @Override
    public int findAllBoardCount(String userId) {
        return normalhubMapper.selectAllBoardCountInNormalhub(userId);
    }

    @Override
    public List<BoardDto> findBoardWithPaging(int start, String category, String searchValue, String userId, String sortValue) {
        List<BoardDto> boards = new ArrayList<>();
        if (searchValue.isEmpty()) {
            boards = normalhubMapper.selectBoardWithPaging(start, userId, sortValue, category);
        } else {
            boards = normalhubMapper.selectBoardWithPagingAndSearch(start, userId, sortValue, category, searchValue);
        }
        return boards;
    }
}
