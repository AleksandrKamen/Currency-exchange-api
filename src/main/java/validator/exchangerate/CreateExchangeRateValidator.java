package validator.exchangerate;

import dao.CurrencyDao;
import dto.exchangeRate.CreateExchangeRateDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import validator.Error;
import validator.ErrorMessage;
import validator.ValidationResult;
import validator.Validator;

import java.math.BigDecimal;
import java.sql.SQLException;

import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static jakarta.servlet.http.HttpServletResponse.SC_NOT_FOUND;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateExchangeRateValidator implements Validator<CreateExchangeRateDto> {
    private static final CreateExchangeRateValidator INSTANCE = new CreateExchangeRateValidator();
    private final CurrencyDao currencyDao = CurrencyDao.getInstance();
    @Override
    public ValidationResult isValid(CreateExchangeRateDto object) throws SQLException {
        ValidationResult validationResult = new ValidationResult();

        if (!currencyDao.findByCode(object.getBaseCurrencyCode()).isPresent()){
            validationResult.add(Error.of(SC_NOT_FOUND,ErrorMessage.NOT_FOUND.formatted("первая валюта")));
        }
        if (!currencyDao.findByCode(object.getTargetCurrencyCode()).isPresent()){
            validationResult.add(Error.of(SC_NOT_FOUND,ErrorMessage.NOT_FOUND.formatted("вторая валюта")));
        }
        return validationResult;
    }

    public ValidationResult isValidRequest(String baseCurrencyCode, String targetCurrencycode, String rate){
        ValidationResult validationResult = new ValidationResult();
        if (baseCurrencyCode == null || baseCurrencyCode.isEmpty()){
            validationResult.add(Error.of(SC_BAD_REQUEST,ErrorMessage.MISSING_PARAMETER.formatted("baseCurrencyCode")));
        } else if (!baseCurrencyCode.matches("[a-zA-Z]{3}")){
            validationResult.add(Error.of(SC_BAD_REQUEST, "baseCurrencyCode - " + ErrorMessage.INVALID_CODE));
        }

        if (targetCurrencycode == null || targetCurrencycode.isEmpty()){
            validationResult.add(Error.of(SC_BAD_REQUEST,ErrorMessage.MISSING_PARAMETER.formatted("targetCurrencycode")));
        } else if (!targetCurrencycode.matches("[a-zA-Z]{3}")){
            validationResult.add(Error.of(SC_BAD_REQUEST,"targetCurrencycode - " + ErrorMessage.INVALID_CODE));
        }

        if (rate == null || rate.isEmpty()){
            validationResult.add(Error.of(SC_BAD_REQUEST, ErrorMessage.MISSING_PARAMETER.formatted("rate")));
        } else {
            try {
                BigDecimal.valueOf(Double.parseDouble(rate));
            } catch (NumberFormatException e) {
                validationResult.add(Error.of(SC_BAD_REQUEST, ErrorMessage.INVALID_PARAMETER.formatted("rate")));
            }
        }
        return validationResult;
    }

    public static CreateExchangeRateValidator getInstance(){return INSTANCE;}
}
