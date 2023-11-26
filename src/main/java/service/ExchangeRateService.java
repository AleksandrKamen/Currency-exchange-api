package service;

import dao.ExchangeRateDao;
import dto.CurrencyDto;
import dto.ExchangeRateDto;
import entity.CurrencyEntity;
import entity.ExchangeRateEntity;
import exception.ValidationException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import mapper.exchange_rate_mapper.CreateExchangeRateMapper;
import mapper.exchange_rate_mapper.ReadExchangeRateMapper;
import validator.ValidationResult;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExchangeRateService {
    // TODO: 24.11.2023 Валидация на уровне сревиса
    private static final ExchangeRateService INSTANCE = new ExchangeRateService();
    private final ExchangeRateDao exchangeRateDao = ExchangeRateDao.getInstance();
    private final CreateExchangeRateMapper createExchangeRateMapper = CreateExchangeRateMapper.getInstance();
    private final ReadExchangeRateMapper readExchangeRateMapper = ReadExchangeRateMapper.getInstance();


    public Object[] readAllExchangeRates(){
       return exchangeRateDao.findAll()
                                .stream()
                                   .map(readExchangeRateMapper::mapFrom)
                                      .toArray();
    }

    @SneakyThrows
    public ExchangeRateDto create(ExchangeRateDto exchangeRateDto){
//        ValidationResult validationResult = createCurrencyValidator.isValid(currencyDto);
//        if (!validationResult.isValid()){
//            throw new ValidationException(validationResult.getErrors());
//        }
        ExchangeRateEntity exchangeRateEntity = createExchangeRateMapper.mapFrom(exchangeRateDto);
        exchangeRateDao.save(exchangeRateEntity);
        return exchangeRateDto;

    }



    public String[] getCodes(String codes){
        return new String[]{codes.substring(0,3), codes.substring(3)};
    }


    public static ExchangeRateService getInstance(){return INSTANCE;}

}
