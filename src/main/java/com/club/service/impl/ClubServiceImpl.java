package com.club.service.impl;

import com.club.entity.Club;
import com.club.entity.request.ClubApprovalDto;
import com.club.entity.request.ClubQueryDto;
import com.club.entity.vo.ResultCodeEnum;
import com.club.exception.ClubDefinedException;
import com.club.entity.vo.ClubCreateRequestVO;
import com.club.entity.vo.Result;

import com.club.mapper.ClubMapper;
import com.club.mapper.UserMapper;
import com.club.mapper.ClubMemberMapper;
import com.club.mapper.ClubApplyMapper;
import com.club.mapper.ActivityMapper;
import com.club.service.ClubService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ClubServiceImpl implements ClubService {

    @Autowired
    private ClubMapper clubMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ClubMemberMapper clubMemberMapper;

    @Autowired
    private ClubApplyMapper clubApplyMapper;

    @Autowired
    private ActivityMapper activityMapper;

    @Override
    public Result<Void> createClub(ClubCreateRequestVO request) {
        try {
            // 检查社团名称是否重复
            int count = clubMapper.countByName(request.getName());
            if (count > 0) {
                return Result.build(null, 400, "社团名称已存在");
            }

            // VO → Entity
            Club club = new Club();
            club.setName(request.getName());
            club.setCategoryId(request.getCategoryId());
            club.setDescription(request.getDescription());
            club.setCharter(request.getCharter());
            club.setFounderId(request.getFounderId());
            club.setStatus("1"); // 待平台管理员确认
            club.setCreatedAt(LocalDateTime.now());

            // 插入数据库
            int rows = clubMapper.insertClub(club);
            if (rows > 0) {
                return Result.build(null, 200, "社团创建成功，等待管理员审核");
            } else {
                return Result.build(null, 500, "社团创建失败");
            }

        } catch (Exception e) {
            log.error("新增社团异常", e);
            return Result.build(null, 500, "新增社团失败：" + e.getMessage());
        }

    }

    @Override
    public PageInfo<Club> getClubList(ClubQueryDto queryDto) {
        // 只有当分页参数都不为空时才开启分页
        if (queryDto.getPageNum() != null && queryDto.getPageSize() != null) {
            PageHelper.startPage(queryDto.getPageNum(), queryDto.getPageSize());
        }
        // 执行查询
        List<Club> clubList = clubMapper.getClubList(queryDto);
        // 封装分页结果
        return new PageInfo<>(clubList);
    }

    @Override
    public Club getClubDetail(Integer clubId) {
        if (clubId == null) {
            throw new ClubDefinedException(ResultCodeEnum.PARAM_ERROR);
        }
        Club club = clubMapper.selectByIdByAdmin(clubId);
        if (club == null) {
            throw new ClubDefinedException(ResultCodeEnum.CLUB_NOT_EXIST);
        }
        return club;
    }

    @Transactional
    @Override
    public void approveClub(ClubApprovalDto approvalDto) {
        // 参数校验
        if (approvalDto.getClubId() == null || approvalDto.getStatus() == null) {
            throw new ClubDefinedException(ResultCodeEnum.PARAM_ERROR);
        }

        // 校验状态是否合法
        if (!"2".equals(approvalDto.getStatus()) && !"4".equals(approvalDto.getStatus())) {
            throw new ClubDefinedException(ResultCodeEnum.PARAM_ERROR);
        }

        // 拒绝时必须填写理由
        if ("4".equals(approvalDto.getStatus()) &&
                (approvalDto.getRejectReason() == null || approvalDto.getRejectReason().trim().isEmpty())) {
            throw new ClubDefinedException(ResultCodeEnum.REJECT_REASON_REQUIRED);
        }

        // 检查社团是否存在且状态为待审批
        Club club = clubMapper.selectByIdByAdmin(approvalDto.getClubId());
        if (club == null) {
            throw new ClubDefinedException(ResultCodeEnum.CLUB_NOT_EXIST);
        }
        if (!"1".equals(club.getStatus())) {
            throw new ClubDefinedException(ResultCodeEnum.CLUB_STATUS_ERROR);
        }

        // 更新社团状态
        int rows = clubMapper.updateApprovalStatus(approvalDto);
        if (rows <= 0) {
            throw new ClubDefinedException(ResultCodeEnum.OPERATION_FAIL);
        }

    }

    @Override
    public Result<List<Club>> listMyClubs(Integer userId) {
        List<Club> clubs = clubMapper.selectByUserId(userId);
        return Result.build(clubs, 200, "查询成功");
    }

    @Override
    public Map<String, Object> getClubListForStudent(Map<String, Object> params) {
        try {
            log.info("开始查询学生端社团列表，参数: {}", params);

            // 构建查询条件
            Map<String, Object> queryParams = new HashMap<>();
            queryParams.put("name", params.get("name"));
            queryParams.put("category", params.get("category"));
            queryParams.put("sort", params.getOrDefault("sort", "newest"));

            // 分页参数
            int page = (int) params.getOrDefault("page", 1);
            int limit = (int) params.getOrDefault("limit", 10);

            // 开启分页
            PageHelper.startPage(page, limit);

            // 查询社团列表
            List<Map<String, Object>> clubList = clubMapper.selectClubsForStudent(queryParams);

            // 封装分页结果
            PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(clubList);

            // 构建返回结果
            Map<String, Object> result = new HashMap<>();
            result.put("list", clubList);
            result.put("total", pageInfo.getTotal());
            result.put("pageSize", pageInfo.getPageSize());
            result.put("currentPage", pageInfo.getPageNum());

            log.info("查询学生端社团列表成功，共{}条记录", pageInfo.getTotal());
            return result;
        } catch (Exception e) {
            log.error("查询学生端社团列表异常", e);
            throw new RuntimeException("查询社团列表失败", e);
        }
    }

    @Override
    public Map<String, Object> getClubDetailForStudent(Long clubId, Long userId) {
        try {
            log.info("开始查询学生端社团详情，社团ID: {}, 用户ID: {}", clubId, userId);

            // 查询社团基础信息
            Map<String, Object> clubInfo = clubMapper.selectClubDetailForStudent(clubId);
            if (clubInfo == null) {
                throw new ClubDefinedException(ResultCodeEnum.CLUB_NOT_EXIST);
            }

            // 查询负责人信息
            Map<String, Object> founderInfo = clubMapper.selectFounderInfo(clubId);
            clubInfo.put("founderInfo", founderInfo);

            // 查询成员前5名
            List<Map<String, Object>> topMembers = clubMemberMapper.selectTopMembersByClubId(clubId, 5);
            clubInfo.put("topMembers", topMembers);

            // 查询最近活动
            List<Map<String, Object>> recentActivities = activityMapper.selectRecentActivitiesByClubId(clubId, 5);
            clubInfo.put("recentActivities", recentActivities);

            // 如果用户已登录，查询用户在该社团的状态
            if (userId != null) {
                Map<String, Object> userClubStatus = new HashMap<>();

                // 查询是否已加入
                Integer memberCount = clubMemberMapper.countByUserIdAndClubId(userId, clubId);
                userClubStatus.put("isMember", memberCount > 0);

                // 查询是否有申请记录
                Integer applyCount = clubApplyMapper.countByUserIdAndClubId(userId, clubId);
                userClubStatus.put("hasApplied", applyCount > 0);

                clubInfo.put("userClubStatus", userClubStatus);
            }

            log.info("查询学生端社团详情成功");
            return clubInfo;
        } catch (ClubDefinedException e) {
            throw e;
        } catch (Exception e) {
            log.error("查询学生端社团详情异常", e);
            throw new RuntimeException("查询社团详情失败", e);
        }
    }

    @Transactional
    @Override
    public void applyToJoinClub(Long userId, Long clubId, String remark) {
        try {
            log.info("开始处理加入社团申请，用户ID: {}, 社团ID: {}", userId, clubId);

            // 检查社团是否存在且状态为已通过
            Map<String, Object> clubInfo = clubMapper.selectClubDetailForStudent(clubId);
            if (clubInfo == null) {
                throw new ClubDefinedException(ResultCodeEnum.CLUB_NOT_EXIST);
            }
            if (!"2".equals(clubInfo.get("status"))) {
                throw new ClubDefinedException(ResultCodeEnum.CLUB_STATUS_ERROR);
            }

            // 检查用户是否已加入该社团
            Integer memberCount = clubMemberMapper.countByUserIdAndClubId(userId, clubId);
            if (memberCount > 0) {
                throw new ClubDefinedException(ResultCodeEnum.ALREADY_MEMBER);
            }

            // 检查用户是否已有申请记录
            Integer applyCount = clubApplyMapper.countByUserIdAndClubId(userId, clubId);
            if (applyCount > 0) {
                throw new ClubDefinedException(ResultCodeEnum.APPLY_EXISTS);
            }

            // 创建申请记录
            Map<String, Object> applyData = new HashMap<>();
            applyData.put("userId", userId);
            applyData.put("clubId", clubId);
            applyData.put("remark", remark);
            applyData.put("status", "1"); // 1表示待审核
            applyData.put("createdAt", LocalDateTime.now());

            // 保存申请记录
            clubApplyMapper.insertClubApply(applyData);

            log.info("加入社团申请处理成功");
        } catch (ClubDefinedException e) {
            throw e;
        } catch (Exception e) {
            log.error("处理加入社团申请异常", e);
            throw new RuntimeException("申请加入社团失败", e);
        }
    }

    @Override
    public Map<String, Object> getMyClubs(Long userId) {
        try {
            log.info("开始查询用户加入的社团列表，用户ID: {}", userId);

            // 查询用户加入的社团列表
            List<Map<String, Object>> myClubs = clubMemberMapper.selectMyClubs(userId);

            // 构建返回结果
            Map<String, Object> result = new HashMap<>();
            result.put("list", myClubs);
            result.put("total", myClubs.size());

            log.info("查询用户加入的社团列表成功，共{}个社团", myClubs.size());
            return result;
        } catch (Exception e) {
            log.error("查询用户加入的社团列表异常", e);
            throw new RuntimeException("查询我的社团失败", e);
        }
    }

}
