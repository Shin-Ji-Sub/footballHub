package com.mezzala.mapper;

import com.mezzala.dto.BoardDto;
import com.mezzala.dto.BoardLargeCategoryDto;
import com.mezzala.dto.CommentDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface BoardMapper {
    List<BoardLargeCategoryDto> selectAllCategory();

    void insertBoard(BoardDto board);

    List<BoardDto> selectBoardWithPaging(@Param("start") int start, @Param("category") String category);

    void insertBoardAttach(@Param("imageFiles") List<Map<String, String>> imageFiles, @Param("boardId") int boardId);

    int selectAllBoardCount();

    List<BoardDto> selectBoardWithPagingAndSearch(@Param("start") int start, @Param("category") String category, @Param("searchValue") String searchValue);

    List<BoardDto> selectBoardWithBoardId(int boardId);

    void updateVisitedBoard(int boardId);

    List<BoardDto> selectBoardWithBoardNo(int boardNo);

    void insertUserAction(@Param("userId") String userId, @Param("boardId") int boardId, @Param("actionCategory") String actionCategory);

    void deleteUserAction(@Param("userId") String userId, @Param("boardId") int boardId, @Param("actionCategory") String actionCategory);

    void insertComment(@Param("content") String content, @Param("boardId") int boardId, @Param("userId") String userId);

    List<CommentDto> selectCommentsWithBoardId(int boardId);
}
