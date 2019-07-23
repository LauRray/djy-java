package com.djy.manager.serviceImpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONObject;
import com.djy.common.CommonUtil;
import com.djy.common.IdWorker;
import com.djy.manager.service.UserInterface;
import com.djy.req.IdListReq;
import com.djy.res.PageResult;
import com.djy.res.ResponseEnum;
import com.djy.res.Result;
import com.djy.sql.mapper.UserMapper;
import com.djy.sql.pojo.SysLog;
import com.djy.sql.pojo.SysUser;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class UserService implements UserInterface {

    @Resource
    UserMapper userMapper;

    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Resource
    PageResult<SysUser> pageResult;

    @Resource
    RedisTemplate redisTemplate;


    /**
     * 用户登录
     * @param sysUser
     * @return
     */
    @Override
    public Result userLogin(SysUser sysUser) {
        SysUser byUsername = userMapper.findByUsername(sysUser.getUsername());

        //用户不存在
        if (byUsername==null){
            return Result.buildResultOfErrorData(ResponseEnum.USER_NOT_EXIT,new SysUser());
        }
        //密码错误
        if (!StringUtils.equals(byUsername.getPassword(),CommonUtil.password(sysUser.getPassword()))){
            return Result.buildResultOfErrorData(ResponseEnum.USER_PASSWORD_ERROR,byUsername);
        }

        //状态1正常，2停用
        if (byUsername.getStatus()==2){
            return Result.buildResultOfErrorData(ResponseEnum.USER_STOP_USEING,byUsername);
        }
        String Authorization = CommonUtil.uuid();
        byUsername.setLastLogin(new Date());
        //默认保存30分钟
        stringRedisTemplate.opsForValue().set("djy::authorization::" + Authorization,JSONObject.toJSONString(byUsername)
                                                ,30,TimeUnit.MINUTES);

        //修改最后登陆时间
        this.save(byUsername);
        byUsername.setAuthorization(Authorization);
        return Result.buildResultOfSuccess(ResponseEnum.USER_LOGIN_SUCCESS,byUsername);
    }

    /**
     * 根据id获取用户信息
     * @param userId
     * @return
     */
    @Override
    @Cacheable(cacheNames = "djy",key = "'sys_user::'+#userId",unless = "#result.code!=200")
    public Result findById(Long userId) {
        log.info("从数据库获取用户信息..................................");
        SysUser sysUser = userMapper.selectByPrimaryKey(userId);
        if (sysUser==null){
            return Result.buildResultOfSuccess(ResponseEnum.USER_NOT_EXIT,null);
        }

        return Result.buildResultOfSuccess(ResponseEnum.SUCCESS, sysUser);
    }

    /**
     * 保存和修改用户信息
     * @param sysUser
     * @return
     */
    @Override
    @CachePut(cacheNames = "djy",key = "'sys_user::'+#sysUser.id",unless = "#result.code!=200")
    public Result save(SysUser sysUser) {
        if (sysUser.getId()==null){
            userMapper.insertSelective(sysUser);
            return Result.buildResultOfErrorNotData(ResponseEnum.USER_SAVE_SUCCESS);
        }else {
            userMapper.updateByPrimaryKeySelective(sysUser);
            return Result.buildResultOfErrorNotData(ResponseEnum.USER_UPDATE_SUCCESS);
        }

    }

    @Override
    public PageResult<SysUser> pageList(int pageNum, int pageSize, String username) {
        PageHelper.startPage(pageNum,pageSize);

        List<SysUser> sysUsers = userMapper.findUserPageList(username);
        PageInfo<SysUser> pageInfo=new PageInfo<>(sysUsers);
        return pageResult.build(ResponseEnum.USER_PAGELIST_SUCCESS,sysUsers,pageInfo.getTotal());
    }

    @Override

    public Result delete(IdListReq idListReq) {
         userMapper.deleteByIdList(idListReq.getList());
        



        return Result.buildResultOfErrorNotData(ResponseEnum.USER_DELETE_SUCCESS);
    }
    @Override
    @CacheEvict(cacheNames = "djy",key = "'sys_user::'+#id",beforeInvocation = false)
    public void deleteRedis(Long id){
        log.info(id+"");
    }

    @Override
    public List<SysUser> findSysUser(SysUser sysUser) {
        return userMapper.select(sysUser);
    }

}
