package com.wenmrong.community1.community.mq.producer.transaction;

import com.wenmrong.community1.community.controller.MqMessageController;
import com.wenmrong.community1.community.mapper.NftOrderMapper;
import com.wenmrong.community1.community.model.NftOrder;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.Message;

import javax.annotation.Resource;

@RocketMQTransactionListener
public class TransactionListenerImpl implements RocketMQLocalTransactionListener {
    private static final Logger log = LoggerFactory.getLogger(TransactionListenerImpl.class);

    @Autowired
    RedisTemplate redisTemplate;
    @Resource
    NftOrderMapper orderMapper;

    /**
     *  执行业务逻辑
     *
     * @param message
     * @param o
     * @return
     */
    @Override
    public RocketMQLocalTransactionState executeLocalTransaction(Message message, Object o) {
        log.error("事务id {}",(String)message.getHeaders().get(RocketMQHeaders.TRANSACTION_ID));

        NftOrder NftOrder = new NftOrder();
        NftOrder.setGmtCreate(System.currentTimeMillis());
        NftOrder.setGmtModified(NftOrder.getGmtCreate());
        String transId = (String)message.getHeaders().get(RocketMQHeaders.TRANSACTION_ID);
        NftOrder.setId(String.valueOf(transId));
        try {
            orderMapper.insert(NftOrder);
            // 设置事务状态
            // 返回事务状态给生产者
            return RocketMQLocalTransactionState.COMMIT;
        } catch (Exception e) {
            log.error("订单创建失败");
        }
        return RocketMQLocalTransactionState.ROLLBACK;
    }

    /**
     * 回查
     *
     * @param message
     * @return
     */
    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message message) {
        log.error("事务id {}",(String)message.getHeaders().get(RocketMQHeaders.TRANSACTION_ID));
        NftOrder nftOrder = orderMapper.selectById((String)message.getHeaders().get(RocketMQHeaders.TRANSACTION_ID));
        if (nftOrder != null) {
            return RocketMQLocalTransactionState.COMMIT;
        }
        return RocketMQLocalTransactionState.ROLLBACK;
    }
}
