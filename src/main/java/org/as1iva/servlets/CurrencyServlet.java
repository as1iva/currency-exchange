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
        // TODO: убирать пробелы со всех сторон на входе и сделать uppercase
        ParameterValidator.checkCode(code);

        CurrencyRequestDTO currencyRequestDTO = new CurrencyRequestDTO(code);

        CurrencyService currencyService = new CurrencyService(JdbcCurrencyDAO.getInstance());


        CurrencyResponseDTO currencyResponseDTO = currencyService.getByCode(currencyRequestDTO);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = objectMapper.writeValueAsString(currencyResponseDTO);

        resp.setStatus(HttpServletResponse.SC_OK); // TODO: поменять код ответа
        resp.getWriter().write(jsonResponse);
    }

    @Override
    public void destroy() {
        super.destroy();
    }
}
