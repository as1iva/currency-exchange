package org.as1iva.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;

public class ErrorHandler {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private ErrorHandler() {
    }

    public static void handle(HttpServletResponse httpServletResponse, Exception e) throws IOException {
        Map<String, String> errorResponse = Map.of("message", e.getMessage());
        String jsonResponse = objectMapper.writeValueAsString(errorResponse);

        httpServletResponse.setStatus(getStatus(e));
        httpServletResponse.getWriter().write(jsonResponse);
    }

    private static int getStatus(Exception e) {
        return switch (e.getClass().getSimpleName()) {
            case "DatabaseException" -> 500;
            case "DataExistsException" -> 409;
            case "DataNotFoundException" -> 404;
            case "InvalidDataException" -> 400;
            default -> 500;
        };
    }
}
