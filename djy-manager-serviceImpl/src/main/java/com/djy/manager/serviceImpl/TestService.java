package com.djy.manager.serviceImpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.djy.manager.service.TestInterface;

import com.djy.sql.mapper.TestMapper;
import com.djy.sql.pojo.Test;


import javax.annotation.Resource;
import java.util.List;

@Service
public class TestService implements TestInterface {

    @Resource
    TestMapper testMapper;

    @Override
    public List<Test> findAll() {

        return testMapper.selectAll();
    }
}
