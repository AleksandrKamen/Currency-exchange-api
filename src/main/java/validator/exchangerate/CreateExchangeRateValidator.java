package validator.exchangerate;

import dao.CurrencyDao;
import dto.exchangeRate.CreateExchangeRateDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import validator.Error;
import validator.ValidationResult;
import validator.Validator;

import java.sql.SQLException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateExchangeRateValidator implements Validator<CreateExchangeRateDto> {
    private static final CreateExchangeRateValidator INSTANCE = new CreateExchangeRateValidator();
    private final CurrencyDao currencyDao = CurrencyDao.getInstance();
    @Override
    public ValidationResult isValid(CreateExchangeRateDto object)  {
        ValidationResult validationResult = new ValidationResult();

        if (!currencyDao.findByCode(object.getBaseCurrencyCode()).isPresent() || !currencyDao.findByCode(object.getTargetCurrencyCode()).isPresent()){
            validationResult.add(Error.of(409,"Указанные валюты отсутствуют в базе данных"));
        }
        if (object.getBaseCurrencyCode().equals(object.getTargetCurrencyCode())){
            validationResult.add(Error.of(409,"Дублирование валюты"));
        }
        return validationResult;
    }
    public static CreateExchangeRateValidator getInstance(){return INSTANCE;}
}
