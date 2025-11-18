package com.club.mapper;

import com.club.entity.Club;
import com.club.entity.vo.ClubSimpleVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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


}
