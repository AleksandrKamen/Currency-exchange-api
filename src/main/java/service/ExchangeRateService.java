package service;

import dao.ExchangeRateDao;
import dto.CourceDto;
import dto.exchangeRate.CreateExchangeRateDto;
import dto.exchangeRate.ReadExchangeRateDto;
import entity.ExchangeRateEntity;
import exception.ValidationException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import mapper.exchange_rate_mapper.CreateExchangeRateMapper;
import mapper.exchange_rate_mapper.ReadExchangeRateMapper;
import validator.Error;
import validator.ValidationResult;
import validator.exchangerate.CreateExchangeRateValidator;
import validator.exchangerate.ReadExchangeRateValidator;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

import static jakarta.servlet.http.HttpServletResponse.SC_NOT_FOUND;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExchangeRateService {

    private static final ExchangeRateService INSTANCE = new ExchangeRateService();
    private final ExchangeRateDao exchangeRateDao = ExchangeRateDao.getInstance();
    private final CreateExchangeRateMapper createExchangeRateMapper = CreateExchangeRateMapper.getInstance();
    private final ReadExchangeRateMapper readExchangeRateMapper = ReadExchangeRateMapper.getInstance();
    private final CreateExchangeRateValidator createExchangeRateValidator = CreateExchangeRateValidator.getInstance();
    private final ReadExchangeRateValidator readExchangeRateValidator = ReadExchangeRateValidator.getInstance();

    public Object[] findAllExchangeRates() throws SQLException {
       return exchangeRateDao.findAll()
                                .stream()
                                   .map(readExchangeRateMapper::mapFrom)
                                      .toArray();
    }
    public ReadExchangeRateDto findExchangeRate(String baseCode, String targetCode){

        var currenciesByCodes = exchangeRateDao.getCurrenciesByCodes(baseCode, targetCode);
        ValidationResult validationResult = readExchangeRateValidator.isValid(currenciesByCodes);
        if (!validationResult.isValid()){
             throw new  ValidationException(validationResult.getErrors());
        }
        var exchangeRate = exchangeRateDao.findByCodesCurrencies(baseCode, targetCode);
        if (!exchangeRate.isPresent()){
            throw new  ValidationException(List.of(Error.of(SC_NOT_FOUND, "Курс не найден")));
        }

        return readExchangeRateMapper.mapFrom(exchangeRate.get());
    }

    public CourceDto makeExchange(String from, String to, Double amount){

        var currenciesByCodes = exchangeRateDao.getCurrenciesByCodes(from, to);
        ValidationResult validationResult = readExchangeRateValidator.isValid(currenciesByCodes);
        if (!validationResult.isValid()){
            throw new  ValidationException(validationResult.getErrors());
        }

        var fromCurrency = currenciesByCodes.stream().filter(currencyEntity -> currencyEntity.getCode().equals(from)).findFirst().get();
        var toCurrency = currenciesByCodes.stream().filter(currencyEntity -> currencyEntity.getCode().equals(to)).findFirst().get();
        BigDecimal rate;

        var exchangeRate = exchangeRateDao.findByCodesCurrencies(from, to);
        var exchangeRateRevers = exchangeRateDao.findByCodesCurrencies(to, from);
        var crossCource = exchangeRateDao.getCrossCource(from, to);

        if (exchangeRate.isPresent()){
            var ex= exchangeRate.get();
            rate = exchangeRate.get().getRate();
        }

       else if (exchangeRateRevers.isPresent()){
            var exReverce = exchangeRateRevers.get();
            rate = getReverseRate(exReverce.getRate());
        }
       else if (crossCource.isPresent()){
           rate = crossCource.get();
        }
       else {
            throw new ValidationException(List.of(Error.of(409,"Курс не найден")));
        }
       return   CourceDto.builder()
               .fromCurrencyName(fromCurrency.getFullName())
               .fromCurrencyCode(fromCurrency.getCode())
               .toCurrencyName(toCurrency.getFullName())
               .toCurrencyCode(toCurrency.getCode())
               .amount(amount)
               .rate(rate)
               .result(amount * rate.doubleValue())
               .build();
    }

    public CreateExchangeRateDto createExchangeRate(CreateExchangeRateDto exchangeRateDto) throws SQLException {
        ValidationResult validationResult = createExchangeRateValidator.isValid(exchangeRateDto);

        if (!validationResult.isValid()){
            throw new ValidationException(validationResult.getErrors());
        }
        ExchangeRateEntity exchangeRateEntity = createExchangeRateMapper.mapFrom(exchangeRateDto);
        exchangeRateDao.save(exchangeRateEntity);
        return exchangeRateDto;
    }

    @SneakyThrows
    public CreateExchangeRateDto updateExchangeRate(CreateExchangeRateDto createExchangeRateDto){
        String baseCode = createExchangeRateDto.getBaseCurrencyCode(), targetCode = createExchangeRateDto.getTargetCurrencyCode();

        if (exchangeRateDao.findByCodesCurrencies(createExchangeRateDto.getBaseCurrencyCode(), createExchangeRateDto
                .getTargetCurrencyCode()).isPresent()) {
            var exchangeRateEntity = exchangeRateDao.findByCodesCurrencies(baseCode, targetCode).get();
            exchangeRateEntity.setRate(createExchangeRateDto.getRate());
            exchangeRateDao.update(exchangeRateEntity);
        } else {
            var exchangeRateEntity = exchangeRateDao.findByCodesCurrencies(targetCode, baseCode).get();
            exchangeRateEntity.setRate(getReverseRate(createExchangeRateDto.getRate()));
             exchangeRateDao.update(exchangeRateEntity);
        }
        return createExchangeRateDto;
    }

    public boolean isNew(CreateExchangeRateDto exchangeRateDto){
        return exchangeRateDao.findByCodesCurrencies(exchangeRateDto.getBaseCurrencyCode(), exchangeRateDto.getTargetCurrencyCode())
                .isPresent()
            || exchangeRateDao.findByCodesCurrencies(exchangeRateDto.getTargetCurrencyCode(), exchangeRateDto.getBaseCurrencyCode())
                .isPresent();
    }
    public void isValidRequest(String baseCurrencyCode, String targetCurrencycode, String rate){
        var validPostRequest = createExchangeRateValidator.isValidPostRequest(baseCurrencyCode, targetCurrencycode, rate);
        if (!validPostRequest.isValid()){
            throw new ValidationException(validPostRequest.getErrors());
        }
    }
    public void isValidRequest(String сurrencyСodes)  {
        var validGetRequest = readExchangeRateValidator.isValidCurrenciesCodes(сurrencyСodes);
        if (!validGetRequest.isValid()){
            throw new ValidationException(validGetRequest.getErrors());
        }
    }

    public BigDecimal getReverseRate(BigDecimal rate){
        return BigDecimal.valueOf(1/rate.doubleValue()).setScale(6,BigDecimal.ROUND_HALF_UP);
    }
    public static ExchangeRateService getInstance(){return INSTANCE;}
}
