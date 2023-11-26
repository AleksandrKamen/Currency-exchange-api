package validator.currency;

import dao.CurrencyDao;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import validator.Error;
import validator.ValidationResult;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReadCurrencyValidator{

    private static final ReadCurrencyValidator INSTANCE = new ReadCurrencyValidator();
    private  final CurrencyDao currencyDao = CurrencyDao.getInstance();

    public ValidationResult isValidCode(String code) {
        ValidationResult validationResult = new ValidationResult();
        if (code == null){
            validationResult.add(Error.of(404, "Валюта не выбрана (не задан код валюты)"));
        } else if(code.length() != 3){
            validationResult.add(Error.of(404, "Код указан неккорректно - количество символов не равно 3"));
        }
        else if (!currencyDao.findByCode(code).isPresent()){
            validationResult.add(Error.of(404, "Валюта с данным кодом не найдена"));
        }

        return validationResult;
    }

    public ValidationResult isValidName(String name) {
        ValidationResult validationResult = new ValidationResult();
        if (name == null){
            validationResult.add(Error.of(404, "Валюта не выбрана (не задано название валюты)"));
        }
        else if (!currencyDao.findByName(name).isPresent()){
            validationResult.add(Error.of(404, "Валюта с данным именем не найдена"));
        }

        return validationResult;
    }



    public static ReadCurrencyValidator getInstance(){return INSTANCE;}
}
