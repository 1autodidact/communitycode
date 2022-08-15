package com.wenmrong.community1.community;

import com.wenmrong.community1.community.algorithm.SnowFlake;
import com.wenmrong.community1.community.mapper.NftOrderMapper;
import com.wenmrong.community1.community.model.NftOrder;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * <h3>community</h3>
 * <p></p>
 *
 * @author : Autodidact
 * @date : 2022-08-14 14:37
 **/
@SpringBootTest
public class TestMyBatisPlus {
    static SnowFlake snowFlake = new SnowFlake(2,3);
    @Resource
    NftOrderMapper nftOrderMapper;
    @Test
    public void testInsert() {
        long id = snowFlake.nextId();
        NftOrder nftOrder = new NftOrder();
        nftOrder.setAddressId("北京");
        nftOrder.setGmtCreate(System.currentTimeMillis());
        nftOrder.setGmtModified(System.currentTimeMillis());
        nftOrder.setId(String.valueOf(id));
        nftOrderMapper.insert(nftOrder);
        System.out.println("aa");
    }
}
