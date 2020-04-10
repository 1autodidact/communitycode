package com.wenmrong.community1.community.service;

import com.wenmrong.community1.community.mapper.QuestionMapper;
import com.wenmrong.community1.community.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
public class QuestionHistoryService {
    @Autowired
    private QuestionMapper questionMapper;
    public String createHistory(String id, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String history = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("history")) {
                    history = cookie.getValue();
                }
            }
        }

        if (history == null) {
            return id;
        }
        String[] strings = history.split("\\_");
        List<String> list = Arrays.asList(strings);
        LinkedList<String> linkedList = new LinkedList<>(list);
        if (linkedList.contains(id)) {
            linkedList.remove(id);
            linkedList.addFirst(id);
        } else {
            if (linkedList.size() >= 5) {
                linkedList.removeLast();
                linkedList.addFirst(id);
            } else {
                linkedList.addFirst(id);
            }
        }
        StringBuilder stringBuffer = new StringBuilder();
        for (String s : linkedList) {
            stringBuffer.append(s).append("_");

        }
        return stringBuffer.deleteCharAt(stringBuffer.length() - 1).toString();
    }


    public ArrayList<Question> showHistory(String history) {
        String[] ids = history.split("\\_");
        ArrayList<Question> questions = new ArrayList<>();
        for (String id : ids) {
            Question question = questionMapper.selectByPrimaryKey(Long.parseLong(id));
            questions.add(question);
        }
        return questions;
    }
}
