package com.club.mapper;



import com.club.entity.vo.ClubSimpleVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface HomeMapper {

    /**
     * 获取热门社团推荐（按时间排序）
     */
    List<ClubSimpleVO> selectHotClubs(@Param("limit") int limit);

    /**
     * 获取人气社团推荐（按人数前三排序）
     */
    List<ClubSimpleVO> selectPopularClubs(@Param("limit") int limit);
}