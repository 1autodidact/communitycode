package com.wenmrong.community1.community.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wenmrong.community1.community.exception.CustomizeErrorCode;
import com.wenmrong.community1.community.exception.CustomizeException;
import com.wenmrong.community1.community.mapper.QuestionMapper;
import com.wenmrong.community1.community.mapper.UserMapper;
import com.wenmrong.community1.community.model.Question;
import com.wenmrong.community1.community.model.User;
import com.wenmrong.community1.community.model.UserExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.wenmrong.community1.community.exception.CustomizeErrorCode.LOGIN_FAILURE;

@Service
public class UserService extends ServiceImpl<UserMapper, User> {
    @Autowired
    RedisTemplate redisTemplate;
    @Resource
    private UserMapper userMapper;

    @Autowired
    private MailService mailService;

    @Autowired
    private RandomCodeService randomCodeService;

    @Resource
    private QuestionMapper questionMapper;

    public String login(User user) {
        User userInfo = userMapper.selectOne(new QueryWrapper<User>().eq("name", user.getName()));
        if (userInfo == null) {
            throw new CustomizeException(LOGIN_FAILURE);
        }
        return String.valueOf(userInfo.getId());
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

    public String createUser(User user) {
        userMapper.insert(user);
        return String.valueOf(user.getId());
    }

    public String sendEmail(String email,String character) {
        //创建激活码
        String code = randomCodeService.createActiveCode();
        //主题
        String subject = "来自Autodidact网站的激活邮件";
        //上面的激活码发送到用户注册邮箱
        //  String context = "<a href=\"http://localhost:8887/checkCode?code="+code+"\">激活请点击:"+code+"</a>";
        String context = "<a href=\"\">Please complete in 5 minutes "+character+":"+code+"</a>";
        //发送激活邮件
        mailService.sendMimeMail (email,subject,context);
        return code;
    }


    public List<User> getHotAuthorsList() {
        List<Question> creator = questionMapper.selectList(new QueryWrapper<Question>());
        Map<Long, List<Question>> creatorGrouping = creator.stream().collect(Collectors.groupingBy(Question::getCreator));
        Map<Long, Integer> scoreGroupingByCreator = creatorGrouping.entrySet().stream().collect(Collectors.toMap(item -> item.getKey(), item -> {
            List<Question> list = item.getValue();
            return list.stream().map(question -> question.getViewCount() + question.getCommentCount() * 5 + question.getLikeCount() * 10)
                    .collect(Collectors.summingInt(value -> value));
        }));
        List<Long> creatorId = scoreGroupingByCreator.entrySet().stream().sorted((entry, entry2) -> entry.getValue()).limit(5).map(item -> item.getKey()).collect(Collectors.toList());
        if (creatorId.size() == 0) {
            return new ArrayList<>();
        }
        return userMapper.selectBatchIds(creatorId);
    }


    public User getUserInfo(String id) {
        User user = userMapper.selectByPrimaryKey(Long.valueOf(id));
        return user;
    }

    public void updateLikeState(String articleId) {

    }

    public void validateUserInfo(String username) {
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("name", username));
        if (user != null) {
            throw new CustomizeException(CustomizeErrorCode.REGISTER_FAILURE);
        }
    }
}
