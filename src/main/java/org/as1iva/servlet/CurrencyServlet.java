package org.as1iva.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.as1iva.dao.JdbcCurrencyDAO;
import org.as1iva.dto.CurrencyRequestDTO;
import org.as1iva.dto.CurrencyResponseDTO;
import org.as1iva.service.CurrencyService;
import org.as1iva.util.ParameterValidator;

import java.io.IOException;

@WebServlet("/currency/*")
public class CurrencyServlet extends HttpServlet {
    @Override
    public void init() throws ServletException {
        super.init();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        String code = pathInfo.substring(1);

        ParameterValidator.checkCode(code);

        CurrencyRequestDTO currencyRequestDTO = new CurrencyRequestDTO(code, null, null);

        CurrencyService currencyService = new CurrencyService(JdbcCurrencyDAO.getInstance());


        CurrencyResponseDTO currencyResponseDTO = currencyService.getByCode(currencyRequestDTO);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = objectMapper.writeValueAsString(currencyResponseDTO);

        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().write(jsonResponse);
    }

    @Override
    public void destroy() {
        super.destroy();
    }
}
