package validator;

import dao.CurrencyDao;
import dto.CurrencyDto;
import exception.ValidationException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateCurrencyValidator implements Validator<CurrencyDto>{
    private static final CreateCurrencyValidator INSTANCE = new CreateCurrencyValidator();



    @Override
    public ValidationResult isValid(CurrencyDto object) {
      ValidationResult validationResult = new ValidationResult();
      if (CurrencyDao.getInstance().findByCode(object.getCode()).isPresent()){
          validationResult.add(Error.of("Данная валюта уже имеется в базе данных"));
      }

      return validationResult;
    }



    public static CreateCurrencyValidator getInstance(){return INSTANCE;}
}
