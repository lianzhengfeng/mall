package com.lzf.mall.exception;

import com.lzf.mall.common.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lianzhengfeng
 * @create 2021-01-13-16:26
 * 统一异常处理
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    private static  final Logger logger= LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 系统异常
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Object handlerException(Exception e) {
        logger.error("Default Exception: ",e);
        return R.error(MallExceptionEnum.SYSTEM_ERROR);
    }

    /**
     * 业务异常
     * @param e
     * @return
     */
    @ExceptionHandler(MallException.class)
    @ResponseBody
    public Object handlerMallException(MallException e) {
        logger.error("MallException: ",e);
        return R.error(e.getCode(),e.getMessage());
    }

    /**
     * 参数校验异常
     * @param e
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public R handlerMethodArgumentNotValidException(MethodArgumentNotValidException e){
        logger.error("MethodArgumentNotValidException: ",e);
        return handlerBindingResult(e.getBindingResult());
    }

    private R handlerBindingResult(BindingResult result){
        //把异常处理对外暴露提示
        List<String> list=new ArrayList<>();
        if (result.hasErrors()){
            List<ObjectError> allErrors = result.getAllErrors();
            for (ObjectError allError : allErrors) {
                String messages = allError.getDefaultMessage();
                list.add(messages);
            }
        }
        if (list.size() == 0){
            return R.error(MallExceptionEnum.REQUEST_PARAM_ERROR);
        }
        return R.error(MallExceptionEnum.REQUEST_PARAM_ERROR.getCode(),list.toString());
    }
}
