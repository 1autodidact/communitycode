package com.wenmrong.community1.community.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wenmrong.community1.community.model.Label;
import com.wenmrong.community1.community.model.TbOrder;

import java.util.List;

/**
 * <h3>community</h3>
 * <p></p>
 *
 * @author : Autodidact
 * @date : 2022-08-20 15:00
 **/
public interface  LabelService extends IService<Label> {
    List<Label> getLabels();
}
