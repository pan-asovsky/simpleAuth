package dev.panasovsky.module.auth.components;

import dev.panasovsky.module.auth.model.jwt.JWTAuthentication;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import io.jsonwebtoken.Claims;

import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;


@Component
@RequiredArgsConstructor
public class JWTFilter extends GenericFilterBean {

    private final JWTUtils jwtUtils;
    private final JWTProvider jwtProvider;

    private final static String BEARER = "Bearer ";
    private final static int SUBSTRING_POSITION = 7;
    private final static String AUTHORIZATION = "Authorization";


    @Override
    public void doFilter(final ServletRequest request,
                         final ServletResponse response,
                         final FilterChain filterChain) throws IOException, ServletException {

        final String token = getTokenFromRequest((HttpServletRequest) request);

        // TODO: обработка исключений и выдача JsonResponse на 500
        if (token != null && jwtProvider.validateAccessToken(token)) {
            final Claims claims = jwtProvider.getAccessClaims(token);
            final JWTAuthentication jwtInfoToken = jwtUtils.generate(claims);
            jwtInfoToken.setAuthenticated(true);
            SecurityContextHolder.getContext().setAuthentication(jwtInfoToken);
        }

        filterChain.doFilter(request, response);
    }

    private String getTokenFromRequest(final HttpServletRequest request) {

        final String bearer = request.getHeader(AUTHORIZATION);
        if (StringUtils.hasText(bearer) && bearer.startsWith(BEARER)) {
            return bearer.substring(SUBSTRING_POSITION);
        }

        return null;
    }

}