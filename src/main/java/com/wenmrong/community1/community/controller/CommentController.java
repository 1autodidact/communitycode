package com.wenmrong.community1.community.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wenmrong.community1.community.dto.CommentCreateDTO;
import com.wenmrong.community1.community.dto.CommentDTO;
import com.wenmrong.community1.community.dto.ResultDTO;
import com.wenmrong.community1.community.enums.CommentTypeEnum;
import com.wenmrong.community1.community.exception.CustomizeErrorCode;
import com.wenmrong.community1.community.mapper.CommentMapper;
import com.wenmrong.community1.community.model.Comment;
import com.wenmrong.community1.community.model.User;
import com.wenmrong.community1.community.service.CommentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;
    @Resource
    private CommentMapper commentMapper;
    @ResponseBody
    @RequestMapping(value = "/comment", method = RequestMethod.POST)
    public Object post(@RequestBody CommentCreateDTO commentCreateDTO, HttpServletRequest httpServletRequest) {
        User user = (User) httpServletRequest.getSession().getAttribute("user");
        if (user == null) {
            return ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);

        }
        if (commentCreateDTO == null || StringUtils.isBlank(commentCreateDTO.getContent())) {
            return ResultDTO.errorOf(CustomizeErrorCode.COMMENT_IS_EMPTY);
        }

        Comment comment = new Comment();
        comment.setParentId(commentCreateDTO.getParentId());
        comment.setContent(commentCreateDTO.getContent());
        comment.setType(commentCreateDTO.getType());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setGmtModified(System.currentTimeMillis());
        comment.setCommentator(user.getId());
        comment.setLikeCount(0L);
        commentService.insert(comment, user);
        return ResultDTO.okOf();

    }

    @ResponseBody
    @RequestMapping(value = "/comment/{id}", method = RequestMethod.GET)
    public ResultDTO<List<CommentDTO>> comments(@PathVariable(name = "id") Long id) {
        List<CommentDTO> commentDTOS = commentService.listByTargetId(id, CommentTypeEnum.COMMENT);
        return ResultDTO.okOf(commentDTOS);
    }

    @ResponseBody
    @RequestMapping(value = "/getLatestComment", method = RequestMethod.GET)
    public ResultDTO<List<Comment>> getLatestComment() {
        List<Comment> comments = commentMapper.selectList(new QueryWrapper<Comment>().orderByAsc("gmt_create").last("limit 6"));
        return ResultDTO.okOf(comments);
    }
}
