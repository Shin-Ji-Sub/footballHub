package com.mezzala.service;

import com.mezzala.dto.BoardDto;
import com.mezzala.dto.BoardSmallCategoryDto;
import com.mezzala.mapper.FandomhubMapper;
import com.mezzala.mapper.NormalhubMapper;
import lombok.Setter;

import javax.naming.directory.SearchControls;
import java.util.ArrayList;
import java.util.List;

public class FandomhubServiceImpl implements FandomhubService {
    @Setter
    private FandomhubMapper fandomhubMapper;


    @Override
    public List<BoardSmallCategoryDto> findAllBoardSmallCategory() {
        return fandomhubMapper.selectAllBoardSmallCategory();
    }

    @Override
    public int findAllBoardCount(String userId, String category, String searchValue) {
        return fandomhubMapper.selectAllBoardCount(userId, category, searchValue);
    }

    @Override
    public List<BoardDto> findBoardWithPaging(int start, String category, String searchValue, String userId, String sortValue) {
        List<BoardDto> boards = new ArrayList<>();
        if (searchValue.isEmpty()) {
            boards = fandomhubMapper.selectBoardWithPaging(start, category, userId, sortValue);
        } else {
            boards = fandomhubMapper.selectBoardWithPagingAndSearch(start, userId, sortValue, category, searchValue);
        }
        return boards;
    }
}
