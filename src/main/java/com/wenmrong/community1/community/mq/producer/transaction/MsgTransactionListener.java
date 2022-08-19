package com.wenmrong.community1.community.mq.producer.transaction;

import com.wenmrong.community1.community.mapper.TbOrderMapper;
import com.wenmrong.community1.community.model.NftOrder;
import com.wenmrong.community1.community.model.TbOrder;
import com.wenmrong.community1.community.sysenum.SysEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.Message;

import javax.annotation.Resource;

@RocketMQTransactionListener
@Slf4j
public class MsgTransactionListener implements RocketMQLocalTransactionListener {

    @Autowired
    RedisTemplate redisTemplate;
    @Resource
    TbOrderMapper orderMapper;

    /**
     *  执行业务逻辑
     *
     * @param message
     * @param o
     * @return
     */
    @Override
    public RocketMQLocalTransactionState executeLocalTransaction(Message message, Object o) {
        log.error("事务id {}", message.getHeaders().get(RocketMQHeaders.TRANSACTION_ID));
        TbOrder order = (TbOrder) message.getPayload();
        try {
            orderMapper.insert(order);
            // 设置事务状态
            // 返回事务状态给生产者
            return RocketMQLocalTransactionState.UNKNOWN;
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
        TbOrder order = orderMapper.selectById((String) message.getHeaders().get(RocketMQHeaders.TRANSACTION_ID));
        if (order != null && order.getStatus().equals(SysEnum.Status.PAID.getStatus())) {
            return RocketMQLocalTransactionState.COMMIT;
        }
        return RocketMQLocalTransactionState.UNKNOWN;
    }
}
