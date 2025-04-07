package com.mezzala.mapper;

import com.mezzala.dto.BoardDto;
import com.mezzala.dto.BoardSmallCategoryDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FandomhubMapper {

    List<BoardSmallCategoryDto> selectAllBoardSmallCategory();

    int selectAllBoardCount(@Param("userId") String userId, @Param("category") String category, @Param("searchValue") String searchValue);

    List<BoardDto> selectBoardWithPaging(@Param("start") int start, @Param("category") String category,
                                         @Param("userId") String userId, @Param("sortValue") String sortValue);

    List<BoardDto> selectBoardWithPagingAndSearch(@Param("start") int start, @Param("userId") String userId,
                                                  @Param("sortValue") String sortValue, @Param("category") String category,
                                                  @Param("searchValue") String searchValue);
}
