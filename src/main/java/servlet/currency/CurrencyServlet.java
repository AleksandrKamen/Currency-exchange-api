package servlet.currency;

import com.fasterxml.jackson.databind.ObjectMapper;
import exception.ValidationException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.CurrencyService;
import validator.Error;
import validator.ErrorMessage;

import java.io.IOException;
import java.sql.SQLException;

import static jakarta.servlet.http.HttpServletResponse.*;

@WebServlet("/currency/*")
public class CurrencyServlet extends HttpServlet {
    private static final CurrencyService currencyService  = CurrencyService.getInstance();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var code = req.getPathInfo().replaceAll("/","").toUpperCase();

        try {
            var currency = currencyService.readCurrencyByCode(code);
            objectMapper.writeValue(resp.getWriter(), currency.get());
        } catch (ValidationException validationException){
            resp.setStatus(SC_BAD_REQUEST);
            objectMapper.writeValue(resp.getWriter(), validationException.getErrors());
        }catch (SQLException sqlException){
            resp.setStatus(SC_INTERNAL_SERVER_ERROR);
            objectMapper.writeValue(resp.getWriter(), Error.of(SC_INTERNAL_SERVER_ERROR, ErrorMessage.SERVER_ERROR));
        }
    }
}
