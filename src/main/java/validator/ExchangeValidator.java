package validator;


import dao.CurrencyDao;
import dao.ExchangeRateDao;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.SQLException;

import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static jakarta.servlet.http.HttpServletResponse.SC_NOT_FOUND;
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExchangeValidator {

    private static final ExchangeValidator INSTANCE = new ExchangeValidator();
    CurrencyDao currencyDao = CurrencyDao.getInstance();
    ExchangeRateDao exchangeRateDao = ExchangeRateDao.getInstance();

    public ValidationResult isValid(String from, String to, String amount) throws SQLException {
        ValidationResult validationResult = new ValidationResult();

        if (from == null || from.equals("")){
            validationResult.add(Error.of(SC_BAD_REQUEST, "Код валюты отсутствует в адресе"));
        }else if(!from.matches("[a-zA-Z]{3}")){
            validationResult.add(Error.of(SC_BAD_REQUEST,"Параметр code не соответствует стандарту ISO 4217"));
        } else if (!currencyDao.findByCode(from).isPresent()){
            validationResult.add(Error.of(SC_NOT_FOUND,"Первая указанная валюта отсутствует в базе данных"));
        }

        if (to == null || to.equals("")){
            validationResult.add(Error.of(SC_BAD_REQUEST, "Код валюты отсутствует в адресе"));
        }else if(!to.matches("[a-zA-Z]{3}")){
            validationResult.add(Error.of(SC_BAD_REQUEST,"Параметр code не соответствует стандарту ISO 4217"));
        } else if (!currencyDao.findByCode(to).isPresent()){
            validationResult.add(Error.of(SC_NOT_FOUND,"Вторая указанная валюта отсутствует в базе данных"));
        }

        if (amount == null || amount.isEmpty()){
            validationResult.add(Error.of(SC_BAD_REQUEST,"Параметр amount отсутствует"));
        } else {
            try {
                BigDecimal.valueOf(Double.parseDouble(amount));
            } catch (NumberFormatException e) {
                validationResult.add(Error.of(SC_BAD_REQUEST, "Параметр amount указан не корректно"));
            }
        }
        if (!exchangeRateDao.findByCodesCurrencies(from,to).isPresent() && !exchangeRateDao.findByCodesCurrencies(to,from).isPresent()
            && !exchangeRateDao.getCrossCource(from,to).isPresent()){
            validationResult.add(Error.of(SC_NOT_FOUND, "Невозможно выполнить обмен(отсутствует возможность конвертации)"));
        }

        return validationResult;

    }
    public static ExchangeValidator getInstance(){return INSTANCE;}

}
