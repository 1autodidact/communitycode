package com.wenmrong.community1.community.controller;


import com.wenmrong.community1.community.dto.ResultDTO;
import com.wenmrong.community1.community.service.TbOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author autodidact
 * @since 2022-08-17
 */
@Controller
@RequestMapping("/tb-order")
public class TbOrderController {
    private static final Logger log = LoggerFactory.getLogger(TbOrderController.class);

    @Autowired
    TbOrderService tbOrderService;

    @RequestMapping("/placeOrder")
    public ResultDTO placeOrder() {
        tbOrderService.placeOrder();
        return ResultDTO.okOf();
    }

}
