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
import java.util.ArrayList;
import java.util.List;

@WebServlet("/currencies")
public class CurrenciesServlet extends HttpServlet {

    ObjectMapper objectMapper = new ObjectMapper();
    CurrencyService currencyService = new CurrencyService(JdbcCurrencyDAO.getInstance());

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String code = req.getParameter("code").trim().toUpperCase();
        String name = req.getParameter("name").trim();
        String sign = req.getParameter("sign").trim();

        ParameterValidator.checkCode(code);
        ParameterValidator.checkName(name);
        ParameterValidator.checkSign(sign);

        CurrencyRequestDTO currencyRequestDTO = new CurrencyRequestDTO(code, name, sign);

        CurrencyResponseDTO currencyResponseDTO = currencyService.add(currencyRequestDTO);

        resp.setStatus(HttpServletResponse.SC_CREATED);
        objectMapper.writeValue(resp.getWriter(), currencyResponseDTO);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        List<CurrencyResponseDTO> currencyResponseDTOS = currencyService.getAll();

        resp.setStatus(HttpServletResponse.SC_OK);
        objectMapper.writeValue(resp.getWriter(), currencyResponseDTOS);
    }
}
