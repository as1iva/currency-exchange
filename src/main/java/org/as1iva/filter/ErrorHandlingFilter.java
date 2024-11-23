package org.as1iva.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.as1iva.exception.DataExistsException;
import org.as1iva.exception.DataNotFoundException;
import org.as1iva.exception.DatabaseException;
import org.as1iva.exception.InvalidDataException;

import java.io.IOException;
import java.util.Map;

import static jakarta.servlet.http.HttpServletResponse.*;

@WebFilter(value = {
        "/currencies", "/currency/*", "/exchangeRates", "/exchangeRate/*", "/exchange"
})
public class ErrorHandlingFilter extends HttpFilter {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse resp, FilterChain chain) throws IOException, ServletException{
        try {
            super.doFilter(req, resp, chain);
        }
        catch (DatabaseException e) {
            writeErrorResponse(resp, SC_INTERNAL_SERVER_ERROR, e);
        }
        catch (DataExistsException e) {
            writeErrorResponse(resp, SC_CONFLICT, e);
        }
        catch (DataNotFoundException e) {
            writeErrorResponse(resp, SC_NOT_FOUND, e);
        }
        catch (InvalidDataException e) {
            writeErrorResponse(resp, SC_BAD_REQUEST, e);
        }
    }

    private void writeErrorResponse(HttpServletResponse response, int errorCode, RuntimeException e) throws IOException {
        Map<String, String> errorResponse = Map.of("message", e.getMessage());
        String jsonResponse = objectMapper.writeValueAsString(errorResponse);

        response.setStatus(errorCode);
        response.getWriter().write(jsonResponse);
    }
}
