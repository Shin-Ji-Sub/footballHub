package com.mezzala.mapper;

import com.mezzala.dto.BoardLargeCategoryDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BoardMapper {
    List<BoardLargeCategoryDto> selectAllCategory();
}
