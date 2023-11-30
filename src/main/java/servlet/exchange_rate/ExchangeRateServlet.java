package servlet.exchange_rate;

import com.fasterxml.jackson.databind.ObjectMapper;
import dto.exchangeRate.ReadExchangeRateDto;
import exception.ValidationException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.ExchangeRateService;
import util.JSPUtil;
import validator.Error;

import java.io.IOException;
import java.sql.SQLException;

import static jakarta.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;

@WebServlet("/exchangeRate/*")
public class ExchangeRateServlet extends HttpServlet {

    ExchangeRateService exchangeRateService = ExchangeRateService.getInstance();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var сurrencyСodes = req.getPathInfo().replaceAll("/","").toUpperCase();
        try {
            exchangeRateService.isValidRequest(сurrencyСodes);
        } catch (ValidationException validationException){
            objectMapper.writeValue(resp.getWriter(), validationException.getErrors());
        }
        var baseCurrencyCode = сurrencyСodes.substring(0,3).toUpperCase();
        var targetCurrencyCode = сurrencyСodes.substring(3).toUpperCase();

         try {
             var exchangeRate = exchangeRateService.findExchangeRate(baseCurrencyCode, targetCurrencyCode);
             objectMapper.writeValue(resp.getWriter(), exchangeRate);
         } catch (ValidationException validationException){
             objectMapper.writeValue(resp.getWriter(), validationException.getErrors());
         } catch (Exception e){
             resp.setStatus(SC_INTERNAL_SERVER_ERROR);
             objectMapper.writeValue(resp.getWriter(), Error.of(SC_INTERNAL_SERVER_ERROR, "Ошибка сервера"));
         }


        }
    }

//    @Override
//    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        var from = req.getParameter("from");
//        var to = req.getParameter("to");
//        var amount = req.getParameter("amount");
//    try {
//    var exchangeRate = exchangeRateService.makeExchange(from, to, Double.valueOf(amount));
//        System.out.println(exchangeRate);
//    req.setAttribute("converted", exchangeRate);
//    req.getRequestDispatcher(JSPUtil.getPath("exchangeRate")).forward(req, resp);
//    } catch (ValidationException validationException){
//        req.setAttribute("errors", validationException.getErrors());
//        req.getRequestDispatcher(JSPUtil.getPath("exchangeRate")).forward(req, resp);
//    } catch (Exception e){
//        resp.sendError(500,"Ошибка на стороне сервера");
//    }
//
//    }
//}
