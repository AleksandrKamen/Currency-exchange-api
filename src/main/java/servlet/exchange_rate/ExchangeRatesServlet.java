package servlet.exchange_rate;

import dto.ExchangeRateDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.ExchangeRateService;
import util.JSPUtil;

import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/exchangeRates")
public class ExchangeRatesServlet extends HttpServlet {
    private final ExchangeRateService exchangeRateService = ExchangeRateService.getInstance();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // TODO: 23.11.2023 Валидация на работу бд 
        req.setAttribute("exchangeRates", exchangeRateService.readAllExchangeRates());
        req.getRequestDispatcher(JSPUtil.getPath("exchangeRates")).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        var codes = exchangeRateService.getCodes(req.getParameter("codes"));

        ExchangeRateDto exchangeRateDto = ExchangeRateDto.builder()
                .baseCurrency(req.getParameter("baseCurrency"))
                .targetCurrency(req.getParameter("targetCurrency"))
                .rate(new BigDecimal(req.getParameter("rate")))
                .build();

        var newExchangeRate = exchangeRateService.create(exchangeRateDto);
        req.setAttribute("newExchangeRate", newExchangeRate);
        doGet(req, resp);


    }
}
