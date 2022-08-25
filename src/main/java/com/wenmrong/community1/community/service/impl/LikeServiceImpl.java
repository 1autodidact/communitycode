package com.wenmrong.community1.community.service.impl;

import com.wenmrong.community1.community.model.UserLike;
import com.wenmrong.community1.community.mapper.UserLikeMapper;
import com.wenmrong.community1.community.service.LikeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 点赞 服务实现类
 * </p>
 *
 * @author autodidact
 * @since 2022-08-23
 */
@Service
public class LikeServiceImpl extends ServiceImpl<UserLikeMapper, UserLike> implements LikeService {

}
