package validator.exchangeValidator;


import dao.CurrencyDao;
import dao.ExchangeRateDao;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import validator.Error;
import validator.ErrorMessage;
import validator.ValidationResult;

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
            validationResult.add(Error.of(SC_BAD_REQUEST, ErrorMessage.MISSING_PARAMETER.formatted("код валюты")));
        }else if(!from.matches("[a-zA-Z]{3}")){
            validationResult.add(Error.of(SC_BAD_REQUEST,ErrorMessage.INVALID_CODE));
        } else if (!currencyDao.findByCode(from).isPresent()){
            validationResult.add(Error.of(SC_NOT_FOUND,ErrorMessage.NOT_FOUND.formatted("первая указанная валюта")));
        }

        if (to == null || to.equals("")){
            validationResult.add(Error.of(SC_BAD_REQUEST, ErrorMessage.MISSING_PARAMETER.formatted("коды валют в адрессной строке")));
        }else if(!to.matches("[a-zA-Z]{3}")){
            validationResult.add(Error.of(SC_BAD_REQUEST,ErrorMessage.INVALID_CODE));
        } else if (!currencyDao.findByCode(to).isPresent()){
            validationResult.add(Error.of(SC_NOT_FOUND,ErrorMessage.NOT_FOUND.formatted("вторая валюта")));
        }

        if (amount == null || amount.isEmpty()){
            validationResult.add(Error.of(SC_BAD_REQUEST,ErrorMessage.MISSING_PARAMETER.formatted("amount")));
        } else {
            try {
                BigDecimal.valueOf(Double.parseDouble(amount));
            } catch (NumberFormatException e) {
                validationResult.add(Error.of(SC_BAD_REQUEST, ErrorMessage.INVALID_PARAMETER.formatted("amount")));
            }
        }
        if (!exchangeRateDao.findByCodesCurrencies(from,to).isPresent() && !exchangeRateDao.findByCodesCurrencies(to,from).isPresent()
            && !exchangeRateDao.getCrossCource(from,to).isPresent()){
            validationResult.add(Error.of(SC_NOT_FOUND, ErrorMessage.ERROR_EXCHANGE));
        }

        return validationResult;

    }
    public static ExchangeValidator getInstance(){return INSTANCE;}

}
