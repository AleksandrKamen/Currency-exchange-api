package validator.currency;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import validator.Error;
import validator.ValidationResult;
import validator.Validator;

import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReadCurrencyValidator implements Validator<String> {
    private static final ReadCurrencyValidator INSTANCE = new ReadCurrencyValidator();

    @Override
    public ValidationResult isValid(String code) {
        ValidationResult validationResult = new ValidationResult();
        if (code.equals("")){
            validationResult.add(Error.of(SC_BAD_REQUEST, "Код валюты отсутствует в адресе"));
        }else if(!code.matches("[a-zA-Z]{3}")){
            validationResult.add(Error.of(SC_BAD_REQUEST,"Параметр code должен соответствовать стандарту ISO 4217"));
        }
        return validationResult;
    }
    public static ReadCurrencyValidator getInstance(){return INSTANCE;}
}
