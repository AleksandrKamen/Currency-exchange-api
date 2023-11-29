package servlet.currency;

import com.fasterxml.jackson.databind.ObjectMapper;
import dto.currency.CreateCurrencyDto;
import exception.ValidationException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.CurrencyService;
import validator.Error;

import java.io.IOException;
import java.sql.SQLException;

import static jakarta.servlet.http.HttpServletResponse.SC_CONFLICT;
import static jakarta.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;

@WebServlet("/currencies")
public class CurrenciesServlet extends HttpServlet {
   private static final CurrencyService currencyService  = CurrencyService.getInstance();
   private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
       try {
           var readAllCurrencies = currencyService.readAllCurrencies();
           objectMapper.writeValue(resp.getWriter(), readAllCurrencies);
       } catch (SQLException sqlException){
           resp.setStatus(SC_INTERNAL_SERVER_ERROR);
           objectMapper.writeValue(resp.getWriter(), Error.of(SC_INTERNAL_SERVER_ERROR, "Ошибка сервера"));
       }
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        CreateCurrencyDto currencyDto = CreateCurrencyDto.builder()
                .code(req.getParameter("code"))
                .name(req.getParameter("name"))
                .sign(req.getParameter("sign"))
                .build();
        try  {
            var currency = currencyService.create(currencyDto);
            objectMapper.writeValue(resp.getWriter(), currency);

        } catch (ValidationException validationException){
            objectMapper.writeValue(resp.getWriter(), validationException.getErrors());
        }
        catch (SQLException sqlException){
               resp.setStatus(SC_CONFLICT);
               objectMapper.writeValue(resp.getWriter(), Error.of(
                       SC_CONFLICT,
                       sqlException.getMessage()
               ));
        }
        catch (Exception e){
            e.getMessage();
            objectMapper.writeValue(resp.getWriter(), Error.of(SC_INTERNAL_SERVER_ERROR, "Ошибка сервера"));
        }
    }
}
