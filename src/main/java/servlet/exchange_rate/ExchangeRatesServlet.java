package servlet.exchange_rate;

import com.fasterxml.jackson.databind.ObjectMapper;
import dto.exchangeRate.CreateExchangeRateDto;
import exception.ValidationException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.ExchangeRateService;
import validator.Error;


import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;

import static jakarta.servlet.http.HttpServletResponse.*;
import static jakarta.servlet.http.HttpServletResponse.SC_CONFLICT;

@WebServlet("/exchangeRates")
public class ExchangeRatesServlet extends HttpServlet {
    private final ExchangeRateService exchangeRateService = ExchangeRateService.getInstance();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
          try {
              var allExchangeRates = exchangeRateService.findAllExchangeRates();
              objectMapper.writeValue(resp.getWriter(),allExchangeRates);
          } catch (SQLException sqlException){
              resp.setStatus(SC_INTERNAL_SERVER_ERROR);
              objectMapper.writeValue(resp.getWriter(), Error.of(SC_INTERNAL_SERVER_ERROR, "Ошибка сервера"));
          }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var baseCurrencyCode = req.getParameter("baseCurrencyCode");
        var targetCurrencyCode = req.getParameter("targetCurrencyCode");
        var rate = req.getParameter("rate");

        try {
            exchangeRateService.isValidRequest(baseCurrencyCode,targetCurrencyCode,rate);
        } catch (ValidationException validationException){
            objectMapper.writeValue(resp.getWriter(), validationException.getErrors());
        }
            CreateExchangeRateDto exchangeRateDto = CreateExchangeRateDto.builder()
                    .baseCurrencyCode(baseCurrencyCode)
                    .targetCurrencyCode(targetCurrencyCode)
                    .rate(new BigDecimal(rate))
                    .build();
           try {
               var newExchangeRate = exchangeRateService.createExchangeRate(exchangeRateDto);
               objectMapper.writeValue(resp.getWriter(),newExchangeRate);
           } catch (ValidationException validationException){
               objectMapper.writeValue(resp.getWriter(), validationException.getErrors());
           }
           catch (SQLException sqlException){
               resp.setStatus(SC_CONFLICT);
               objectMapper.writeValue(resp.getWriter(), Error.of(
                       SC_CONFLICT,
                       sqlException.getMessage()
               ));
           }  catch (Exception e){
               resp.setStatus(SC_INTERNAL_SERVER_ERROR);
               objectMapper.writeValue(resp.getWriter(), Error.of(SC_INTERNAL_SERVER_ERROR, "Ошибка сервера"));
           }
    }
}
