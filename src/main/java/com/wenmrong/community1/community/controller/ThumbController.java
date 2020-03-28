package com.wenmrong.community1.community.controller;

import com.wenmrong.community1.community.enums.NotificationTypeEnum;
import com.wenmrong.community1.community.mapper.CommentMapper;
import com.wenmrong.community1.community.mapper.QuestionMapper;
import com.wenmrong.community1.community.mapper.ThumbMapper;
import com.wenmrong.community1.community.mapper.UserMapper;
import com.wenmrong.community1.community.model.*;
import com.wenmrong.community1.community.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ThumbController {
    @Autowired
    private ThumbMapper thumbMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CommentService commentService;

    @ResponseBody
    @RequestMapping(value = "/thumb/{thumbParentId}/{thumbId}/{questionId}", method = RequestMethod.GET)
    public String thumb(@PathVariable(name = "thumbParentId") Long thumbParentId,
                        @PathVariable(name = "thumbId") Long thumbId,
                        @PathVariable(name = "questionId") Long questionId) {

        ThumbExample example = new ThumbExample();
        example.createCriteria()
                .andThumbIdParentEqualTo(thumbParentId)
                .andThumbIdEqualTo(thumbId);
        long totalCount = thumbMapper.countByExample(example);
        //判断同一个用户对同一个评论是否点赞超过一次
        if (totalCount >= 1) {
            ThumbExample thumbExample = new ThumbExample();
            thumbExample.createCriteria()
                    .andThumbIdParentEqualTo(thumbParentId);
            long count = thumbMapper.countByExample(thumbExample);
            return "" + count;
        }


        Thumb thumb = new Thumb();
        thumb.setThumbId(thumbId);
        thumb.setThumbIdParent(thumbParentId);
        thumbMapper.insert(thumb);

        ThumbExample thumbExample = new ThumbExample();
        thumbExample.createCriteria()
                .andThumbIdParentEqualTo(thumbParentId);
        long count = thumbMapper.countByExample(thumbExample);
        Comment comment = commentMapper.selectByPrimaryKey(thumbParentId);
        //创建点赞通知
        Question question = questionMapper.selectByPrimaryKey(questionId);
        User commentator = userMapper.selectByPrimaryKey(comment.getCommentator());
        commentService.createNotification(comment, comment.getCommentator(), commentator.getName(), question.getTitle(), NotificationTypeEnum.THUMB_COMMENT, question.getId());
        comment.setLikeCount(count);
        commentMapper.updateByPrimaryKey(comment);


        return "" + count;
    }

}
