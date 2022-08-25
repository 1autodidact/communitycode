package com.wenmrong.community1.community.utils;

import com.wenmrong.community1.community.model.User;

/**
 * @author wenmingrong@kungeek.com
 * @since
 */
public class UserInfoProfile {
    public static ThreadLocal<User> USER_PROFILE = new ThreadLocal<>();

    public static User getUserProfile() {
        return USER_PROFILE.get();
    }

    public static void setUserProfile(User userProfile) {
        USER_PROFILE.set(userProfile);
    }

    public static void remove() {
        USER_PROFILE.remove();
    }
}
