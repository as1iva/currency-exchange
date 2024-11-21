package org.as1iva.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.as1iva.dao.JdbcCurrencyDAO;
import org.as1iva.dao.JdbcExchangeRateDAO;
import org.as1iva.dto.ExchangeRateRequestDTO;
import org.as1iva.dto.ExchangeRateResponseDTO;
import org.as1iva.service.ExchangeRateService;
import org.as1iva.util.ParameterValidator;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/exchangeRates")
public class ExchangeRatesServlet extends HttpServlet {

    ObjectMapper objectMapper = new ObjectMapper();
    ExchangeRateService exchangeRateService = new ExchangeRateService(JdbcExchangeRateDAO.getInstance(), JdbcCurrencyDAO.getInstance());

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String baseCurrencyCode = req.getParameter("baseCurrencyCode").trim();
        String targetCurrencyCode = req.getParameter("targetCurrencyCode").trim();
        String rate = req.getParameter("rate").trim();

        rate = rate.replace(',','.');

        ParameterValidator.checkCode(baseCurrencyCode);
        ParameterValidator.checkCode(targetCurrencyCode);
        ParameterValidator.checkCodePair(baseCurrencyCode, targetCurrencyCode);
        ParameterValidator.checkRate(rate);

        BigDecimal rateValue = new BigDecimal(rate);

        ExchangeRateRequestDTO exchangeRateRequestDTO = new ExchangeRateRequestDTO(baseCurrencyCode, targetCurrencyCode, rateValue);

        ExchangeRateResponseDTO exchangeRateResponseDTO = exchangeRateService.add(exchangeRateRequestDTO);

        String jsonResponse = objectMapper.writeValueAsString(exchangeRateResponseDTO);

        resp.setStatus(HttpServletResponse.SC_CREATED);
        resp.getWriter().write(jsonResponse);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        List<ExchangeRateResponseDTO> exchangeRateResponseDTOS = exchangeRateService.getAll();

        List<String> jsonResponses = new ArrayList<>();

        for (ExchangeRateResponseDTO exchangeRate : exchangeRateResponseDTOS) {
            String jsonResponse = objectMapper.writeValueAsString(exchangeRate);

            jsonResponses.add(jsonResponse);
        }

        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().write(String.valueOf(jsonResponses));
    }
}
