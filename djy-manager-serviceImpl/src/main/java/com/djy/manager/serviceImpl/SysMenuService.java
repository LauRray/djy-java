package com.djy.manager.serviceImpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.djy.manager.service.SysMenuInterface;
import com.djy.res.ResponseEnum;
import com.djy.res.Result;
import com.djy.sql.mapper.SysMenuMapper;
import com.djy.sql.mapper.SysRoleMenuMapper;
import com.djy.sql.mapper.SysUserRoleMapper;
import com.djy.sql.pojo.SysMenu;
import com.djy.sql.pojo.SysRoleMenu;
import com.djy.sql.pojo.SysUserRole;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SysMenuService implements SysMenuInterface {

    @Resource
    SysMenuMapper sysMenuMapper;

    @Resource
    SysUserRoleMapper sysUserRoleMapper;

    @Resource
    SysRoleMenuMapper sysRoleMenuMapper;

    @Override
    @Cacheable(cacheNames = "djy",key = "'sys_menu::tree'")
    public Result getMenuTree() {
        log.info("从数据库中获取.............................................");
        List<SysMenu> menuTreeList = sysMenuMapper.getMenuTree();
        return Result.buildResultOfSuccess(ResponseEnum.SYS_MENU_TREE_SUCCESS,menuTreeList);
    }




    /**
     * 根据用户得id获取用户所属权限
     */
    @Cacheable(cacheNames = "djy",key = "'sys_user_menu::'+#userId",unless = "#result.size()==0")
    @Override
    public List<SysMenu> findMenuByUserId(Long userId){
        /**
         * 获取用户得角色
         */
        Example example=new Example(SysUserRole.class);
        example.createCriteria().andEqualTo("userId",userId);
        List<SysUserRole> sysUserRoles = sysUserRoleMapper.selectByExample(example);
        /**
         * 用户没有任何权限
         */
        if (CollectionUtils.isEmpty(sysUserRoles)){
            return new ArrayList<SysMenu>();
        }
        /**
         * 获取角色集合
         */
        List<Long> collect = sysUserRoles.stream().map(SysUserRole::getRoleId).collect(Collectors.toList());
        /**
         * 根据角色集合获取角色所对应得菜单权限
         */
        Example exa=new Example(SysRoleMenu.class);
        exa.createCriteria().andIn("roleId", collect);
        List<SysRoleMenu> list = sysRoleMenuMapper.selectByExample(exa);
        if (CollectionUtils.isEmpty(list)){
            return new ArrayList<SysMenu>();
        }

        /**
         * 获取菜单权限
         */
        Set<Long> set = list.stream().map(SysRoleMenu::getMenuId).collect(Collectors.toSet());
        List<SysMenu> menuList = sysMenuMapper.selectByIdList(new ArrayList<>(set));
        return menuList;
    }
}
