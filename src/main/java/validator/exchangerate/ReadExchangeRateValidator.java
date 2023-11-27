package validator.exchangerate;

import dto.ReadExchangeRateDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import validator.Error;
import validator.ValidationResult;
import validator.Validator;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReadExchangeRateValidator implements Validator<ReadExchangeRateDto> {

    private static final ReadExchangeRateValidator INSTANCE = new ReadExchangeRateValidator();

    @Override
    public ValidationResult isValid(ReadExchangeRateDto object) {
        ValidationResult validationResult = new ValidationResult();

        if (object == null){
            validationResult.add(Error.of(409,"Указанный курс отсутствует в базе данных"));
        }

        return validationResult;

    }

    public static ReadExchangeRateValidator getInstance(){return INSTANCE;}

}
