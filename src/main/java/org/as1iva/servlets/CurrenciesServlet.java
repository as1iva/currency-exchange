package org.as1iva.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.as1iva.dao.JdbcCurrencyDAO;
import org.as1iva.dto.CurrencyRequestDTO;
import org.as1iva.dto.CurrencyResponseDTO;
import org.as1iva.services.CurrencyService;
import org.as1iva.util.ParameterValidator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/currencies")
public class CurrenciesServlet extends HttpServlet {
    @Override
    public void init() throws ServletException {
        super.init();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String code = req.getParameter("code");
        String name = req.getParameter("name");
        String sign = req.getParameter("sign");

        ParameterValidator.checkCode(code);
        ParameterValidator.checkName(name);
        ParameterValidator.checkSign(sign);

        CurrencyRequestDTO currencyRequestDTO = new CurrencyRequestDTO(code, name, sign);

        CurrencyService currencyService = new CurrencyService(JdbcCurrencyDAO.getInstance());

        CurrencyResponseDTO currencyResponseDTO = currencyService.add(currencyRequestDTO);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = objectMapper.writeValueAsString(currencyResponseDTO);

        resp.setStatus(HttpServletResponse.SC_CREATED);
        resp.getWriter().write(jsonResponse);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        CurrencyService currencyService = new CurrencyService(JdbcCurrencyDAO.getInstance());

        List<CurrencyResponseDTO> currencyResponseDTOS = currencyService.getAll();

        List<String> jsonResponses = new ArrayList<>();

        ObjectMapper objectMapper = new ObjectMapper();

        for (CurrencyResponseDTO currency : currencyResponseDTOS) {
            String jsonResponse = objectMapper.writeValueAsString(currency);

            jsonResponses.add(jsonResponse);
        }

        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().write(String.valueOf(jsonResponses));
    }

    @Override
    public void destroy() {
        super.destroy();
    }
}
