package validator.currency;

import dao.CurrencyDao;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import validator.Error;
import validator.ValidationResult;
import validator.Validator;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReadCurrencyValidator implements Validator<String> {

    private static final ReadCurrencyValidator INSTANCE = new ReadCurrencyValidator();
    private  final CurrencyDao currencyDao = CurrencyDao.getInstance();
    @Override
    public ValidationResult isValid(String code) {
        ValidationResult validationResult = new ValidationResult();
        if (code == null){
            validationResult.add(Error.of(404, "Валюта не выбрана (не задан код валюты)"));
        }
        if(!code.matches("[a-zA-Z]{3}")){
            validationResult.add(Error.of(409,"Указаны некорректные данные(Недопустимые символы)"));
        }
         if (!currencyDao.findByCode(code).isPresent()){
            validationResult.add(Error.of(404, "Валюта с данным кодом не найдена"));
        }

        return validationResult;
    }

//    public ValidationResult isValidName(String name) {
//        ValidationResult validationResult = new ValidationResult();
//        if (name == null){
//            validationResult.add(Error.of(404, "Валюта не выбрана (не задано название валюты)"));
//        }
//        else if (!currencyDao.findByName(name).isPresent()){
//            validationResult.add(Error.of(404, "Валюта с данным именем не найдена"));
//        }
//
//        return validationResult;
//    }

    public static ReadCurrencyValidator getInstance(){return INSTANCE;}
}
