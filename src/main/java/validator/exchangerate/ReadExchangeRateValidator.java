package validator.exchangerate;

import dao.CurrencyDao;
import entity.CurrencyEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import validator.Error;
import validator.ValidationResult;
import validator.Validator;

import java.sql.SQLException;
import java.util.List;

import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static jakarta.servlet.http.HttpServletResponse.SC_NOT_FOUND;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReadExchangeRateValidator implements Validator<List<CurrencyEntity>> {
    private static final ReadExchangeRateValidator INSTANCE = new ReadExchangeRateValidator();
    private final CurrencyDao currencyDao = CurrencyDao.getInstance();

    @Override
    public ValidationResult isValid(List<CurrencyEntity> list) {
        ValidationResult validationResult = new ValidationResult();
        if (list.size() != 2){
            validationResult.add(Error.of(SC_NOT_FOUND,"Одна или обе валюты не найдены"));
        }
        return validationResult;
    }

    public ValidationResult isValidCurrenciesCodes(String currencyCodes)  {
      ValidationResult validationResult = new ValidationResult();

      if (currencyCodes == null || currencyCodes.isEmpty()){
          validationResult.add(Error.of(SC_BAD_REQUEST,"Коды валют не заданы"));
      }
     else if (currencyCodes.length() != 6){
          validationResult.add(Error.of(SC_BAD_REQUEST,"Коды валют не соответствуют стандарту ISO 4217"));
      } else {
          var baseCurrency = currencyCodes.substring(0, 3).toUpperCase();
          var targetCurrency = currencyCodes.substring(3).toUpperCase();
      if (!baseCurrency.matches("[a-zA-Z]{3}")){
          validationResult.add(Error.of(SC_BAD_REQUEST,"Код первой валюты не соответствует стандарту ISO 4217"));
      }
      if (!targetCurrency.matches("[a-zA-Z]{3}")){
          validationResult.add(Error.of(SC_BAD_REQUEST,"Код второй валюты не соответствует стандарту ISO 4217"));
      }

    }
      return validationResult;
    }
    public static ReadExchangeRateValidator getInstance(){return INSTANCE;}
}
