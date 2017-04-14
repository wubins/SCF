package com.scf.module.security.interceptor;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.scf.core.constant.CommonConstants;
import com.scf.core.context.ContextHolder;
import com.scf.core.context.Identity;
import com.scf.core.context.IdentityContext;
import com.scf.core.context.SwitchContext;
import com.scf.module.security.matcher.RequestMatcher;
import com.scf.module.security.meta.Permission;
import com.scf.module.security.meta.support.PermissionMetaProvider;
import com.scf.utils.JacksonObjectMapper;
import com.scf.utils.StringUtilies;
import com.scf.utils.WebUtilies;
import com.scf.web.comp.ace.ResponseMessage;

/**
 * 权限拦截器
 * @author wubin
 * @date 2016年8月3日 上午9:47:49
 * @version V1.1.0
 */
public class AuthorityInterceptor extends HandlerInterceptorAdapter {
    private static final Logger    logger = LoggerFactory.getLogger(AuthorityInterceptor.class);

    private PermissionMetaProvider permissionMetaProvider;

    // 公用请求路径，只需要登录即可访问
    private List<String>           baseUrl;

    @SuppressWarnings("unchecked")
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        IdentityContext identityContext = ContextHolder.getIdentityContext();
        Identity userIdentity = identityContext.getIdentity();
        // 会话中没有实体返回登录页面
        if (userIdentity == null) {
            // 用户没有资源使用权限处理
            logger.warn("会话超时============");
            if (WebUtilies.isAjaxRequest(request)) {
                response.getWriter().write(JacksonObjectMapper.toJsonString(ResponseMessage.error("noLogin", "未登录")));
            } else {
                response.sendRedirect(request.getContextPath());
            }
            return false;
        } else {
            /**
             * 鉴权开关
             */
            if (SwitchContext.isOpen("switch_authorization")) {
                // 对模块权限进行判断.baseUrl放过
                String moduleValue = getCurrentModuleValue(request);
                boolean isBaseUrl = checkBaseUrl(moduleValue);
                if (isBaseUrl) {
                    return true;
                }
                //用户的权限集字符串，逗号隔开，登陆的时候保存user_modules到identity中
                List<String>  moduleList = (List<String>) userIdentity.getData(CommonConstants.IDENTITY_KEY_USER_MODULES);

                String modules =   StringUtilies.listToString(moduleList);

                boolean hasModulePermission = verifier(modules, moduleValue, request);
                if (!hasModulePermission) {
                    if (WebUtilies.isAjaxRequest(request)) {

                        response.getWriter().write(JacksonObjectMapper.toJsonString(ResponseMessage.error("noPermission", "无权限，请联系管理员")));
                        return false;
                    } else {
                        response.sendRedirect(request.getContextPath() + "/errors/noPermission.html");
                        return false;
                    }
                }
            } else {
                return true;
            }

        }
        return true;
    }

    /**
     * 判断是否基本url路径
     *
     */
    public boolean checkBaseUrl(String currentUrl) {
        if (CollectionUtils.isEmpty(baseUrl)) {
            return true;
        }
        if (baseUrl.contains(currentUrl)) {
            return true;
        }
        return false;
    }

    /**
     * 从请求中获取权限路径
     *
     */
    public static String getCurrentModuleValue(HttpServletRequest request) {
        String privilegeValue = request.getRequestURI();
        String contextPath = request.getContextPath();
        if (contextPath != null && contextPath.length() > 0) {
            privilegeValue = privilegeValue.substring(contextPath.length());
        }
        return privilegeValue;
    }

    /**
     * 判断是否有模块权限，所有请求都拦截
     *
     */
    public boolean verifier(String modules, String moduleValue, HttpServletRequest request) {

        boolean hasThisPermission = false;

        /**
         * 获取请求对应的权限code
         */
        String moduleCode = getModuleCode(moduleValue, request);

        /**
         * 没有找到请求url的配置，请提示无权限
         */
        if (StringUtils.isEmpty(moduleCode)) {
            return hasThisPermission;
        }

        if (!StringUtils.isEmpty(modules)) {
            String[] moduleArr = modules.split(",");
            List<String> moduleList = Arrays.asList(moduleArr);
            if (!CollectionUtils.isEmpty(moduleList)) {

                for (String module : moduleList) {
                    if (module.equalsIgnoreCase(moduleCode)) {
                        hasThisPermission = true;
                        break;
                    }
                }
            }

        }

        if (!hasThisPermission) {
            IdentityContext identityContext = ContextHolder.getIdentityContext();
            Identity userIdentity = identityContext.getIdentity();
            logger.warn("[system:{},userid:{}] doesn't have the request permission of  \"{}\"", new Object[]{CommonConstants.CURRENT_SYSTEM,userIdentity.getId(), moduleValue});
        }
        return hasThisPermission;
    }

    /**
     * 从全局配置中获取当前请求的权限key
     *
     * @param moduleValue
     * @return
     */
    public String getModuleCode(String moduleValue, HttpServletRequest request) {
        String moduleCode = null;
        List<Permission> permission = permissionMetaProvider.getPermissionMeta();
        if (CollectionUtils.isEmpty(permission)) {
            return null;
        }
        for (Permission permissionTemp : permission) {
            RequestMatcher requestMatcher = permissionTemp.getRequestMatcher();
            boolean isMatch = requestMatcher.matches(request);
            if (isMatch) {
                moduleCode = permissionTemp.getCode();
                break;
            }

        }

        return moduleCode;
    }

    /**
     * @return 返回 baseUrl
     */
    public List<String> getBaseUrl() {
        return baseUrl;
    }

    /**
     * @param 对baseUrl进行赋值
     */
    public void setBaseUrl(List<String> baseUrl) {
        this.baseUrl = baseUrl;
    }


    /**
     * @return permissionMetaProvider
     */

    public PermissionMetaProvider getPermissionMetaProvider() {
        return permissionMetaProvider;
    }


    /**
     * @param permissionMetaProvider 设置 permissionMetaProvider
     */

    public void setPermissionMetaProvider(PermissionMetaProvider permissionMetaProvider) {
        this.permissionMetaProvider = permissionMetaProvider;
    }



}
