package com.example.horse_racing.Filter;

import com.auth0.jwk.Jwk;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.UrlJwkProvider;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import jakarta.servlet.FilterConfig;
import javax.servlet.Filter;
import javax.servlet.ServletException;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URL;
import java.security.interfaces.RSAPublicKey;
import java.util.List;

public class AuthFilter implements Filter {

    private final String issuer = "https://dev-dxdypdfdh3k26fq0.us.auth0.com/";
    private final String jwksUrl = issuer + ".well-known/jwks.json";

    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpReq = (HttpServletRequest) request;
        HttpServletResponse httpResp = (HttpServletResponse) response;

        if ("OPTIONS".equalsIgnoreCase(httpReq.getMethod())) {
            chain.doFilter(request, response);
            return;
        }

        String authHeader = httpReq.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            httpResp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing or invalid Authorization header");
            return;
        }

        String token = authHeader.substring("Bearer ".length());
        try {
            JwkProvider provider = new UrlJwkProvider(new URL(jwksUrl));
            DecodedJWT decodedJWT = JWT.decode(token);
            String kid = decodedJWT.getKeyId();
            Jwk jwk = provider.get(kid);
            Algorithm algorithm = Algorithm.RSA256((RSAPublicKey) jwk.getPublicKey(), null);

            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(issuer)
                    .build();
            DecodedJWT jwt = verifier.verify(token);

            List<String> roles = jwt.getClaim("https://myapp.example.com/roles").asList(String.class);
            String role = (roles != null && !roles.isEmpty()) ? roles.get(0) : null;

            httpReq.setAttribute("role", role);


            String path = httpReq.getRequestURI();
            if (path.startsWith("/ahistory") || path.startsWith("/abets")) {
                if (!"admin".equalsIgnoreCase(role)) {
                    httpResp.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied");
                    return;
                }
            }

            chain.doFilter(request, response);
        } catch (Exception e) {
            httpResp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token: " + e.getMessage());
        }
    }

    @Override
    public void destroy() {
    }
}
