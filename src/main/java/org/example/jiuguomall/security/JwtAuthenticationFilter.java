package org.example.jiuguomall.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.jiuguomall.util.JwtUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * JWT 认证过滤器
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // 白名单：登录/注册接口不走 JWT 校验
        String path = request.getServletPath();
        if (path.startsWith("/users/login") || path.startsWith("/users/register")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 解析 token
        String token = resolveToken(request);

        if (StringUtils.hasText(token)) {
            try {
                String username = JwtUtil.getUsername(token);
                List<String> roles = JwtUtil.getRoles(token);

                if (username != null) {
                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                            username,
                            null,
                            roles.stream().map(SimpleGrantedAuthority::new).toList()
                    );
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            } catch (Exception e) {
                response.setContentType("application/json;charset=UTF-8");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("{\"code\":401,\"message\":\"Token无效或过期\",\"data\":null}");
                return;
            }
        }

        // 放行请求
        filterChain.doFilter(request, response);
    }

    /**
     * 从请求头 Authorization 中解析 Bearer token
     */
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
