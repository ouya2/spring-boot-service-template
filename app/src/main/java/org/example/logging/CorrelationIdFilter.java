package org.example.logging;

import java.io.IOException;
import java.util.UUID;

import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CorrelationIdFilter extends HttpFilter {

    private static final String HEADER = "X-Correlation-Id";
    private static final String MDC_CORRELATTION_ID_KEY= "correlationId";

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String correlationId = request.getHeader(HEADER);
        if (correlationId == null || correlationId.isEmpty()) {
            correlationId = UUID.randomUUID().toString();
        } else {
            correlationId = correlationId.trim();
        }
        
        MDC.put("correlationId", correlationId);
        response.setHeader(MDC_CORRELATTION_ID_KEY, correlationId);
        try {
            filterChain.doFilter(request, response);
        } finally {
            MDC.remove("MDC_CORRELATTION_ID_KEY");
        }
    }


}
