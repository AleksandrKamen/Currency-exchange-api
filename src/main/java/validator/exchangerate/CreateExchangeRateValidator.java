package validator.exchangerate;

import dao.CurrencyDao;
import dto.exchangeRate.CreateExchangeRateDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import validator.Error;
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
            validationResult.add(Error.of(SC_NOT_FOUND,"Первая указанная валюта отсутствует в базе данных"));
        }
        if (!currencyDao.findByCode(object.getTargetCurrencyCode()).isPresent()){
            validationResult.add(Error.of(SC_NOT_FOUND,"Вторая указанная валюта отсутствует в базе данных"));
        }
        return validationResult;
    }

    public ValidationResult isValidPostRequest (String baseCurrencyCode, String targetCurrencycode, String rate){
        ValidationResult validationResult = new ValidationResult();
        if (baseCurrencyCode == null || baseCurrencyCode.isEmpty()){
            validationResult.add(Error.of(SC_BAD_REQUEST,"Параметр baseCurrencyCode отсутствует"));
        } else if (!baseCurrencyCode.matches("[a-zA-Z]{3}")){
            validationResult.add(Error.of(SC_BAD_REQUEST,"Параметр baseCurrencyCode должен соответствовать стандарту ISO 4217"));
        }

        if (targetCurrencycode == null || targetCurrencycode.isEmpty()){
            validationResult.add(Error.of(SC_BAD_REQUEST,"Параметр targetCurrencyCode отсутствует"));
        } else if (!targetCurrencycode.matches("[a-zA-Z]{3}")){
            validationResult.add(Error.of(SC_BAD_REQUEST,"Параметр targetCurrencyCode должен соответствовать стандарту ISO 4217"));
        }

        if (rate == null || rate.isEmpty()){
            validationResult.add(Error.of(SC_BAD_REQUEST,"Параметр rate отсутствует"));
        } else {
            try {
                BigDecimal.valueOf(Double.parseDouble(rate));
            } catch (NumberFormatException e) {
                validationResult.add(Error.of(SC_BAD_REQUEST, "Параметр rate указан не корректно"));
            }
        }
        return validationResult;
    }

    public static CreateExchangeRateValidator getInstance(){return INSTANCE;}
}
