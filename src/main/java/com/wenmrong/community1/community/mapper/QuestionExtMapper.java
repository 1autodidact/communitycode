package com.wenmrong.community1.community.mapper;

import com.wenmrong.community1.community.dto.QuestionQueryDTO;
import com.wenmrong.community1.community.model.Question;
import com.wenmrong.community1.community.model.QuestionExample;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface QuestionExtMapper {
    int incView(Question record);
    int incCommentCount(Question record);
    List<Question> selectRelated(Question question);

    Integer countBySearch(QuestionQueryDTO questionQueryDTO);

    List<Question> selectBySearch(QuestionQueryDTO questionQueryDTO);
    
}