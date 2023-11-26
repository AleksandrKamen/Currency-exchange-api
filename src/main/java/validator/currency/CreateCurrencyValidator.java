package validator.currency;

import dao.CurrencyDao;
import dto.CurrencyDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import validator.Error;
import validator.ValidationResult;
import validator.Validator;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateCurrencyValidator implements Validator<CurrencyDto> {
    private static final CreateCurrencyValidator INSTANCE = new CreateCurrencyValidator();
    private  final CurrencyDao currencyDao = CurrencyDao.getInstance();



    @Override
    public ValidationResult isValid(CurrencyDto object) {
      ValidationResult validationResult = new ValidationResult();

      if (currencyDao.findByCode(object.getCode()).isPresent()
          || currencyDao.findByName(object.getName()).isPresent() || currencyDao.findBySign(object.getSign()).isPresent()){
          validationResult.add(Error.of(409,"Указаны некорректные или повторяющиеся данные - повторите попытку снова"));
      }
      return validationResult;
    }



    public static CreateCurrencyValidator getInstance(){return INSTANCE;}
}
