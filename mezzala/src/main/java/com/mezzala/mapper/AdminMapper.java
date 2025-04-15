package com.mezzala.mapper;

import com.mezzala.dto.BoardDto;
import com.mezzala.dto.BoardLargeCategoryDto;
import com.mezzala.dto.CommentDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@Mapper
public interface AdminMapper {
    void insertBoard(BoardDto board);

    void insertBoardAttach(@Param("imageFiles") List<Map<String, String>> imageFiles, @Param("boardId") int boardId);

    int selectAllBoardCount(@Param("searchValue") String searchValue, @Param("state") boolean state);

    List<BoardDto> selectBoardWithPaging(@Param("start") int start, @Param("searchValue") String searchValue,
                                         @Param("state") boolean state);

    void updateBoardState(@Param("boardId") int boardId, @Param("state") boolean state);

    void updateVisitedBoard(int boardId);

    List<BoardDto> selectBoardWithBoardId(int boardId);

    void updateBoardsState(List<Integer> contentIds);
}
