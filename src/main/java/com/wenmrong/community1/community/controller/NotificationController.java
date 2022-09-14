package com.wenmrong.community1.community.controller;


import com.wenmrong.community1.community.dto.ResultDTO;
import com.wenmrong.community1.community.model.Notification;
import com.wenmrong.community1.community.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author autodidact
 * @since 2022-09-09
 */
@RestController
public class NotificationController {
    @Autowired
    NotificationService notificationService;
    @GetMapping("/notify/getList")
    public ResultDTO getList(@RequestParam(name = "current", defaultValue = "1") Integer currentPage,
                             @RequestParam(name = "size", defaultValue = "3") Integer pageSize,
                             @RequestParam(required = false) String type,
                             @RequestParam(required = false) String isRead) {
        List<Notification> list = notificationService.getList(currentPage, pageSize, type, isRead);
        return ResultDTO.okOf(list);
    }
}
