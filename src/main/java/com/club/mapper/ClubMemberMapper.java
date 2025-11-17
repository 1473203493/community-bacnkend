package com.club.mapper;

import com.club.entity.ClubMember;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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
}