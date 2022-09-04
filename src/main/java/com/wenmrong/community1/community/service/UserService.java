package com.wenmrong.community1.community.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.wenmrong.community1.community.dto.UserDto;
import com.wenmrong.community1.community.exception.CustomizeErrorCode;
import com.wenmrong.community1.community.exception.CustomizeException;
import com.wenmrong.community1.community.mapper.*;
import com.wenmrong.community1.community.model.*;
import com.wenmrong.community1.community.sysenum.SysEnum;
import com.wenmrong.community1.community.utils.BeanPlusUtils;
import com.wenmrong.community1.community.utils.JwtTokenUtil;
import com.wenmrong.community1.community.utils.UserInfoProfile;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.wenmrong.community1.community.constants.CommunityConstants.COMMUNITY_USER_TOKEN;
import static com.wenmrong.community1.community.constants.CommunityConstants.LOGIN_PREFIX;
import static com.wenmrong.community1.community.exception.CustomizeErrorCode.LOGIN_FAILURE;

@Service
public class UserService extends ServiceImpl<UserMapper, User> {
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    JwtTokenUtil jwtTokenUtil;
    @Resource
    private UserLevelMapper userLevelMapper;
    @Resource
    private UserMapper userMapper;
    @Autowired
    private MailService mailService;
    @Autowired
    private RandomCodeService randomCodeService;
    @Resource
    private QuestionMapper questionMapper;
    @Resource
    private UserFollowMapper userFollowMapper;
    @Resource
    private UserLikeMapper userLikeMapper;

    public UserDto login(User user) throws InterruptedException {
        UserDto userDto = this.getLoginInfo(user);
        return userDto;
    }

    private UserDto getLoginInfo(User user) throws InterruptedException {
        if (Boolean.TRUE.equals(redisTemplate.opsForValue().setIfAbsent(LOGIN_PREFIX + user.getName(), user.getName(), 1000 * 10, TimeUnit.MILLISECONDS))) {
            User userInfo = userMapper.selectOne(new QueryWrapper<User>().eq("name", user.getName()));
            if (userInfo == null) {
                throw new CustomizeException(LOGIN_FAILURE);
            }
            String token = jwtTokenUtil.createToken(JSONObject.toJSONString(userInfo));
            redisTemplate.opsForValue().set(COMMUNITY_USER_TOKEN + user.getName(), token, 1000 * 60 * 60 * 2, TimeUnit.MILLISECONDS);

        } else {
            LocalDateTime startTime = LocalDateTime.now();
            do {
                Thread.sleep(1000);
            } while (redisTemplate.opsForValue().get(COMMUNITY_USER_TOKEN + user.getName()) == null && Duration.between(startTime, LocalDateTime.now()).getSeconds() < 5000);
        }
        String userInfoJson = jwtTokenUtil.getUserInfoFromToken((String) redisTemplate.opsForValue().get(COMMUNITY_USER_TOKEN + user.getName()));
        UserDto userDto = JSONObject.parseObject(userInfoJson).toJavaObject(UserDto.class);
        userDto.setToken((String) redisTemplate.opsForValue().get(COMMUNITY_USER_TOKEN + user.getName()));
        UserLevel levelInfo = userLevelMapper.selectOne(new QueryWrapper<UserLevel>().eq("user_id", user.getId()));
        userDto.setUserLevel(levelInfo);
        return userDto;
    }

    public void createOrUpdate(User user) {
        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andAccountIdEqualTo(user.getAccountId());
        List<User> users = userMapper.selectByExample(userExample);
        if (users.size() == 0) {
            //插入
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userMapper.insert(user);
        } else {
            //更新(同一个账号用户可能改了名字和头像)
            User dbUser = users.get(0);
            User updateUser = new User();
            updateUser.setGmtModified(System.currentTimeMillis());
            updateUser.setAvatarUrl(user.getAvatarUrl());
            updateUser.setName(user.getName());
            updateUser.setToken(user.getToken());
            UserExample example = new UserExample();
            example.createCriteria()
                    .andIdEqualTo(dbUser.getId());
            userMapper.updateByExampleSelective(updateUser, example);
        }
    }

    @Transactional
    public UserDto createUser(User user) throws InterruptedException {
        userMapper.insert(user);

        UserLevel userLevel = new UserLevel();
        userLevel.setUserId(user.getId());
        userLevel.setLevel(SysEnum.LEVEL.LV1.getLevel());
        userLevel.setPoints(0);
        userLevelMapper.insert(userLevel);
        return this.getLoginInfo(user);
    }

    public String sendEmail(String email, String character) {
        //创建激活码
        String code = randomCodeService.createActiveCode();
        //主题
        String subject = "来自Autodidact网站的激活邮件";
        //上面的激活码发送到用户注册邮箱
        //  String context = "<a href=\"http://localhost:8887/checkCode?code="+code+"\">激活请点击:"+code+"</a>";
        String context = "<a href=\"\">Please complete in 5 minutes " + character + ":" + code + "</a>";
        //发送激活邮件
        mailService.sendMimeMail(email, subject, context);
        return code;
    }


    public List<UserDto> getHotAuthorsList() {
        List<Question> creator = questionMapper.selectList(new QueryWrapper<Question>());
        Map<Long, List<Question>> creatorGrouping = creator.stream().collect(Collectors.groupingBy(Question::getCreator));
        Map<Long, Integer> scoreGroupingByCreator = creatorGrouping.entrySet().stream().collect(Collectors.toMap(item -> item.getKey(), item -> {
            List<Question> list = item.getValue();
            return (Integer) list.stream().map(question -> question.getViewCount() + question.getCommentCount() * 5 + question.getLikeCount() * 10).mapToInt(value -> value).sum();
        }));
        List<Long> creatorId = scoreGroupingByCreator.entrySet().stream().sorted((entry, entry2) -> entry.getValue()).limit(5).map(item -> item.getKey()).collect(Collectors.toList());
        if (creatorId.size() == 0) {
            return new ArrayList<>();
        }
        List<User> users = userMapper.selectBatchIds(creatorId);
        List<Long> userIds = users.stream().map(User::getId).collect(Collectors.toList());
        List<UserLevel> relatedUserLevel = userLevelMapper.selectList(new QueryWrapper<UserLevel>().in("user_id", userIds));
        List<UserDto> userDtos = users.stream().map(item -> {
            UserDto userDto = new UserDto();
            BeanUtils.copyProperties(item, userDto);
            Optional<UserLevel> validUser = relatedUserLevel.stream().filter(info -> info.getUserId().equals(item.getId())).findFirst();
            validUser.ifPresent(userDto::setUserLevel);
            return userDto;
        }).collect(Collectors.toList());
        return userDtos;
    }


    public UserDto getUserInfo(String id) {
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("id", Long.valueOf(id)));
        return this.buildUserLevelInfo(user);
    }

    public void updateLikeState(Long articleId) {
        User user = UserInfoProfile.getUserProfile();
        UserLike record = userLikeMapper.selectOne(new QueryWrapper<UserLike>()
                .eq("article_id", articleId)
                .eq("like_user", user.getId()));
        if (record != null) {
            record.setState(false);
            userLikeMapper.updateById(record);
        } else {
            UserLike userLike = new UserLike();
            userLike.setArticleId(articleId);
            userLike.setState(true);
            userLike.setLikeUser(user.getId());
            userLikeMapper.insert(userLike);
        }

    }

    public void validateUserInfo(String username) {
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("name", username));
        if (user != null) {
            throw new CustomizeException(CustomizeErrorCode.REGISTER_FAILURE);
        }
    }

    public void logout() {
        User user = UserInfoProfile.getUserProfile();
        redisTemplate.delete(COMMUNITY_USER_TOKEN + user.getName());
    }

    public UserDto buildUserLevelInfo(User user) {
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(user, userDto);
        UserLevel levelInfo = userLevelMapper.selectOne(new QueryWrapper<UserLevel>().eq("user_id", user.getId()));
        userDto.setUserLevel(levelInfo);
        return userDto;
    }

    public UserDto getCurrentUserRights() {
        User userProfile = UserInfoProfile.getUserProfile();
        UserDto userDto = new UserDto();
        if (userProfile == null) {
            return userDto;
        }
        BeanUtils.copyProperties(userProfile, userDto);
        return userDto;
    }

    public Integer getFollowCount(String userId) {
        return userFollowMapper.selectCount(new QueryWrapper<UserFollow>().eq("user_id", Long.valueOf(userId)));
    }


    public List<UserDto> getFollowUsers(Integer currentPage, Integer pageSize, String bigCow, String fan) {
        PageHelper.startPage(currentPage, pageSize, true);
        String column = bigCow != null ? "follow_id" : "user_id";
        String id = bigCow != null ? bigCow : fan;

        List<UserFollow> followUsers = userFollowMapper.selectList(new QueryWrapper<UserFollow>().eq(column, id));
        List<Long> followerIds = followUsers.stream().map(item -> {
            if (bigCow != null) {
                return item.getUserId();
            }
            return item.getFollowId();
        }).collect(Collectors.toList());
        if (followerIds.size() == 0) {
            return new ArrayList<>();
        }
        List<User> users = userMapper.selectList(new QueryWrapper<User>().in("id", followerIds));
        List<UserLevel> userLevelInfos = userLevelMapper.selectList(new QueryWrapper<UserLevel>().in("user_id", followerIds));

        List<UserDto> userDtos = users.stream().map(item -> {
            UserDto userDto = new UserDto();
            BeanUtils.copyProperties(item, userDto);
            Optional<UserLevel> validLevelInfo = userLevelInfos.stream().filter(userInfo -> userInfo.getUserId().equals(userDto.getId())).findFirst();
            validLevelInfo.ifPresent(userLevel -> userDto.setUserLevel(validLevelInfo.get()));
            userDto.setIsFollow(true);
            return userDto;
        }).collect(Collectors.toList());
        return userDtos;
    }

    public void updateFollowState(UserDto toUser) {
        User user = UserInfoProfile.getUserProfile();

        UserFollow record = userFollowMapper.selectOne(new QueryWrapper<UserFollow>().eq("user_id", toUser.getId()).eq("follow_id", user.getId()));
        if (record != null) {
            userFollowMapper.deleteById(record.getId());
        } else {
            UserFollow userFollow = new UserFollow();
            userFollow.setUserId(toUser.getId());
            userFollow.setFollowId(user.getId());
            userFollowMapper.insert(userFollow);
        }
    }
}
