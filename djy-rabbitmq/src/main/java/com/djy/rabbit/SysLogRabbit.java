package com.djy.rabbit;

import com.djy.sql.mapper.SysLogMapper;
import com.djy.sql.mapper.SysMenuMapper;
import com.djy.sql.pojo.SysLog;
import com.djy.sql.pojo.SysMenu;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

@Component
public class SysLogRabbit {

    @Resource
    SysLogMapper sysLogMapper;

    @Resource
    SysMenuMapper sysMenuMapper;


     @RabbitListener(bindings = @QueueBinding(
             value = @Queue(value = "syslog" ,durable = "true"),//绑定队列
             exchange = @Exchange(//绑定交换机
                     value = "${rabbit.send.log.exchange}",
                     ignoreDeclarationExceptions = "true",
                     type = ExchangeTypes.TOPIC
             ),
             key = {"${rabbit.send.log.routerkey}"}
     ))
    public void sysLog(SysLog sysLog){

         Example example=new Example(SysMenu.class);
         example.createCriteria().andEqualTo("url",sysLog.getUrl());
         List<SysMenu> list = sysMenuMapper.selectByExample(example);
         if (!CollectionUtils.isEmpty(list)){
             sysLog.setUrlDesc(list.get(0).getTitle());
             sysLogMapper.insertSelective(sysLog);
         }

    }
}
