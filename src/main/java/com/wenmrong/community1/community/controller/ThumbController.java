package com.wenmrong.community1.community.controller;

import com.wenmrong.community1.community.mapper.CommentMapper;
import com.wenmrong.community1.community.mapper.ThumbMapper;
import com.wenmrong.community1.community.model.Comment;
import com.wenmrong.community1.community.model.Thumb;
import com.wenmrong.community1.community.model.ThumbExample;
import com.wenmrong.community1.community.service.CommentService;
import com.wenmrong.community1.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

    @ResponseBody
    @RequestMapping(value = "/thumb/{thumbParentId}/{thumbId}", method = RequestMethod.GET)
    public String thumb(@PathVariable(name = "thumbParentId") Long thumbParentId,
                        @PathVariable(name = "thumbId") Long thumbId ){

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
            return ""+count;
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
        comment.setLikeCount(count);
        commentMapper.updateByPrimaryKey(comment);


        return ""+count;
    }

}
