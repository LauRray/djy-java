package com.djy.manager.serviceImpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.djy.manager.service.SysRoleInterface;
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
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CachePut;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;

/**
 * 角色管理
 */
@Service
public class SysRoleService implements SysRoleInterface {

    @Resource
    SysRoleMapper sysRoleMapper;

    @Resource
    PageResult<SysRole> pageResult;

    @Resource
    SysRoleMenuMapper sysRoleMenuMapper;

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
    @Override
    public Result delete(IdListReq idListReq) {
        sysRoleMapper.deleteByIdList(idListReq.getList());
        return Result.buildResultOfErrorNotData(ResponseEnum.SYS_ROLE_DELETE_SUCCESS);
    }

    /**
     * 新增和修改角色
     * @param sysRole
     * @return
     */
    @Override
    @Transactional
    @CachePut(cacheNames = "djy" , key = "'sys_role::'+#result.data.id")
    public Result save(SysRole sysRole) {
        sysRole.setCreatTime(new Date());
        if (sysRole.getId()==null){
            sysRoleMapper.insertSelective(sysRole);
            //sysRoleMenuMapper.save(sysRole.getList(),sysRole.getId());
        }

        //设置角色权限，不管是新增还是修改都走这一步
        if (!CollectionUtils.isEmpty(sysRole.getList())){
            List<SysMenu> interat = this.interat(sysRole.getList());
            //根据用户的id删除原有的菜单权限
            sysRoleMenuMapper.deleteByRoleId(sysRole.getId());
            //新增新的权限
            sysRoleMenuMapper.save(interat,sysRole.getId());
        }
        return Result.buildResultOfSuccess(ResponseEnum.SYS_ROLE_SAVE_SUCCESS,sysRole);
    }

    /**
     * 递归
     */
    private List<SysMenu> interat(List<SysMenu> list){
        List<SysMenu> listItem=new ArrayList<>();
        for(SysMenu sysMenu:list){
            SysMenu sysMenuData=new SysMenu();
            sysMenuData.setId(sysMenu.getId());
            sysMenuData.setType(sysMenu.getType());
            listItem.add(sysMenuData);
            if (!CollectionUtils.isEmpty(sysMenu.getChildren())){
                List<SysMenu> interat = this.interat(sysMenu.getChildren());
                listItem.addAll(interat);
            }
        }
        return listItem;
    }
}
