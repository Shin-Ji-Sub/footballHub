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

    int selectAllNoticeBoardCount(@Param("searchValue") String searchValue, @Param("state") boolean state);

    List<BoardDto> selectNoticeBoardWithPaging(@Param("start") int start, @Param("searchValue") String searchValue,
                                         @Param("state") boolean state);

    void updateBoardState(@Param("boardId") int boardId, @Param("state") boolean state);

    void updateVisitedBoard(int boardId);

    List<BoardDto> selectNoticeBoardWithBoardId(int boardId);

    void updateBoardsState(List<Integer> contentIds);

    List<BoardLargeCategoryDto> selectCategories();

    int selectAllBoardCount(@Param("totalSelectValue") String totalSelectValue, @Param("smallSelectValue") String smallSelectValue,
                            @Param("searchValue") String searchValue);

    List<BoardDto> selectBoardWithPaging(@Param("start") int start, @Param("sortValue") String sortValue,
                                         @Param("totalSelectValue") String totalSelectValue, @Param("smallSelectValue") String smallSelectValue,
                                         @Param("searchValue") String searchValue);

    List<BoardDto> selectBoardWithBoardId(int boardId);

    int selectAllReportBoardCount(String searchValue);

    List<BoardDto> selectReportBoardWithPaging(@Param("start") int start, @Param("sortValue") String sortValue,
                                               @Param("searchValue") String searchValue);
}
