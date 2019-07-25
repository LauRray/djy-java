package com.djy.manager.serviceImpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.djy.manager.service.SysBillInterface;
import com.djy.res.ResponseEnum;
import com.djy.res.Result;
import com.djy.sql.mapper.SysBillMapper;
import com.djy.sql.pojo.SysBill;

import javax.annotation.Resource;

@Service
public class SysBillService implements SysBillInterface {

    @Resource
    SysBillMapper sysBillMapper;

    @Override
    public Result save(SysBill sysBill) {
        return null;
    }
}
