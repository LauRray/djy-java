package com.djy.manager.serviceImpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.djy.common.IdWorker;
import com.djy.manager.service.LogInterface;
import com.djy.manager.service.UserInterface;
import com.djy.res.PageResult;
import com.djy.res.ResponseEnum;
import com.djy.res.Result;
import com.djy.sql.mapper.SysLogMapper;
import com.djy.sql.pojo.SysLog;
import com.djy.sql.pojo.SysUser;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class LogService implements LogInterface {

    @Resource
    SysLogMapper sysLogMapper;

    @Resource
    UserInterface userInterface;

    @Resource
    PageResult<SysLog> pageResult;

    /**
     * 日志保存
     * @param sysLog
     * @return
     */
    @Override
    public Result save(SysLog sysLog) {
        return null;
    }

    /**
     * 日志分页查询
     *
     * @return
     */
    @Override
    public PageResult<SysLog> pageList(int pageNum,int pageSize,SysLog sysLog) {

        if (StringUtils.isNotBlank(sysLog.getUsername())){
            SysUser sysUser=new SysUser();
            sysUser.setUsername(sysLog.getUsername());
            List<SysUser> sysUserList = userInterface.findSysUser(sysUser);
            if (!CollectionUtils.isEmpty(sysUserList)){
                sysLog.setUserId(sysUserList.get(0).getId());
            }else {
                return pageResult.build(ResponseEnum.SYS_LOG_PAGELIST_SUCCESS,new ArrayList<SysLog>(),0L);
            }
        }
        PageHelper.startPage(pageNum,pageSize);
        List<SysLog> sysLogList = sysLogMapper.pageList(sysLog.getUserId(), sysLog.getStatus(), sysLog.getType());
        PageInfo<SysLog> pageInfo=new PageInfo<SysLog>(sysLogList);
        return pageResult.build(ResponseEnum.SYS_LOG_PAGELIST_SUCCESS,sysLogList,pageInfo.getTotal());
    }
}
