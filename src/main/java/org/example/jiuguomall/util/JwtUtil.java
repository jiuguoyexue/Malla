package org.example.jiuguomall.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;
import java.util.List;

public class JwtUtil {

    // ⚠️ 实际项目中放到 yml

    //固定密钥，生产级，重启服务token不失效，密钥越长越好，>32字符
    //private static final String SECRET = "jiuguomall-jwt-secret-key-2025-very-secure!!!";

    private static final long EXPIRATION = 2 * 60 * 60 * 1000; //过期时间 2小时

    //最推荐｜工程级，不要手写密钥
    //缺点：重启服务token失效
    private static final Key KEY =
            Keys.secretKeyFor(SignatureAlgorithm.HS256);

    /**
     * 根据用户名和角色生成 JWT
     */
    public static String generateToken(String username, List<String> roles) {
        return Jwts.builder()
                .setSubject(username)
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(KEY)
                .compact();
    }

    /**
     * 解析 token 并返回 Claims
     */
    private static Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 从 token 获取用户名
     */
    public static String getUsername(String token) {
        Claims claims = getClaims(token);
        return claims.getSubject();
    }

    /**
     * 从 token 获取角色列表
     */
    public static List<String> getRoles(String token) {
        Claims claims = getClaims(token);
        Object roles = claims.get("roles");
        if (roles instanceof List<?>) {
            return ((List<?>) roles).stream()
                    .map(Object::toString)
                    .toList();
        }
        return List.of();
    }

    /**
     * 校验 token 是否有效
     */
    public static boolean validateToken(String token) {
        try {
            Claims claims = getClaims(token);
            return !claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}
