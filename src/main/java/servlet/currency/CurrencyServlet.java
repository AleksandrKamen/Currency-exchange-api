package servlet.currency;

import exception.ValidationException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.CurrencyService;
import util.JSPUtil;

import java.io.IOException;

@WebServlet("/currency")
public class CurrencyServlet extends HttpServlet {
    private static final CurrencyService currencyService  = CurrencyService.getInstance();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var code = req.getParameter("code");
        try {
                req.setAttribute("currency", currencyService.readCurrencyByCode(code));
                req.getRequestDispatcher(JSPUtil.getPath("currency")).forward(req, resp);
        } catch (ValidationException validationException){
            req.setAttribute("errors", validationException.getErrors());
            req.getRequestDispatcher(JSPUtil.getPath("currency")).forward(req, resp);
        }
    }
}
