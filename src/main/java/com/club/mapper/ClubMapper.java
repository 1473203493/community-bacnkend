package com.club.mapper;

import com.club.entity.Club;
import com.club.entity.vo.ClubSimpleVO;
import com.club.entity.request.ClubQueryDto;
import com.club.entity.request.ClubApprovalDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface ClubMapper {

    // 搜索社团
    List<ClubSimpleVO> searchClubs(@Param("keyword") String keyword,
            @Param("categoryId") Integer categoryId);

    // 新增社团
    int insertClub(Club club);

    /**
     * 查询用户加入的社团列表
     */
    List<Club> selectByUserId(@Param("userId") Integer userId);

    // 查询社团名称是否已存在
    int countByName(@Param("name") String name);

    // 通过 ID 查询社团
    Club selectById(@Param("clubId") Integer clubId);

    List<Club> getClubList(ClubQueryDto queryDto);

    // 根据ID查询社团详情（用于审批查看）
    Club selectByIdByAdmin(@Param("clubId") Integer clubId);

    // 更新社团审批状态
    int updateApprovalStatus(ClubApprovalDto approvalDto);

    /**
     * 查询学生端社团列表
     */
    List<Map<String, Object>> selectClubsForStudent(@Param("params") Map<String, Object> params);

    /**
     * 查询学生端社团详情
     */
    Map<String, Object> selectClubDetailForStudent(@Param("clubId") Long clubId);

    /**
     * 查询社团负责人信息
     */
    Map<String, Object> selectFounderInfo(@Param("clubId") Long clubId);

    /**
     * 更新社团状态
     * @param club
     */
    void updateStatus(Club club);
}
