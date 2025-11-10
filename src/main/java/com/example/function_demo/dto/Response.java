package com.example.function_demo.dto;


import com.example.function_demo.enums.ResponseEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
@AllArgsConstructor
public class Response<T> {
    @NotNull
    private int code;
    @NotNull
    private String msg;

    private T data;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object extra;

    public Response(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <T> Response<T> ok(){
        return toResponse(ResponseEnum.SUCCESS.getCode(), ResponseEnum.SUCCESS.getMessage(), null);
    }
    public static <T> Response<T> ok(T data){
        return toResponse(ResponseEnum.SUCCESS.getCode(), ResponseEnum.SUCCESS.getMessage(), data);
    }
    public static <T> Response<T> ok(T data, Object extra){
        return toResponse(ResponseEnum.SUCCESS.getCode(), ResponseEnum.SUCCESS.getMessage(), data, extra);
    }
    public static <T> Response<T> failed(int code, String msg){
        return toResponse(code, formatErrMsg(msg), null);
    }

    public static <T> Response<T> failed(ResponseEnum responseEnum){
        return toResponse(responseEnum.getCode(), formatErrMsg(responseEnum.getMessage()), null);
    }

    public static <T> Response<T> failed(ResponseEnum responseEnum, T data){
        return toResponse(responseEnum.getCode(), formatErrMsg(responseEnum.getMessage()), data);
    }

    public static <T> Response<T> failed(int code, String msg, T data){
        return new Response(code, formatErrMsg(msg), data);
    }

    private static  <T> Response<T> toResponse(int code, String msg, T data){
        return new Response(code, msg, data);
    }

    private static  <T> Response<T> toResponse(int code, String msg, T data, Object extra){
        return new Response(code, msg, data, extra);
    }

    private static String formatErrMsg(String msgBeforeFormat){

        if (StringUtils.equals(msgBeforeFormat,null)){
            return null;
        }
        String msgAfterTrim = msgBeforeFormat.trim();
        if (!msgAfterTrim.isEmpty()){
            char  msgFirstChar = msgAfterTrim.charAt(0);
            String msgAfterFormat;
            //第一字符不是英文
            if (Character.isLetter(msgFirstChar)){msgFirstChar = Character.toUpperCase(msgFirstChar);}
            //常见的结束符号
            String pattern = ".*[ ，,。.？?!！；;：:]$";

            Pattern r = Pattern.compile(pattern);
            Matcher m = r.matcher(msgAfterTrim);
            if (m.matches()){
                msgAfterFormat =(msgFirstChar+msgAfterTrim.substring(1, msgAfterTrim.length() - 1)).trim()+".";
            }else{
                msgAfterFormat =msgFirstChar+msgAfterTrim.substring(1, msgAfterTrim.length())+".";
            }
            return msgAfterFormat;
        }else{
            return msgAfterTrim;
        }
    }

}
