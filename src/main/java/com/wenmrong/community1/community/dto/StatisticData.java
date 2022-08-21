package com.wenmrong.community1.community.dto;

import lombok.Data;

/**
 * <h3>community</h3>
 * <p></p>
 *
 * @author : Autodidact
 * @date : 2022-08-20 10:03
 **/
@Data
public class StatisticData {
    public Integer commentCount;
    public Integer questionCount;
    public Integer userCount;
    public boolean isFollow;
    public boolean isLike;
    public String level;
    public Integer likeCount;

}
