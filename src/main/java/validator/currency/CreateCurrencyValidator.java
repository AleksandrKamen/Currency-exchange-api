package validator.currency;

import dto.currency.CreateCurrencyDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import validator.Error;
import validator.ErrorMessage;
import validator.ValidationResult;
import validator.Validator;

import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateCurrencyValidator implements Validator<CreateCurrencyDto> {
    private static final CreateCurrencyValidator INSTANCE = new CreateCurrencyValidator();
    @Override
    public ValidationResult isValid(CreateCurrencyDto object) {
      ValidationResult validationResult = new ValidationResult();

      if (object.getName() == null || object.getName().isEmpty()){
          validationResult.add(Error.of(SC_BAD_REQUEST,ErrorMessage.MISSING_PARAMETER.formatted("name")));
      } else if (!object.getName().matches("[a-zA-Z ]*")){
          validationResult.add(Error.of(SC_BAD_REQUEST,ErrorMessage.INVALID_PARAMETER.formatted("name")));
      }

      if (object.getCode() == null || object.getCode().isEmpty()){
          validationResult.add(Error.of(SC_BAD_REQUEST, ErrorMessage.MISSING_PARAMETER.formatted("code")));
      } else if (!object.getCode().matches("[a-zA-Z]{3}")){
          validationResult.add(Error.of(SC_BAD_REQUEST,ErrorMessage.INVALID_CODE));
      }

      if (object.getSign() == null || object.getSign().isEmpty()){
          validationResult.add(Error.of(SC_BAD_REQUEST,ErrorMessage.MISSING_PARAMETER.formatted("sign")));
      } else if(!object.getSign().matches("[^\\d\s]{1,3}")){
          validationResult.add(Error.of(SC_BAD_REQUEST,ErrorMessage.INVALID_PARAMETER.formatted("sign")));
      }
      return validationResult;
    }
    public static CreateCurrencyValidator getInstance(){return INSTANCE;}
}
