package com.fervanz.auth.security.filters;

import com.fervanz.auth.client.models.dao.ClientDao;
import com.fervanz.auth.client.models.entities.Client;
import com.fervanz.auth.security.constants.Routes;
import com.fervanz.auth.security.context.jwt.service.IJWTService;
import com.fervanz.auth.security.models.dto.JWTAuthDetails;
import com.fervanz.auth.shared.exceptions.GlobalException;
import com.fervanz.auth.shared.utils.CustomLogs;
import com.fervanz.auth.shared.utils.IPIdentifierHelper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class JWTFilter extends OncePerRequestFilter {
    private final IJWTService ijwtService;
    private final ClientDao clientDAO;
    private final HandlerExceptionResolver handlerExceptionResolver;
    private static final Logger logger = LoggerFactory.getLogger(JWTFilter.class);


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwtToken = ijwtService.extractTokenFromRequest(request);
        String ipAddress = IPIdentifierHelper.getClientIp(request);
        long startTime = System.currentTimeMillis();

        String path = request.getRequestURI();
        logger.info(CustomLogs.LOG_SEPARATOR);
        logger.info("IP requester [{}] Accessing path: {}", ipAddress, path);

        if (Arrays.asList(Routes.WHITE_LIST).contains(path)) { // if the path is in the whitelist, then skip the validation
            filterChain.doFilter(request, response);
            return;
        }
        try {
            Optional.ofNullable(ijwtService.getUsernameFromToken(jwtToken))
                    .flatMap(clientDAO::findByUsername)
                    .ifPresent(client -> settingConfiguration(client, request, jwtToken));
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            SecurityContextHolder.clearContext();
            logger.error("Authentication error for requester with IP [{}]: {}", ipAddress, e.getMessage(), e);
            handlerExceptionResolver.resolveException(request, response, null, e);
        }

        long endTime = System.currentTimeMillis();
        logger.info("Total filter time: {} ms", (endTime - startTime));

    }

    private void settingConfiguration(Client client, HttpServletRequest request, String jwtToken) {
        request.setAttribute("X-User-Id", client.getId());
        processAuthentication(request, client.toUserDetails(), jwtToken);
    }

    private void processAuthentication(HttpServletRequest request, UserDetails userDetails, String token) {
        Optional.of(token)
                .filter(jwtToken -> {
                    if (ijwtService.isExpired(jwtToken)) {
                        throw new GlobalException("Session expired.");
                    } else {
                        return true;
                    }
                })
                .filter(ijwtService::isTokenAccess)
                // TODO, validate existing session
                .ifPresent(jwtToken -> {
                    settingContext(request, userDetails, jwtToken);
                });
    }

    private void settingContext(HttpServletRequest request, UserDetails userDetails, String token) {
        if (userDetails.getUsername().equals(ijwtService.getUsernameFromToken(token))) {
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(),
                            userDetails.getAuthorities());
            JWTAuthDetails authDetails = new JWTAuthDetails(new WebAuthenticationDetailsSource().buildDetails(request), token);

            usernamePasswordAuthenticationToken.setDetails(authDetails);
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        }
    }
}
