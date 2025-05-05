package com.mezzala.mapper;

import com.mezzala.dto.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.sql.Timestamp;
import java.time.LocalDateTime;
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

    int selectAllReportBoardCommentCount(String searchValue);

    List<CommentDto> selectReportBoardCommentWithPaging(@Param("start") int start, @Param("sortValue") String sortValue,
                                                      @Param("searchValue") String searchValue);

    int selectAllLargeCategoryCount();

    int selectAllSmallCategoryCount(int largeCategoryValue);

    List<BoardLargeCategoryDto> selectLargeCategory(int start);

    List<BoardLargeCategoryDto> selectAllLargeCategory();

    List<BoardSmallCategoryDto> selectSmallCategory(@Param("start") int start, @Param("largeCategoryValue") int largeCategoryValue);

    void insertLargeCategory(String categoryValue);

    void insertSmallCategory(@Param("categoryValue") String categoryValue, @Param("largeCategoryValue") String largeCategoryValue);

    void updateLargeCategory(@Param("categoryName") String categoryName, @Param("categoryId") int categoryId);

    void updateSmallCategory(@Param("categoryName") String categoryName, @Param("categoryId") int categoryId);

    void deleteLargeCategory(int categoryId);

    void deleteSmallCategory(int categoryId);

    List<UserRoleDto> selectAllUserRole();

    int selectAllUserCount(String category, String searchValue);

    List<UserDto> selectUserList(@Param("start") int start, @Param("category") String category,
                                 @Param("searchValue") String searchValue, @Param("sortValue") String sortValue);

    void updateUserRole(@Param("roleValue") int roleValue, @Param("userId") String userId);

    void insertCompetition(String value);

    void insertCompetitionRound(String value);

    void insertTeam(@Param("value") String value, @Param("logo") String logo);

    List<TeamDto> selectAllTeam();

    List<CompetitionDto> selectAllCompetition();

    List<CompetitionRoundDto> selectAllCompetitionRound();

    void insertSchedule(ScheduleDto schedule);

    int selectAllCompetitionCount(String searchValue);

    List<CompetitionDto> selectCompetition(@Param("start") int start, @Param("searchValue") String searchValue);

    int selectAllCompetitionRoundCount(String searchValue);

    List<CompetitionRoundDto> selectCompetitionRound(@Param("start") int start, @Param("searchValue") String searchValue);

    int selectAllTeamCount(String searchValue);

    List<TeamDto> selectTeam(@Param("start") int start, @Param("searchValue") String searchValue);

    void updateCompetitionName(@Param("id") int id, @Param("name") String name);

    void updateCompetitionRoundName(@Param("id") int id, @Param("name") String name);

    void updateTeam(@Param("id") int id, @Param("name") String name, @Param("logo") String logo);

    void deleteTeam(@Param("id") int id, @Param("category") String category);

    int selectAllScheduleCount(@Param("category") String category, @Param("fromDay") String fromDay,
                               @Param("tillDay") String tillDay);

    List<ScheduleDto> selectSchedule(@Param("start") int start, @Param("category") String category,
                                     @Param("fromDay") String fromDay, @Param("tillDay") String tillDay);

    void updateSchedule(@Param("schedule") ScheduleDto schedule, @Param("date") LocalDateTime date);

    void deleteSchedule(int scheduleId);
}
