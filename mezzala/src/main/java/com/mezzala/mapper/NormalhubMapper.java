package com.mezzala.mapper;

import com.mezzala.dto.BoardDto;
import com.mezzala.dto.BoardSmallCategoryDto;
import com.mezzala.dto.UserDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface NormalhubMapper {
    List<BoardSmallCategoryDto> selectAllBoardSmallCategory();

    int selectAllBoardCountInNormalhub(String userId);

    List<BoardDto> selectBoardWithPaging(@Param("start") int start, @Param("userId") String userId,
                                         @Param("sortValue") String sortValue, @Param("category") String category);

    List<BoardDto> selectBoardWithPagingAndSearch(@Param("start") int start, @Param("userId") String userId,
                                                  @Param("sortValue") String sortValue, @Param("category") String category,
                                                  @Param("searchValue") String searchValue);
}
