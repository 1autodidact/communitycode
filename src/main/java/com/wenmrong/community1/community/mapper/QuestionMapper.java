package com.wenmrong.community1.community.mapper;

import com.wenmrong.community1.community.model.Question;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Mapper
public interface QuestionMapper {
    @Insert("insert into question (title,description,gmt_create,gmt_modified,creator,tag) values (#{title},#{description},#{gmtCreate},#{gmtModified},#{creator},#{tag})")
    void create(Question question);
    @Select("select * from question limit #{offset},#{size}")
    List<Question> list(@RequestParam(value = "offset") Integer offset,@RequestParam(value = "size") Integer size);
    @Select("select count(1) from question")
    Integer count();

    @Select("select * from question where creator = #{userId} limit #{offset},#{size}")
    List<Question> listByUserId(@RequestParam(value = "userId") Integer userId, @RequestParam(value = "offset")Integer offset, @RequestParam(value = "size")Integer size);
    @Select("select count(1) from question where creator = #{userId}")
    Integer countByUserId(@RequestParam(value = "userId") Integer userId);
    @Select("select * from question where id = #{id}")
    Question getById(Integer id);

}
