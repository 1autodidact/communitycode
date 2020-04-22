package com.wenmrong.community1.community.schedule;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.wenmrong.community1.community.cache.HotTagCache;
import com.wenmrong.community1.community.cache.TagCache;
import com.wenmrong.community1.community.mapper.CommentMapper;
import com.wenmrong.community1.community.mapper.QuestionMapper;
import com.wenmrong.community1.community.mapper.UserMapper;
import com.wenmrong.community1.community.model.Question;
import com.wenmrong.community1.community.model.QuestionExample;
import com.wenmrong.community1.community.model.UserExample;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.RowBounds;
import org.h2.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ScheduledTasks {
 	@Autowired
	private QuestionMapper questionMapper;
	@Autowired
	private HotTagCache hotTagCache;
	@Scheduled(fixedRate = 1000 * 60 * 60 * 3)
	public void hotTagSchedule() {
		log.info("The time is now {}", new Date());
		int offset = 0;
		int limit = 20;
		List<Question> questions = new ArrayList<Question>();
		HashMap<String, Integer> priorities = new HashMap<>();
		while (offset == 0 || questions.size() == limit) {
			questions = questionMapper.selectByExampleWithRowbounds(new QuestionExample(),new RowBounds(offset,limit));
			for (Question question : questions) {
				String[] tags = StringUtils.arraySplit(question.getTag(), ',', true);
				for (String tag : tags) {
					Integer priority = priorities.get(tag);
					if (priority != null){
						priority = 5 + priority + 2*question.getCommentCount() + question.getViewCount();
					}else {
						priority = 5 + 2*question.getCommentCount() + question.getViewCount();
					}
					priorities.put(tag,priority);
				}
			}
			offset += limit;
		}
		hotTagCache.sortTags(priorities);
	}
}