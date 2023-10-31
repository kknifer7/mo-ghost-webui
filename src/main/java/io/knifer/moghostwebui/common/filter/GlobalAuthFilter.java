package io.knifer.moghostwebui.common.filter;

import io.knifer.moghostwebui.common.constant.ErrorCodes;
import io.knifer.moghostwebui.common.constant.SecurityConstants;
import io.knifer.moghostwebui.common.constant.UtilConstants;
import io.knifer.moghostwebui.common.exception.MoException;
import io.knifer.moghostwebui.common.tool.security.PatternMatcher;
import io.knifer.moghostwebui.common.tool.security.impl.CommonPathMatcher;
import io.knifer.moghostwebui.common.util.ServletUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 全局认证过滤器
 *
 * @author Knifer
 * @version 1.0.0
 */
public class GlobalAuthFilter implements Filter {

    private Set<String> excludedUris;

    private final PatternMatcher patternMatcher = CommonPathMatcher.getInstance();

    @Override
    public void init(FilterConfig filterConfig) {
        String param = filterConfig.getInitParameter(SecurityConstants.GLOBAL_AUTH_FILTER_EXCLUDE_URIS_INIT_PARAM_NAME);

        excludedUris = StringUtils.isBlank(param) ?
                Set.of() : UtilConstants.COMMA_SPLITTER.splitToStream(param).collect(Collectors.toSet());
    }
    @Override
    @SuppressWarnings("all")
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req;
        HttpServletResponse resp;
        HttpSession session;
        String uri;

        if (!(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse)){
            MoException.throwOut(ErrorCodes.UNKNOWN, "Servlet can't cast to HttpServlet, something wrong");
        }
        req = (HttpServletRequest) request;
        uri = req.getRequestURI();
        if (excludedUris.stream().anyMatch(pattern -> patternMatcher.matches(pattern, uri))){
            chain.doFilter(request, response);
        }else{
            resp = (HttpServletResponse) response;
            session = req.getSession(false);
            if (session == null || session.getAttribute(SecurityConstants.LOGIN_USER_SESSION_KEY) == null){
                ServletUtil.sendResponse(HttpStatus.FORBIDDEN);
            }else{
                chain.doFilter(request, response);
            }
        }
    }
}
