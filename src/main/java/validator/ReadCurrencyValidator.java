package validator;

import dao.CurrencyDao;
import dto.CurrencyDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReadCurrencyValidator{

    private static final ReadCurrencyValidator INSTANCE = new ReadCurrencyValidator();
    private  final CurrencyDao currencyDao = CurrencyDao.getInstance();

    public ValidationResult isValid(String code) {
        ValidationResult validationResult = new ValidationResult();
        if (code == null){
            validationResult.add(Error.of(404, "Валюта не выбрана (не задан код валюты)"));
        } else if (!currencyDao.findByCode(code).isPresent()){
            validationResult.add(Error.of(404, "Валюта с данным кодом не найдена"));
        }

        return validationResult;
    }



    public static ReadCurrencyValidator getInstance(){return INSTANCE;}
}
