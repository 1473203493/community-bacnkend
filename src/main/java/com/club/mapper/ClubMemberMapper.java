package com.club.mapper;

import com.club.entity.ClubMember;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;;

@Mapper
public interface ClubMemberMapper {
    /**
     * 根据用户ID和社团ID查询社团成员信息
     */
    ClubMember selectMemberByUserIdAndClubId(@Param("clubId") Integer clubId,
            @Param("userId") Integer userId);

    // 获取社团所有成员
    List<ClubMember> selectMembersByClubId(@Param("clubId") Integer clubId);

    // 更新成员角色
    int updateMemberRole(@Param("clubId") Integer clubId,
            @Param("userId") Integer userId,
            @Param("newRole") String newRole,
            @Param("reason") String reason);

    /**
     * 查询社团前N名成员
     */
    List<Map<String, Object>> selectTopMembersByClubId(@Param("clubId") Long clubId, @Param("limit") Integer limit);

    /**
     * 统计用户在某个社团的成员数量
     */
    Integer countByUserIdAndClubId(@Param("userId") Long userId, @Param("clubId") Long clubId);

    /**
     * 查询用户加入的社团列表
     */
    List<Map<String, Object>> selectMyClubs(@Param("userId") Long userId);

    /**
     * 插入一条新的社团成员记录
     */
    void insert(ClubMember clubMember);

    /**
     * 更新社团成员加入状态
     * @param clubMember
     */
    @Update("update club_member set join_status = #{joinStatus} where club_id = #{clubId} and user_id = #{userId}")
    void updateMemberJoinStatus(ClubMember clubMember);
}