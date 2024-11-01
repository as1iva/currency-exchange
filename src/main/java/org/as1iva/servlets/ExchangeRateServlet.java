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

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;

@WebServlet("/exchangeRate/*")
public class ExchangeRateServlet extends HttpServlet {
    @Override
    public void init() throws ServletException {
        super.init();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();

        if (pathInfo != null && pathInfo.length() > 1) {
            String codes = pathInfo.substring(1);

            String baseCurrencyCode = codes.substring(0, 3);
            String targetCurrencyCode = codes.substring(3, 6);

            ExchangeRateRequestDTO exchangeRateRequestDTO = new ExchangeRateRequestDTO(baseCurrencyCode, targetCurrencyCode);

            ExchangeRateService exchangeRateService = new ExchangeRateService(JdbcExchangeRateDAO.getInstance(), JdbcCurrencyDAO.getInstance());

            try {
                ExchangeRateResponseDTO exchangeRateResponseDTO = exchangeRateService.getByCode(exchangeRateRequestDTO);

                ObjectMapper objectMapper = new ObjectMapper();
                String jsonResponse = objectMapper.writeValueAsString(exchangeRateResponseDTO);

                resp.setContentType("application/json");
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().write(jsonResponse);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        BigDecimal rate = new BigDecimal(req.getParameter("rate"));

        if (pathInfo != null && pathInfo.length() > 1) {
            String codes = pathInfo.substring(1);

            String baseCurrencyCode = codes.substring(0, 3);
            String targetCurrencyCode = codes.substring(3, 6);


            ExchangeRateRequestDTO exchangeRateRequestDTO = new ExchangeRateRequestDTO(baseCurrencyCode, targetCurrencyCode, rate);

            ExchangeRateService exchangeRateService = new ExchangeRateService(JdbcExchangeRateDAO.getInstance(), JdbcCurrencyDAO.getInstance());

            try {
                ExchangeRateResponseDTO exchangeRateResponseDTO = exchangeRateService.update(exchangeRateRequestDTO);

                ObjectMapper objectMapper = new ObjectMapper();
                String jsonResponse = objectMapper.writeValueAsString(exchangeRateResponseDTO);

                resp.setContentType("application/json");
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().write(jsonResponse);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }


    @Override
    public void destroy() {
        super.destroy();
    }
}
