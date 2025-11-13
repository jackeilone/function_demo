package com.example.function_demo.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.security.Key;

public class JwtDemo{
    public static void main(String[] args) {
        // 1. 定义签名密钥（生产环境要放安全位置）
        Key key = Keys.hmacShaKeyFor("mySuperSecretKey1234567890123456".getBytes());

        // 2. 生成 JWT
        String token = Jwts.builder()
                .setSubject("user123") // 用户标识
                .claim("role", "admin") // 自定义字段
                .setIssuedAt(new Date()) // 签发时间
                .setExpiration(new Date(System.currentTimeMillis() + 3600_000)) // 过期时间（1小时）
                .signWith(key, SignatureAlgorithm.HS256) // 使用HS256算法签名
                .compact();

        System.out.println("生成的 JWT：\n" + token);


        // 3. 解析 JWT
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        System.out.println("用户：" + claims.getSubject());
        System.out.println("角色：" + claims.get("role"));
        System.out.println("过期时间：" + claims.getExpiration());
    }
}
