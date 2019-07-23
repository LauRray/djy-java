package com.djy.config;



import com.djy.res.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.UnexpectedTypeException;

/**
 * @author win 10
 */
@ControllerAdvice
@Slf4j
public class ExceptionHandler {

    //LyException
    @ResponseBody
    @org.springframework.web.bind.annotation.ExceptionHandler(RuntimeException.class)
    private Result handlerExcaption(RuntimeException exception) {
        exception.printStackTrace();

        log.error("异常原因：：：" + exception.getMessage());
        log.error("运行时异常堆栈信息：：：", exception);//打印异常的堆栈信息
        if (exception instanceof LyException){
            LyException lyException=(LyException) exception;
            return Result.buildResultOfError(lyException.getResponseEnum(),"");
        }else if (exception instanceof ValidateException){
            ValidateException validateException=(ValidateException)exception;
            String[] messageArray = validateException.getMessageArray();
            StringBuilder stringBuilder=new StringBuilder();
            for (int i=0;i<messageArray.length;i++){
                stringBuilder.append(messageArray[i]+"、");
            }
            return Result.buildResultOfError(ResponseEnum.ERROR,stringBuilder.toString());
        }

        return Result.buildResultOfError(ResponseEnum.EXCEPTION,"");
    }

   /*@org.springframework.web.bind.annotation.ExceptionHandler
   @ResponseBody
    public Map<String,Object> aa(RuntimeException e){
       Map<String,Object> map=new HashMap<>();
       map.put("msg",e.getMessage());
       return map;
   }*/
}
