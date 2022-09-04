package com.wenmrong.community1.community.dto;

import com.wenmrong.community1.community.model.User;
import com.wenmrong.community1.community.model.UserLevel;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wenmingrong@kungeek.com
 * @since
 */
@Data
public class UserDto extends User {
    private List<User> followers = new ArrayList<>();

    public List<User> getFollowers() {
        return followers;
    }

    public void setFollowers(List<User> followers) {
        this.followers = followers;
    }
    public String token;

    public UserLevel userLevel;

    public Boolean isFollow;
}
