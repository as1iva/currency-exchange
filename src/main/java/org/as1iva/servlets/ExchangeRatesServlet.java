package org.as1iva.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.as1iva.dao.CurrencyDAO;
import org.as1iva.dao.ExchangeRateDAO;
import org.as1iva.dto.ExchangeRateRequestDTO;
import org.as1iva.dto.ExchangeRateResponseDTO;
import org.as1iva.services.ExchangeRateService;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/exchangeRates")
public class ExchangeRatesServlet extends HttpServlet {
    @Override
    public void init() throws ServletException {
        super.init();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int baseCurrencyId = Integer.parseInt(req.getParameter("baseCurrencyId"));
        int targetCurrencyId = Integer.parseInt(req.getParameter("targetCurrencyId"));
        BigDecimal rate = new BigDecimal(req.getParameter("rate"));

        ExchangeRateRequestDTO exchangeRateRequestDTO = new ExchangeRateRequestDTO(baseCurrencyId, targetCurrencyId, rate);

        ExchangeRateService exchangeRateService = new ExchangeRateService(ExchangeRateDAO.getInstance(), CurrencyDAO.getInstance());

        try {
            ExchangeRateResponseDTO exchangeRateResponseDTO = exchangeRateService.add(exchangeRateRequestDTO);

            ObjectMapper objectMapper = new ObjectMapper();
            String jsonResponse = objectMapper.writeValueAsString(exchangeRateResponseDTO);

            resp.setContentType("application/json");
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(jsonResponse);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ExchangeRateService exchangeRateService = new ExchangeRateService(ExchangeRateDAO.getInstance(), CurrencyDAO.getInstance());

        try {
            List<ExchangeRateResponseDTO> exchangeRateResponseDTOS = exchangeRateService.getAll();

            List<String> jsonResponses = new ArrayList<>();

            ObjectMapper objectMapper = new ObjectMapper();

            for (ExchangeRateResponseDTO exchangeRate : exchangeRateResponseDTOS) {
                String jsonResponse = objectMapper.writeValueAsString(exchangeRate);

                jsonResponses.add(jsonResponse);
            }

            resp.setContentType("application/json");
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(String.valueOf(jsonResponses));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void destroy() {
        super.destroy();
    }
}
