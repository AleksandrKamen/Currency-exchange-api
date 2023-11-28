package servlet.currency;

import dto.CurrencyDto;
import exception.ValidationException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.CurrencyService;
import util.JSPUtil;

import java.io.IOException;

@WebServlet("/currencies")
public class CurrenciesServlet extends HttpServlet {
   private static final CurrencyService currencyService  = CurrencyService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            req.setAttribute("currencies", currencyService.readAllCurrencies());
            req.getRequestDispatcher(JSPUtil.getPath("currencies")).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        CurrencyDto currencyDto = CurrencyDto.builder()
                .code(req.getParameter("code"))
                .name(req.getParameter("name"))
                .sign(req.getParameter("sign"))
                .build();
        try  {
            var currency = currencyService.create(currencyDto);
            req.setAttribute("newCurrency", currency);
            doGet(req, resp);

        } catch (ValidationException validationException){
            req.setAttribute("errors", validationException.getErrors());
            doGet(req, resp);
        } catch (Exception e){
            resp.sendError(500, "Ошибка со стороны сервера");
        }
    }
}
