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
import java.nio.charset.StandardCharsets;

@WebServlet("/currencies")
public class CurrencyServlet extends HttpServlet {
   private static final CurrencyService currencyService  = CurrencyService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("currencies", currencyService.readAllCurrencies());
        req.getRequestDispatcher("/WEB-INF/jsp/currencies.jsp").forward(req,resp);
    }


// TODO: 22.11.2023

//    @Override
//    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//
//        CurrencyDto currencyDto = CurrencyDto.builder()
//                .name(req.getParameter("name"))
//                .code(req.getParameter("code"))
//                .sign(req.getParameter("sign"))
//                .build();
//
//        try (var writer = resp.getWriter()) {
//            var string = currencyService.create(currencyDto);
//            resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
//            writer.write(string);
//        } catch (ValidationException validationException){
//
//        }


//    }
}
