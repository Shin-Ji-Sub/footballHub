package com.mezzala.service;

import com.mezzala.dto.*;

import java.util.List;
import java.util.Map;

public interface AdminService {
    void addBoard(BoardDto board, List<Map<String, String>> imageFiles);

    int findAllNoticeBoardCount(String searchValue, boolean state);

    List<BoardDto> findNoticeBoardWithPaging(int start, String searchValue, boolean state);

    void modifyBoardState(int boardId, boolean state);

    void incrementVisitedBoard(int boardId);

    List<BoardDto> findNoticeBoardWithBoardId(int boardId);

    void modifyBoardsState(List<Integer> contentIds);

    List<BoardLargeCategoryDto> findCategories();

    int findAllBoardCount(String totalSelectValue, String smallSelectValue, String searchValue);

    List<BoardDto> findBoardWithPaging(int start, String sortValue, String totalSelectValue, String smallSelectValue, String searchValue);

    List<BoardDto> findBoardWithBoardId(int boardId);

    int findAllReportBoardCount(String searchValue);

    List<BoardDto> findReportBoardWithPaging(int start, String sortValue, String searchValue);

    int findAllReportBoardCommentCount(String searchValue);

    List<CommentDto> findReportBoardCommentWithPaging(int start, String sortValue, String searchValue);

    int findAllCategoryCount(String categoryPart, int largeCategoryValue);

    List<BoardLargeCategoryDto> findLargeCategory(int start);

    List<BoardSmallCategoryDto> findSmallCategory(int start, int largeCategoryValue);

    List<BoardLargeCategoryDto> findAllLargeCategory();

    void addCategory(String part, String categoryValue, String largeCategoryValue);

    void modifyCategoryName(String categoryName, int categoryId, String categoryPart);

    void deleteCategory(String categoryPart, int categoryId);

    List<UserRoleDto> findAllUserRole();

    int findAllUserCount(String category, String searchValue);

    List<UserDto> findUserList(int start, String category, String searchValue, String sortValue);

    void modifyUserRole(int roleValue, String userId);

    void addScheduleCategory(String value, String logo, String from, String competitionCategory);

    List<TeamDto> findAllTeam();

    List<CompetitionDto> findAllCompetition();

    List<CompetitionRoundDto> findAllCompetitionRound();

    void addSchedule(ScheduleDto schedule);

    int findAllCompetitionCount(String searchValue);

    List<CompetitionDto> findCompetition(int start, String searchValue);

    int findAllCompetitionRoundCount(String searchValue);

    List<CompetitionRoundDto> findCompetitionRound(int start, String searchValue);

    int findAllTeamCount(String searchValue);

    List<TeamDto> findTeam(int start, String searchValue);

    void modifyNameAndLogo(int id, String name, String logo, String category, String competitionCategory);

    void deleteTeam(int id, String category);

    int findAllScheduleCount(String category, int year, int month, int day);

    Map<Integer, List<ScheduleDto>> findSchedule(int start, String category, int year, int month, int day);

    void modifySchedule(ScheduleDto schedule, int year, int month, int day, int hour, int minute);

    void removeSchedule(int scheduleId);

    void addRanking(int seasonValue, int competitionId, int homeId, int homeScore, int awayId, int awayScore);

    void addCompetitionLeague(int competitionId, int clFrom, int clTo, int olFrom, int olTo);

    List<RankingDto> findRanking(int competitionValue, int seasonValue);

    void modifyRanking(RankingDto ranking);

    YoutubeDto findYoutube();

    void modifyYoutube(String part, String value, int youtubeId);
}
