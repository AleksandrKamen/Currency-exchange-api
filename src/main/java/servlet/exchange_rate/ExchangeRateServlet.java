package servlet.exchange_rate;

import dto.ReadExchangeRateDto;
import exception.ValidationException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.ExchangeRateService;
import util.JSPUtil;

import java.io.IOException;

@WebServlet("/exchange")
public class ExchangeRateServlet extends HttpServlet {

    ExchangeRateService exchangeRateService = ExchangeRateService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var baseCode = req.getParameter("baseCode");
        var targetCode = req.getParameter("targetCode");

        try {
            req.setAttribute("exchangeRate", exchangeRateService.findExchangeRate(baseCode, targetCode));
            req.getRequestDispatcher(JSPUtil.getPath("exchangeRate")).forward(req, resp);
        } catch (ValidationException validationException){
            req.setAttribute("errors", validationException.getErrors());
            req.getRequestDispatcher(JSPUtil.getPath("exchangeRate")).forward(req, resp);
        } catch (Exception e){
            resp.sendError(500,"Ошибка на стороне сервера");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var from = req.getParameter("from");
        var to = req.getParameter("to");
        var amount = req.getParameter("amount");
    try {
    var exchangeRate = exchangeRateService.makeExchange(from, to, Double.valueOf(amount));
        System.out.println(exchangeRate);
    req.setAttribute("converted", exchangeRate);
    req.getRequestDispatcher(JSPUtil.getPath("exchangeRate")).forward(req, resp);
    } catch (ValidationException validationException){
        req.setAttribute("errors", validationException.getErrors());
        req.getRequestDispatcher(JSPUtil.getPath("exchangeRate")).forward(req, resp);
    }

    }
}
