package validator.exchangerate;

import entity.CurrencyEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import validator.Error;
import validator.ValidationResult;
import validator.Validator;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReadExchangeRateValidator implements Validator<List<CurrencyEntity>> {
    private static final ReadExchangeRateValidator INSTANCE = new ReadExchangeRateValidator();
    @Override
    public ValidationResult isValid(List<CurrencyEntity> list) {
        ValidationResult validationResult = new ValidationResult();
        if (list.size() != 2){
            validationResult.add(Error.of(409,"Указанные валюты отсутсвуют в базе данных"));
        }
        return validationResult;
    }
    public static ReadExchangeRateValidator getInstance(){return INSTANCE;}
}
