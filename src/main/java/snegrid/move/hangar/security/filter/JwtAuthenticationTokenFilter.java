package snegrid.move.hangar.security.filter;

import com.alibaba.fastjson.JSON;
import snegrid.move.hangar.base.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import snegrid.move.hangar.security.enums.ReturnStatus;
import snegrid.move.hangar.security.handle.TokenService;
import snegrid.move.hangar.security.model.LoginUser;
import snegrid.move.hangar.security.util.SecurityUtils;
import snegrid.move.hangar.security.util.ServletUtils;
import snegrid.move.hangar.utils.common.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * token过滤器 验证token有效性
 *
 * @author drone
 */
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        LoginUser loginUser = tokenService.getLoginUser(request);
        if (StringUtils.isNotNull(loginUser) && StringUtils.isNull(SecurityUtils.getAuthentication())) {
            //先校验版本，挤下线
            boolean isSuccess = tokenService.checkVersion(loginUser);
            if (!isSuccess) {
                ServletUtils.renderString(response, JSON.toJSONString(AjaxResult.error(ReturnStatus.USER_HAS_LOGGED_IN.getCode(), ReturnStatus.USER_HAS_LOGGED_IN.getMsg())));
                return;
            }
            tokenService.verifyToken(loginUser);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
        chain.doFilter(request, response);
    }
}
