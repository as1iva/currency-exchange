package org.as1iva.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletResponse;
import org.as1iva.util.ErrorHandler;

import java.io.IOException;

@WebFilter(value = {
        "/currencies", "/currency/*", "/exchangeRates", "/exchangeRate/*", "/exchange"
})
public class GlobalServletFilter extends HttpFilter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException {
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
        httpResponse.setHeader("Access-Control-Allow-Origin", "*");
        httpResponse.setHeader("Access-Control-Allow-Methods", "GET, POST, PATCH, PUT, DELETE, OPTIONS");
        servletResponse.setContentType("application/json");

        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (Exception e) {
            ErrorHandler.handle((HttpServletResponse) servletResponse, e);
        }
    }
}
