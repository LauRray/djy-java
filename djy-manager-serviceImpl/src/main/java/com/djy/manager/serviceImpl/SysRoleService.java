package com.djy.manager.serviceImpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.djy.common.CommonUtil;
import com.djy.manager.service.SysMenuInterface;
import com.djy.manager.service.SysRoleInterface;
import com.djy.manager.service.SysRoleMenuInterface;
import com.djy.manager.util.RedisUtil;
import com.djy.req.IdListReq;
import com.djy.res.PageResult;
import com.djy.res.ResponseEnum;
import com.djy.res.Result;
import com.djy.sql.mapper.SysRoleMapper;
import com.djy.sql.mapper.SysRoleMenuMapper;
import com.djy.sql.pojo.SysMenu;
import com.djy.sql.pojo.SysRole;
import com.djy.sql.pojo.SysRoleMenu;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 角色管理
 */
@Service
@Slf4j
public class SysRoleService implements SysRoleInterface {

    @Resource
    SysRoleMapper sysRoleMapper;

    @Resource
    PageResult<SysRole> pageResult;

    @Resource
    SysRoleMenuMapper sysRoleMenuMapper;

     @Resource
    SysMenuInterface sysMenuInterface;

     @Resource
    SysRoleMenuInterface sysRoleMenuInterface;

     @Resource
     RedisUtil redisUtil;

    @Resource
    RedisTemplate redisTemplate;
    /**
     * 角色分页
     * @param pageNum
     * @param pageSize
     * @param sysRole
     * @return
     */
    @Override
    public PageResult<SysRole> pageList(Integer pageNum, Integer pageSize,SysRole sysRole) {
        PageHelper.startPage(pageNum,pageSize);
        Example example=new Example(SysRole.class);
        if (!StringUtils.isBlank(sysRole.getLabel())){
            example.createCriteria().andLike("label","%"+sysRole.getLabel()+"%");
        }
        example.orderBy("id").desc();
        List<SysRole> list = sysRoleMapper.selectByExample(example);
        PageInfo<SysRole> pageInfo=new PageInfo<>(list);
        return pageResult.build(ResponseEnum.SYS_ROLE_PAGELIST_SUCCESS,list,pageInfo.getTotal());
    }

    /**
     * 删除
     * @param idListReq
     * @return
     */
    @Caching(
            evict = {
                    /**清楚权限缓存**/
                    @CacheEvict(cacheNames = "djy" , key = "'sys_role_all'"),
            }
    )
    @Override
    @Transactional
    public Result delete(IdListReq idListReq) {
        sysRoleMapper.deleteByIdList(idListReq.getList());
         //删除角色对应的权限
        for(Long id:idListReq.getList()){
            sysRoleMenuMapper.deleteByRoleId(id);
        }
        /**清除用户详情信息因为用户和角色关联得**/
        Set<String> keys = redisTemplate.keys("djy::sys_user::*");
        redisTemplate.delete(keys);
        //清楚用户权限
        redisUtil.deleteKeys("djy::sys_user_menu::*");
        return Result.buildResultOfErrorNotData(ResponseEnum.SYS_ROLE_DELETE_SUCCESS);
    }

    /**
     * 新增和修改角色
     * @param sysRole
     * @return
     */

    @Override
    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(cacheNames = "djy" , key = "'sys_role::'+#result.data.id"),
                    @CacheEvict(cacheNames = "djy" , key = "'sys_role_all'"),

            }
    )

    public Result save(SysRole sysRole) {
        sysRole.setCreatTime(new Date());
        //新增
        if (sysRole.getId()==null){
            //判断角色名是否存在
            SysRole sys=new SysRole();
            sys.setLabel(sysRole.getLabel());
            if (!CollectionUtils.isEmpty(sysRoleMapper.select(sys))){
                return Result.buildResultOfErrorData(ResponseEnum.SYS_ROLE_LABEL_EXIT,new SysRole());
            }
            sysRoleMapper.insertSelective(sysRole);

        }else {
            //判断角色名是否存在
            Example example=new Example(SysRole.class);
            example.createCriteria().andEqualTo("label",sysRole.getLabel())
                    .andNotEqualTo("id",sysRole.getId());
            if (!CollectionUtils.isEmpty(sysRoleMapper.selectByExample(example))){
                return Result.buildResultOfErrorData(ResponseEnum.SYS_ROLE_LABEL_EXIT,new SysRole());
            }
            sysRoleMapper.updateByPrimaryKey(sysRole);
        }

        //设置角色权限，不管是新增还是修改都走这一步
        //根据用户的id删除原有的菜单权限
        sysRoleMenuMapper.deleteByRoleId(sysRole.getId());
        if (!CollectionUtils.isEmpty(sysRole.getList())){

            //新增新的权限
            sysRoleMenuMapper.save(sysRole.getList(),sysRole.getId());
        }


        //清楚用户权限
        redisUtil.deleteKeys("djy::sys_user_menu::*");
        Result result = Result.buildResultOfSuccess(ResponseEnum.SYS_ROLE_SAVE_SUCCESS, sysRole);

        return result;
    }

    @Override
    @Cacheable(cacheNames = "djy",key = "'sys_role::'+#id")
    public Result findById(Long id) {
        SysRole sysRole = sysRoleMapper.selectByPrimaryKey(id);
        //获取权限树
        List<SysMenu> menuList= (List<SysMenu>) sysMenuInterface.getMenuTree().getData();
        //获取用户对应的角色
        List<SysRoleMenu> list= (List<SysRoleMenu>) sysRoleMenuInterface.findByRoleId(id).getData();
        Set<Long> collect = list.stream().map(SysRoleMenu::getMenuId).collect(Collectors.toSet());
        if (!CollectionUtils.isEmpty(list)){
            menuList=this.roleMenuChecked(menuList,collect);
        }
        System.out.println(menuList);
        Map<String,Object> map=new HashMap<>();
        map.put("roleInfo",sysRole);
        map.put("roleMenu",menuList);
        return Result.buildResultOfSuccess(ResponseEnum.SUCCESS,map);
    }

    /**
     * 查询所有
     * @return
     */
    @Override
    @Cacheable(cacheNames = "djy",key = "'sys_role_all'")
    public Result findAll() {
        log.info("从数据库中获取..............................................");
        Example example=new Example(SysRole.class);
        example.createCriteria().andEqualTo("status",1);
        return Result.buildResultOfSuccess(ResponseEnum.SUCCESS,sysRoleMapper.selectByExample(example));
    }



    /**
     * 递归
     */
    public List<SysMenu> roleMenuChecked(List<SysMenu> menuList,Set<Long> collect){
        for (SysMenu sysMenu:menuList){
            if (collect.contains(sysMenu.getId())){
                sysMenu.setChecked(true);

            }else {
                sysMenu.setChecked(false);
            }
            if (!CollectionUtils.isEmpty(sysMenu.getChildren())){
                this.roleMenuChecked(sysMenu.getChildren(),collect);
            }
        }
        return menuList;
    }




}
