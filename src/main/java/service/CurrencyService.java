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
import validator.ValidationResult;

import java.util.stream.Collectors;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CurrencyService {
    private static final CurrencyService INSTANCE = new CurrencyService();
    private final CurrencyDao currencyDao = CurrencyDao.getInstance();

    private  final CreateCurrencyMapper createCurrencyMapper = CreateCurrencyMapper.getInstance();

    private final ReadCurrencyMapper readCurrencyMapper = ReadCurrencyMapper.getInstance();

     private final CreateCurrencyValidator createCurrencyValidator = CreateCurrencyValidator.getInstance();



     public String readAllCurrencies(){
       var allCurrencies = currencyDao.findAll();
       return allCurrencies.stream()
               .map(readCurrencyMapper::mapFrom)
                 .map(currencyDto -> mapDto(currencyDto))
                    .collect(Collectors.joining("\n"));

     }

     public String read(){
         var currencyEntity = currencyDao.findById(3);

         return "";

     }

    @SneakyThrows
    public String  create(CurrencyDto currencyDto){
        ObjectMapper objectMapper = new ObjectMapper();
        ValidationResult validationResult = createCurrencyValidator.isValid(currencyDto);
        // validation - проверяем правильность набора
        if (validationResult.isValid()){
            throw new ValidationException(validationResult.getErrors());
        }
       // map конвертируем данные в сущность
        CurrencyEntity currency = createCurrencyMapper.mapFrom(currencyDto);
      // Сохраням сущность в БД
        currencyDao.save(currency);
     // Возвращаем String
     return objectMapper.writeValueAsString(currency);

    }






@SneakyThrows
public String mapDto(CurrencyDto currencyDto){
    ObjectMapper objectMapper = new ObjectMapper();
    return objectMapper.writeValueAsString(currencyDto);

}
    public static CurrencyService getInstance(){return INSTANCE;}

}
