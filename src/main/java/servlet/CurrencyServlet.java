package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.CurrencyService;

import java.io.IOException;

@WebServlet("/currency")
public class CurrencyServlet extends HttpServlet {
    private static final CurrencyService currencyService  = CurrencyService.getInstance();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var code = req.getParameter("code");
        req.setAttribute("currency", currencyService.readCurrencyByCode(code));
        req.getRequestDispatcher("/WEB-INF/jsp/currency.jsp").forward(req, resp);
    }
}
