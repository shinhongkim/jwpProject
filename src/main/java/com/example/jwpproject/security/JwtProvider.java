package com.example.jwpproject.security;

import com.example.jwpproject.model.Authority;
import com.example.jwpproject.util.RedisUtil;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Component
public class JwtProvider {

    @Value("${jwt.secret.key}")
    private String salt;

    private Key secretKey;


    @Value("${jwt.access-token-validity-in-seconds}")
    private long accessTokenValidityInSeconds;

    @Value("${jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenValidityInSeconds;


    private final RedisUtil redisUtil;

    //만료시간
    private final long exp = 1000L * 60;
    private final JpaUserDetailsService userDetailsService;

    @PostConstruct
    protected void init(){
        secretKey = Keys.hmacShaKeyFor(salt.getBytes(StandardCharsets.UTF_8));
    }

    //토큰생성
    public String createToken(String account, List<Authority> roles){
        Claims claims = Jwts.claims().setSubject(account);
        claims.put("roles",roles);

        Date now = new Date();

        return Jwts.builder().setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime()+exp))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();

    }

    public String createRefreshToken(String account, List<Authority> roles){
        Claims claims = Jwts.claims().setSubject(account);
        claims.put("roles",roles);

        Date now = new Date();

        return Jwts.builder().setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime()+(refreshTokenValidityInSeconds*1000)))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();

    }



    //권한정보 획득
    //spring Security 인증과정에서 권한확인을 위한 기능
    public Authentication getAuthentication(String token){

        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getAccount(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    //토큰에 담겨있는 유저 account 획득
    public String getAccount(String token){
        return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody().getSubject();
    }


    // Authorization Header를 통해 인증을 한다.
    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("Authorization");
    }

    // 토큰 검증
    public boolean validateToken(String token) {
        try {
            // Bearer 검증
            if (!token.substring(0, "BEARER ".length()).equalsIgnoreCase("BEARER ")) {
                return false;
            } else {
                token = token.split(" ")[1].trim();
            }
            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);

            if(redisUtil.hasKeyBlackList(token)){
                 return false;
            }
            // 만료되었을 시 false
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    /** 토큰에서 Claim 추출**/
    public Claims getClaims(String token){

        try{
            return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
        }catch(ExpiredJwtException e){
            return e.getClaims();
        }

    }

}
