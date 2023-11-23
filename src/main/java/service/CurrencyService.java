package service;

import com.fasterxml.jackson.databind.ObjectMapper;
import dao.CurrencyDao;
import dto.CurrencyDto;
import entity.CurrencyEntity;
import exception.ValidationException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import mapper.currency_mapper.CreateCurrencyMapper;
import mapper.currency_mapper.ReadCurrencyMapper;
import validator.CreateCurrencyValidator;
import validator.ReadCurrencyValidator;
import validator.ValidationResult;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CurrencyService {
    private static final CurrencyService INSTANCE = new CurrencyService();
    private final CurrencyDao currencyDao = CurrencyDao.getInstance();

    private  final CreateCurrencyMapper createCurrencyMapper = CreateCurrencyMapper.getInstance();

    private final ReadCurrencyMapper readCurrencyMapper = ReadCurrencyMapper.getInstance();

    private final CreateCurrencyValidator createCurrencyValidator = CreateCurrencyValidator.getInstance();
    private final ReadCurrencyValidator readCurrencyValidator = ReadCurrencyValidator.getInstance();



     public Object[] readAllCurrencies(){
       var allCurrencies = currencyDao.findAll();
       return allCurrencies.stream()
               .map(readCurrencyMapper::mapFrom).toArray();
     }

     public CurrencyDto readCurrencyByCode(String code){
         ValidationResult validationResult = readCurrencyValidator.isValid(code);
         if (!validationResult.isValid()){
             throw new ValidationException(validationResult.getErrors());
         }
         return currencyDao.findByCode(code)
                 .map(readCurrencyMapper::mapFrom).get();

     }



    @SneakyThrows
    public CurrencyDto create(CurrencyDto currencyDto){
        ObjectMapper objectMapper = new ObjectMapper();
        ValidationResult validationResult = createCurrencyValidator.isValid(currencyDto);
        if (!validationResult.isValid()){
            throw new ValidationException(validationResult.getErrors());
        }
        CurrencyEntity currency = createCurrencyMapper.mapFrom(currencyDto);
        currencyDao.save(currency);
     return currencyDto;

    }


@SneakyThrows
public String mapDto(CurrencyDto currencyDto){
    ObjectMapper objectMapper = new ObjectMapper();
    return objectMapper.writeValueAsString(currencyDto);

}
    public static CurrencyService getInstance(){return INSTANCE;}

}
