package validator.currency;

import dao.CurrencyDao;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import validator.Error;
import validator.ErrorMessage;
import validator.ValidationResult;
import validator.Validator;

import java.sql.SQLException;

import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static jakarta.servlet.http.HttpServletResponse.SC_NOT_FOUND;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReadCurrencyValidator implements Validator<String> {
    private static final ReadCurrencyValidator INSTANCE = new ReadCurrencyValidator();
    CurrencyDao currencyDao = CurrencyDao.getInstance();

    @Override
    public ValidationResult isValid(String code) throws SQLException {
        ValidationResult validationResult = new ValidationResult();
        if (code == null || code.equals("")){
            validationResult.add(Error.of(SC_BAD_REQUEST, ErrorMessage.MISSING_PARAMETER.formatted("code")));
        }else if(!code.matches("[a-zA-Z]{3}")){
            validationResult.add(Error.of(SC_BAD_REQUEST, ErrorMessage.INVALID_CODE));
        } else if (!currencyDao.findByCode(code).isPresent()) {
            validationResult.add(Error.of(SC_NOT_FOUND,ErrorMessage.NOT_FOUND.formatted("валюта")));
        }
        return validationResult;
    }
    public static ReadCurrencyValidator getInstance(){return INSTANCE;}
}
