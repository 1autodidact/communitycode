package com.wenmrong.community1.community.schedule;

import com.wenmrong.community1.community.cache.HotTagCache;
import com.wenmrong.community1.community.mapper.QuestionMapper;
import com.wenmrong.community1.community.model.Question;
import com.wenmrong.community1.community.model.QuestionExample;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

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
				String[] tags = question.getTag().split(",");
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