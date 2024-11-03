package org.as1iva.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletResponse;
import org.as1iva.util.ErrorHandler;

import java.io.IOException;

@WebFilter("/*")
public class GlobalServletFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (Exception e) {
            ErrorHandler.handle((HttpServletResponse) servletResponse, e);
        }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
