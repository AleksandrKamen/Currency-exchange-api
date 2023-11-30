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
import validator.ErrorMessage;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static jakarta.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;

@WebServlet("/exchangeRate/*")
public class ExchangeRateServlet extends HttpServlet {

    ExchangeRateService exchangeRateService = ExchangeRateService.getInstance();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var сurrencyСodes = req.getPathInfo().replaceAll("/", "").toUpperCase();
        try {
            exchangeRateService.isValidRequest(сurrencyСodes);
        } catch (ValidationException validationException) {
            objectMapper.writeValue(resp.getWriter(), validationException.getErrors());
        }
        var baseCurrencyCode = сurrencyСodes.substring(0, 3).toUpperCase();
        var targetCurrencyCode = сurrencyСodes.substring(3).toUpperCase();

        try {
            var exchangeRate = exchangeRateService.findExchangeRate(baseCurrencyCode, targetCurrencyCode);
            objectMapper.writeValue(resp.getWriter(), exchangeRate);
        } catch (ValidationException validationException) {
            objectMapper.writeValue(resp.getWriter(), validationException.getErrors());
        } catch (Exception e) {
            resp.setStatus(SC_INTERNAL_SERVER_ERROR);
            objectMapper.writeValue(resp.getWriter(), Error.of(SC_INTERNAL_SERVER_ERROR, ErrorMessage.SERVER_ERROR));
        }
    }

    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var сurrencyСodes = req.getPathInfo().replaceAll("/", "").toUpperCase();
        var par = req.getReader().readLine();
        if (par == null || par.isEmpty()){
            resp.setStatus(SC_BAD_REQUEST);
            objectMapper.writeValue(resp.getWriter(), new ValidationException(List.of(Error.of(400, ErrorMessage.MISSING_PARAMETER.formatted("rate")))).getErrors());
        }
        var rate = par.substring(par.indexOf("=") + 1);

        try {
            exchangeRateService.isValidRequest(сurrencyСodes);
        } catch (ValidationException validationException) {
            objectMapper.writeValue(resp.getWriter(), validationException.getErrors());
        }
        var baseCurrencyCode = сurrencyСodes.substring(0, 3).toUpperCase();
        var targetCurrencyCode = сurrencyСodes.substring(3).toUpperCase();

        try {
            exchangeRateService.isValidRequest(baseCurrencyCode, targetCurrencyCode, rate);
        } catch (ValidationException validationException) {
            objectMapper.writeValue(resp.getWriter(), validationException.getErrors());
        }
        var exchangeRateDto = CreateExchangeRateDto.builder()
                .baseCurrencyCode(baseCurrencyCode)
                .targetCurrencyCode(targetCurrencyCode)
                .rate(new BigDecimal(rate))
                .build();

        try {
            var createExchangeRateDto = exchangeRateService.updateExchangeRate(exchangeRateDto);
            objectMapper.writeValue(resp.getWriter(), createExchangeRateDto);
        } catch (ValidationException validationException) {
            objectMapper.writeValue(resp.getWriter(), validationException.getErrors());
        } catch (SQLException sqlException) {
            resp.setStatus(SC_INTERNAL_SERVER_ERROR);
            objectMapper.writeValue(resp.getWriter(), ErrorMessage.SERVER_ERROR);
        }
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getMethod().equalsIgnoreCase("PATCH")) {
            doPatch(req, resp);
        } else {
            super.service(req, resp);
        }
    }
}


