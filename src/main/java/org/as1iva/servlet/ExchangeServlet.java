package org.as1iva.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.as1iva.dao.JdbcExchangeRateDAO;
import org.as1iva.dto.ExchangeRequestDTO;
import org.as1iva.dto.ExchangeResponseDTO;
import org.as1iva.service.ExchangeService;
import org.as1iva.util.ParameterValidator;

import java.io.IOException;

@WebServlet("/exchange")
public class ExchangeServlet extends HttpServlet {

    ObjectMapper objectMapper = new ObjectMapper();
    ExchangeService exchangeService = new ExchangeService(JdbcExchangeRateDAO.getInstance());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String baseCurrencyCode = req.getParameter("from").trim();
        String targetCurrencyCode = req.getParameter("to").trim();
        String amount = req.getParameter("amount").trim();

        ParameterValidator.checkCode(baseCurrencyCode);
        ParameterValidator.checkCode(targetCurrencyCode);
        ParameterValidator.checkAmount(amount);

        Integer amountValue = Integer.valueOf(amount);

        ExchangeRequestDTO exchangeRequestDTO = new ExchangeRequestDTO(baseCurrencyCode, targetCurrencyCode, amountValue);

        ExchangeResponseDTO exchangeResponseDTO = exchangeService.exchange(exchangeRequestDTO);

        resp.setStatus(HttpServletResponse.SC_OK);
        objectMapper.writeValue(resp.getWriter(), exchangeResponseDTO);
    }
}
