package servlet;

import dto.CurrencyDto;
import exception.ValidationException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.CurrencyService;

import java.io.IOException;

@WebServlet("/currencies")
public class CurrenciesServlet extends HttpServlet {
   private static final CurrencyService currencyService  = CurrencyService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            req.setAttribute("currencies", currencyService.readAllCurrencies());
            req.getRequestDispatcher("/WEB-INF/jsp/currencies.jsp").forward(req, resp);
    }



    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        CurrencyDto currencyDto = CurrencyDto.builder()
                .code(req.getParameter("code"))
                .name(req.getParameter("name"))
                .sign(req.getParameter("sign"))
                .build();

        try  {
            var string = currencyService.create(currencyDto);
            doGet(req, resp);

        } catch (ValidationException validationException){
            req.setAttribute("errors", validationException.getErrors());
            doGet(req, resp);



        }


    }
}
