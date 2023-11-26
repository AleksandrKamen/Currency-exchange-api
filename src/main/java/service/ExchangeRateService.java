package service;

import dao.ExchangeRateDao;
import dto.CreateExchangeRateDto;
import dto.ReadExchangeRateDto;
import entity.ExchangeRateEntity;
import exception.ValidationException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import mapper.exchange_rate_mapper.CreateExchangeRateMapper;
import mapper.exchange_rate_mapper.ReadExchangeRateMapper;
import validator.ValidationResult;
import validator.exchangerate.CreateExchangeRateValidator;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExchangeRateService {
    // TODO: 24.11.2023 Валидация на уровне сревиса
    private static final ExchangeRateService INSTANCE = new ExchangeRateService();
    private final ExchangeRateDao exchangeRateDao = ExchangeRateDao.getInstance();
    private final CreateExchangeRateMapper createExchangeRateMapper = CreateExchangeRateMapper.getInstance();
    private final ReadExchangeRateMapper readExchangeRateMapper = ReadExchangeRateMapper.getInstance();
    private final CreateExchangeRateValidator createExchangeRateValidator = CreateExchangeRateValidator.getInstance();


    public Object[] readAllExchangeRates(){
       return exchangeRateDao.findAll()
                                .stream()
                                   .map(readExchangeRateMapper::mapFrom)
                                      .toArray();
    }

    @SneakyThrows
    public CreateExchangeRateDto create(CreateExchangeRateDto exchangeRateDto){
        ValidationResult validationResult = createExchangeRateValidator.isValid(exchangeRateDto);
        if (!validationResult.isValid()){
            throw new ValidationException(validationResult.getErrors());
        }
        ExchangeRateEntity exchangeRateEntity = createExchangeRateMapper.mapFrom(exchangeRateDto);
        exchangeRateDao.save(exchangeRateEntity);
        return exchangeRateDto;
    }


    public static ExchangeRateService getInstance(){return INSTANCE;}

}
