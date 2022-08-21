package com.wenmrong.community1.community.service.impl;

import com.wenmrong.community1.community.model.Label;
import com.wenmrong.community1.community.mapper.LabelMapper;
import com.wenmrong.community1.community.service.LabelService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 标签 服务实现类
 * </p>
 *
 * @author autodidact
 * @since 2022-08-20
 */
@Service
public class LabelServiceImpl extends ServiceImpl<LabelMapper, Label> implements LabelService {
    @Resource
    LabelMapper labelMapper;
    public List<Label> getLabels() {
        return labelMapper.selectList(null);
    }
}
