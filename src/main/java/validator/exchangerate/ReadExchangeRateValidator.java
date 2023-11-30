package validator.exchangerate;

import entity.CurrencyEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import validator.Error;
import validator.ErrorMessage;
import validator.ValidationResult;
import validator.Validator;

import java.util.List;

import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static jakarta.servlet.http.HttpServletResponse.SC_NOT_FOUND;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReadExchangeRateValidator implements Validator<List<CurrencyEntity>> {
    private static final ReadExchangeRateValidator INSTANCE = new ReadExchangeRateValidator();

    @Override
    public ValidationResult isValid(List<CurrencyEntity> list) {
        ValidationResult validationResult = new ValidationResult();
        if (list.size() != 2){
            validationResult.add(Error.of(SC_NOT_FOUND,ErrorMessage.NOT_FOUND.formatted("валюта/валюты")));
        }
        return validationResult;
    }

    public ValidationResult isValidCurrenciesCodes(String currencyCodes)  {
      ValidationResult validationResult = new ValidationResult();

      if (currencyCodes == null || currencyCodes.isEmpty()){
          validationResult.add(Error.of(SC_BAD_REQUEST,ErrorMessage.MISSING_PARAMETER.formatted("коды валют")));
      }
     else if (currencyCodes.length() != 6){
          validationResult.add(Error.of(SC_BAD_REQUEST,"Коды заданных вылют - " +ErrorMessage.INVALID_CODE));
      } else {
          var baseCurrency = currencyCodes.substring(0, 3).toUpperCase();
          var targetCurrency = currencyCodes.substring(3).toUpperCase();
      if (!baseCurrency.matches("[a-zA-Z]{3}")){
          validationResult.add(Error.of(SC_BAD_REQUEST, "Валюта 1 - " + ErrorMessage.INVALID_CODE));
      }
      if (!targetCurrency.matches("[a-zA-Z]{3}")){
          validationResult.add(Error.of(SC_BAD_REQUEST,"Валюта 2 - " + ErrorMessage.INVALID_CODE));
      }

    }
      return validationResult;
    }
    public static ReadExchangeRateValidator getInstance(){return INSTANCE;}
}
