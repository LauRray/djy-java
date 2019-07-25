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
import com.djy.sql.mapper.SysUserRoleMapper;
import com.djy.sql.mapper.UserMapper;
import com.djy.sql.pojo.SysLog;
import com.djy.sql.pojo.SysUser;
import com.djy.sql.pojo.SysUserRole;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

import org.springframework.cache.annotation.Caching;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import javax.validation.constraints.Size;
import java.beans.Transient;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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
    SysUserRoleMapper sysUserRoleMapper;

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
         userMapper.updateByPrimaryKey(byUsername);
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
        SysUser sysUser = userMapper.findById(userId);
        if (sysUser==null){
            return Result.buildResultOfSuccess(ResponseEnum.USER_NOT_EXIT,null);
        }
        List<Long> collect = sysUser.getSysUserRoles().stream().map(SysUserRole::getRoleId).collect(Collectors.toList());
        sysUser.setList(collect);
        return Result.buildResultOfSuccess(ResponseEnum.SUCCESS, sysUser);
    }

    /**
     * 保存和修改用户信息
     * @param sysUser
     * @return
     */
    @Override
    //@CachePut(cacheNames = "djy",key = "'sys_user::'+#sysUser.id",unless = "#result.code!=200")
    @Caching(
            evict = {
                    //清楚用户得缓存
                    @CacheEvict(cacheNames = "djy",key = "'sys_user::'+#sysUser.id",beforeInvocation = false),
                    //清楚用户权限的缓存
                    @CacheEvict(cacheNames = "djy",key = "'sys_user_menu::'+#sysUser.id")
            }
    )
    @Transactional
    public Result save(SysUser sysUser) {
        boolean flg=sysUser.getId()==null?true:false;
        Example example=new Example(SysUser.class);

        sysUser.setCreatTime(new Date());
        if (sysUser.getId()==null){
            sysUser.setPassword(CommonUtil.password(sysUser.getPassword()));
            example.createCriteria().andEqualTo("username",sysUser.getUsername());
            if (!CollectionUtils.isEmpty(userMapper.selectByExample(example))){
                return Result.buildResultOfErrorNotData(ResponseEnum.USER_SAVA_ERROR);
            }
            userMapper.insertSelective(sysUser);

        }else {
            example.createCriteria().andEqualTo("username",sysUser.getUsername())
            .andNotEqualTo("id",sysUser.getId());
            if (!CollectionUtils.isEmpty(userMapper.selectByExample(example))){
                return Result.buildResultOfErrorNotData(ResponseEnum.USER_SAVA_ERROR);
            }
            userMapper.updateByPrimaryKeySelective(sysUser);
        }
        /**
         * 设置用户和角色
         */
         sysUserRoleMapper.deleteByUserId(sysUser.getId());
         if (!CollectionUtils.isEmpty(sysUser.getList())){
             sysUserRoleMapper.save(sysUser.getList(),sysUser.getId());
         }
        return Result.buildResultOfSuccess(flg?ResponseEnum.USER_SAVE_SUCCESS:ResponseEnum.USER_UPDATE_SUCCESS,null);
    }

    @Override
    public PageResult<SysUser> pageList(int pageNum, int pageSize, String username) {
        PageHelper.startPage(pageNum,pageSize);

        List<SysUser> sysUsers = userMapper.findUserPageList(username);
        PageInfo<SysUser> pageInfo=new PageInfo<>(sysUsers);
        return pageResult.build(ResponseEnum.USER_PAGELIST_SUCCESS,sysUsers,pageInfo.getTotal());
    }

    @Override

    @Transactional
    public Result delete(IdListReq idListReq) {
         userMapper.deleteByIdList(idListReq.getList());
        /**
         * 删除用户对应的角色
         */
         List<Long> list = idListReq.getList();
        sysUserRoleMapper.deleteByUserIdList(list);
        Set<String> set=new HashSet<>();
        /**
         * 清楚缓存
         */
        for(Long id:list){
            //清楚权限缓存
            redisTemplate.delete("djy::sys_user_menu::"+id);
            set.add("djy::sys_user::"+id);
        }
        redisTemplate.delete(set);
     return Result.buildResultOfErrorNotData(ResponseEnum.USER_DELETE_SUCCESS);
    }

    /**
     * 根据id获取用户
     */
    @Override
    public List<SysUser> findSysUser(SysUser sysUser) {
        return userMapper.select(sysUser);
    }




}
