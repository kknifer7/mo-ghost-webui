package io.knifer.moghostwebui.common.util;

import com.google.common.io.Files;
import io.knifer.moghostwebui.common.constant.ErrorCodes;
import io.knifer.moghostwebui.common.exception.MoException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * JavaEE Servlet相关工具类
 */
@SuppressWarnings("all")
@UtilityClass
@Slf4j
public class ServletUtil {

    /**
     * 响应文件结果
     */
    public static void sendFileResponse(File file, @Nullable String fileName){
        String name = fileName == null || fileName.isBlank() ? file.getName() : fileName;
        HttpServletResponse response = getResponse();

        response.setHeader(
                "Content-Disposition",
                "attachment;fileName=" +
                        new String(name.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1)
        );
        // TODO application/octet-stream 似乎找不到对应的转换器
        response.setContentType("application/octet-stream");
        response.setContentLength((int) file.length());
        response.setCharacterEncoding("UTF-8");
        try {
            Files.copy(file, response.getOutputStream());
        } catch (IOException e) {
            log.error("Send response file failed.", e);
            MoException.throwOut(ErrorCodes.UNKNOWN);
        }
    }

    /**
     * 获取Response
     */
    public static HttpServletResponse getResponse(){
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();

        return requestAttributes == null ? null : requestAttributes.getResponse();
    }

    /**
     * 设置响应码
     */
    public static void setResponseStatus(HttpStatus status){
        HttpServletResponse response = getResponse();

        if (response != null){
            response.setStatus(status.value());
        }
    }

    /**
     * 设置响应头
     */
    public static void addResponseHeader(String name, String value){
        HttpServletResponse response = getResponse();

        if (response != null){
            response.addHeader(name, value);
        }
    }

    /**
     * 设置响应头
     * @param name 响应头名称
     * @param value 响应头内容
     */
    public static void setResponseHeader(String name, String value){
        HttpServletResponse response = getResponse();

        if (response == null){
            return;
        }

        response.setHeader(name, value);
    }

    /**
     * 设置Content-Type
     */
    public static void setResponseContentType(String value){
        HttpServletResponse response = getResponse();

        if (response != null){
            response.setContentType(value);
        }
    }

    /**
     * 获取request
     * @return
     */
    public static HttpServletRequest getRequest(){
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        return requestAttributes==null? null : requestAttributes.getRequest();
    }

    @Nonnull
    public static HttpServletRequest getRequestNotNull(){
        HttpServletRequest request = getRequest();

        if (request == null){
            throw new NullPointerException();
        }

        return request;
    }

    public static String getRequestHeader(String headerName){
        HttpServletRequest request = getRequest();

        if (request == null){
            return null;
        }

        return request.getHeader(headerName);
    }

    /**
     * 获取session
     * @return
     */
    public static HttpSession getSession(){
        return getRequest().getSession(false);
    }

    /**
     * 获取真实路径
     * @return
     */
    public static String getRealRootPath(){
        return getRequest().getServletContext().getRealPath("/");
    }

    /**
     * 获取ip
     * @return
     */
    public static String getIp() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        if(servletRequestAttributes!=null){
            HttpServletRequest request = servletRequestAttributes.getRequest();
            return request.getRemoteAddr();
        }
        return null;
    }

    /**
     * 反向代理的情况下获取用户真实IP
     * @return
     */
    public static String getRemoteIp(){
        HttpServletRequest request = ServletUtil.getRequest();
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * 获取session中的Attribute
     * @param name
     * @return
     */
    public static<T> T getSessionAttribute(String name, Class<T> clazz){
        Object result = getSessionAttribute(name);

        return result == null ? null : (T) result;
    }

    /**
     * 获取session中的Attribute
     * @param name
     * @return
     */
    public static Object getSessionAttribute(String name){
        HttpServletRequest request = getRequest();

        return request == null ? null : request.getSession().getAttribute(name);
    }

    /**
     * 获取session中的Attribute
     * @param name 属性名
     * @param clazz 返回类型
     * @return
     */
    public static<T> T getSessionAttribute(HttpSession session, String name, Class<T> clazz) {
        Object result = session.getAttribute(name);

        return result == null ? null : (T) result;
    }

    /**
     * 设置session的Attribute
     * @param name
     * @param value
     */
    public static void setSessionAttribute(String name, Object value){
        HttpServletRequest request = getRequest();

        if(request != null){
            request.getSession().setAttribute(name, value);
        }
    }
    /**
     * 获取request中的Attribute
     * @param name
     * @return
     */
    public static Object getRequestAttribute(String name){
        HttpServletRequest request = getRequest();
        return request == null?null:request.getAttribute(name);
    }

    /**
     * 设置request的Attribute
     * @param name
     * @param value
     */
    public static void setRequestAttribute(String name,Object value){
        HttpServletRequest request = getRequest();
        if(request!=null){
            request.setAttribute(name, value);
        }
    }
    /**
     * 获取上下文path
     * @return
     */
    public static String getContextPath() {
        return getRequest().getContextPath();
    }
    /**
     * 删除session中的Attribute
     * @param name
     */
    public static void removeSessionAttribute(String name) {
        getRequest().getSession().removeAttribute(name);
    }

}
