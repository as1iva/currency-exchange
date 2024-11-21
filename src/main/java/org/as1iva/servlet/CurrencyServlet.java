package org.as1iva.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
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

    ObjectMapper objectMapper = new ObjectMapper();
    CurrencyService currencyService = new CurrencyService(JdbcCurrencyDAO.getInstance());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        String code = pathInfo.substring(1);

        ParameterValidator.checkCode(code);

        CurrencyRequestDTO currencyRequestDTO = new CurrencyRequestDTO(code, null, null);

        CurrencyResponseDTO currencyResponseDTO = currencyService.getByCode(currencyRequestDTO);

        resp.setStatus(HttpServletResponse.SC_OK);
        objectMapper.writeValue(resp.getWriter(), currencyResponseDTO);
    }
}
