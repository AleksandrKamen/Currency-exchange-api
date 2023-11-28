package servlet.exchange_rate;

import dto.CreateExchangeRateDto;
import exception.ValidationException;
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
            req.setAttribute("exchangeRates", exchangeRateService.findAllExchangeRates());
            req.getRequestDispatcher(JSPUtil.getPath("exchangeRates")).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

            CreateExchangeRateDto exchangeRateDto = CreateExchangeRateDto.builder()
                    .baseCurrencyCode(req.getParameter("baseCurrency"))
                    .targetCurrencyCode(req.getParameter("targetCurrency"))
                    .rate(new BigDecimal(req.getParameter("rate")))
                    .build();
           try {
               if (exchangeRateService.isNew(exchangeRateDto)){
                   var update = exchangeRateService.updateExchangeRate(exchangeRateDto);
                   req.setAttribute("update", update);
                   doGet(req, resp);
               }
               else {
                   var newExchangeRate = exchangeRateService.createExchangeRate(exchangeRateDto);
                   req.setAttribute("newExchangeRate", newExchangeRate);
                   doGet(req, resp);
               }
           }catch (ValidationException validationException){
               req.setAttribute("errors", validationException.getErrors());
               doGet(req, resp);
           }catch (Exception e){
            resp.sendError(500, "Ошибка со стороны сервера");
        }
    }
}
