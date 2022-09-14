package com.wenmrong.community1.community.service;

import com.wenmrong.community1.community.model.Notification;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author autodidact
 * @since 2022-09-09
 */
public interface NotificationService extends IService<Notification> {

    void publish(Notification notification);

    void follow(Notification notification);

    List<Notification> getList(Integer currentPage, Integer pageSize, String type, String isRead);
}
