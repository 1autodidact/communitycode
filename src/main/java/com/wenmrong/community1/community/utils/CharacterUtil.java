package com.wenmrong.community1.community.utils;

import com.wenmrong.community1.community.model.Question;
import com.wenmrong.community1.community.model.User;
import com.wenmrong.community1.community.sysenum.SysEnum;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

/**
 * @author wenmingrong@kungeek.com
 * @since
 */
public class CharacterUtil {
    /**
     * 构建通知类型：top:tag
     *
     * @param
     * @return java.lang.String
     * @since
     */
    public static String buildNotificationDestination(String topic, String tag) {
        return String.format("%s:%s", topic, tag);
    }

    @NotNull
    public static String buildNotificationContent(User userProfile, Question question, Integer type) {
        @NotNull String desc = Objects.requireNonNull(buildDescription(type));
        return String.format("%s%s%s", userProfile.getName(),desc, question.getTitle());
    }

    private static String buildDescription(Integer type) {
        Optional<SysEnum.Notification_Type> validType = Arrays.stream(SysEnum.Notification_Type.values()).filter(item -> item.getType().equals(type)).findFirst();
        SysEnum.Notification_Type notification_type = validType.get();
        switch (notification_type) {
            case PUBLISH:
                return "发布了";
            case FOLLOW:
                return "关注";
        }
        return "";
    }
}
