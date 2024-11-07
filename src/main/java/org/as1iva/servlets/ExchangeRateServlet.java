package org.as1iva.servlets;

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
import org.as1iva.services.ExchangeRateService;
import org.as1iva.util.ParameterValidator;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/exchangeRate/*")
public class ExchangeRateServlet extends HttpServlet {
    @Override
    public void init() throws ServletException {
        super.init();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if ("PATCH".equalsIgnoreCase(req.getMethod())) {
            doPatch(req, resp);
        } else {
            super.service(req, resp);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        String codes = pathInfo.substring(1);

        String baseCurrencyCode = codes.substring(0, 3);
        String targetCurrencyCode = codes.substring(3, 6);

        ParameterValidator.checkCodePair(baseCurrencyCode, targetCurrencyCode);


        ExchangeRateRequestDTO exchangeRateRequestDTO = new ExchangeRateRequestDTO(baseCurrencyCode, targetCurrencyCode);

        ExchangeRateService exchangeRateService = new ExchangeRateService(JdbcExchangeRateDAO.getInstance(), JdbcCurrencyDAO.getInstance());


        ExchangeRateResponseDTO exchangeRateResponseDTO = exchangeRateService.getByCode(exchangeRateRequestDTO);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = objectMapper.writeValueAsString(exchangeRateResponseDTO);

        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().write(jsonResponse);
    }

    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        String codes = pathInfo.substring(1);
        BufferedReader reader = req.getReader();

        String rate = null;
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.contains("rate=")) {
                int index = line.indexOf("rate=");
                rate = line.substring(index + 5);
                rate = rate.replace(',','.');
            }
        }

        String baseCurrencyCode = codes.substring(0, 3);
        String targetCurrencyCode = codes.substring(3, 6);

        ParameterValidator.checkCodePair(baseCurrencyCode, targetCurrencyCode);
        ParameterValidator.checkRate(rate);

        BigDecimal rateValue = new BigDecimal(rate);

        ExchangeRateRequestDTO exchangeRateRequestDTO = new ExchangeRateRequestDTO(baseCurrencyCode, targetCurrencyCode, rateValue);

        ExchangeRateService exchangeRateService = new ExchangeRateService(JdbcExchangeRateDAO.getInstance(), JdbcCurrencyDAO.getInstance());

        ExchangeRateResponseDTO exchangeRateResponseDTO = exchangeRateService.update(exchangeRateRequestDTO);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = objectMapper.writeValueAsString(exchangeRateResponseDTO);

        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().write(jsonResponse);
    }


    @Override
    public void destroy() {
        super.destroy();
    }
}
