package com.wenmrong.community1.community.service;

import com.wenmrong.community1.community.dto.PaginationDTO;
import com.wenmrong.community1.community.mapper.QuestionMapper;
import com.wenmrong.community1.community.mapper.UserMapper;
import com.wenmrong.community1.community.model.Question;
import com.wenmrong.community1.community.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class StarService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private QuestionMapper questionMapper;

    public PaginationDTO list(Long userId, Integer page, Integer size) {
        PaginationDTO<Question> paginationDTO = new PaginationDTO<>();
        User user = userMapper.selectByPrimaryKey(userId);
        String star = user.getStar();
        if (star == null || star.equals("")) {
            paginationDTO.setPagination(0, 0);
            return paginationDTO;
        }
        String[] starIds = star.substring(0, star.length() - 1).split("=");
        Integer totalCount = starIds.length;
        Integer totalPage;


        if (totalCount % size == 0) {
            totalPage = totalCount / size;
        } else {
            totalPage = totalCount / size + 1;
        }

        if (page < 1) {
            page = 1;
        }
        if (page > totalPage) {
            page = totalPage;
        }
        paginationDTO.setPagination(totalPage, page);
        Integer offset = size * (page - 1);


        List starList = Arrays.asList(starIds);
        Collections.reverse(starList);
        // 判断剩余收藏文章的数量
        if (totalCount - offset + 1 >= size) {

            List list = starList.subList(offset, offset + size);
            List<Question> questionList = new ArrayList<>();
            for (Object o : list) {
                long id = Long.parseLong((String) o);
                Question question = questionMapper.selectByPrimaryKey(id);
                questionList.add(question);
            }
            paginationDTO.setData(questionList);
        } else {
            List data = starList.subList(offset, offset + (totalCount - offset));
            List<Question> questionList = new ArrayList<>();
            for (Object o : data) {
                //需要把object转化为String再转化为Long类型
                long id = Long.parseLong((String) o);
                Question question = questionMapper.selectByPrimaryKey(id);
                questionList.add(question);
            }
            paginationDTO.setData(questionList);
        }
        return paginationDTO;
    }
}
