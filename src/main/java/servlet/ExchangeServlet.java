package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import exception.ValidationException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.ExchangeRateService;

import java.io.IOException;
import java.sql.SQLException;

import static jakarta.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;

@WebServlet("/exchange")
public class ExchangeServlet extends HttpServlet {
    ExchangeRateService exchangeRateService = ExchangeRateService.getInstance();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var from = req.getParameter("from");
        var to = req.getParameter("to");
        var amount = req.getParameter("amount");
    try {
        var exchangeDto = exchangeRateService.makeExchange(from, to, amount);
        objectMapper.writeValue(resp.getWriter(), exchangeDto);
    } catch (ValidationException validationException){
        objectMapper.writeValue(resp.getWriter(), validationException.getErrors());
    } catch (SQLException sqlException){
        resp.setStatus(SC_INTERNAL_SERVER_ERROR);
        objectMapper.writeValue(resp.getWriter(), "Ошибка сервера");
    }

    }
}
