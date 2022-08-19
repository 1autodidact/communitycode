package com.wenmrong.community1.community.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@Slf4j
public class MyBatisPlusFillHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("start insert .. ");
        this.setFieldValByName("createTime", LocalDate.now(), metaObject);
        this.setFieldValByName("updateTime", LocalDate.now(), metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("start update .. ");
        this.setFieldValByName("updateTime", LocalDate.now(),metaObject);
    }
}
