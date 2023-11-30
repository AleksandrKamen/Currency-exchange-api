package service;

import dao.CurrencyDao;
import dao.ExchangeRateDao;
import dto.ExchangeDto;
import dto.exchangeRate.CreateExchangeRateDto;
import dto.exchangeRate.ReadExchangeRateDto;
import entity.CurrencyEntity;
import entity.ExchangeRateEntity;
import exception.ValidationException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import mapper.exchange_rate_mapper.CreateExchangeRateMapper;
import mapper.exchange_rate_mapper.ReadExchangeRateMapper;

import validator.Error;
import validator.ExchangeValidator;
import validator.ValidationResult;
import validator.exchangerate.CreateExchangeRateValidator;
import validator.exchangerate.ReadExchangeRateValidator;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
    private final ExchangeValidator exchangeValidator = ExchangeValidator.getInstance();

    public Object[] findAllExchangeRates() throws SQLException {
        return exchangeRateDao.findAll()
                .stream()
                .map(readExchangeRateMapper::mapFrom)
                .toArray();
    }

    public ReadExchangeRateDto findExchangeRate(String baseCode, String targetCode) {

        var currenciesByCodes = exchangeRateDao.getCurrenciesByCodes(baseCode, targetCode);
        ValidationResult validationResult = readExchangeRateValidator.isValid(currenciesByCodes);
        if (!validationResult.isValid()) {
            throw new ValidationException(validationResult.getErrors());
        }
        var exchangeRate = exchangeRateDao.findByCodesCurrencies(baseCode, targetCode);
        if (!exchangeRate.isPresent()) {
            throw new ValidationException(List.of(Error.of(SC_NOT_FOUND, "Курс не найден")));
        }
        return readExchangeRateMapper.mapFrom(exchangeRate.get());
    }

    public ExchangeDto makeExchange(String from, String to, String amount) throws SQLException {

        var validationResult = exchangeValidator.isValid(from, to, amount);
        if (!validationResult.isValid()) {
            throw new ValidationException(validationResult.getErrors());
        }
        CurrencyEntity baseCurrency = null;
        CurrencyEntity targetCurrency = null;
        BigDecimal rate = null;

        var exchangeRate = exchangeRateDao.findByCodesCurrencies(from, to);
        var exchangeRateRevers = exchangeRateDao.findByCodesCurrencies(to, from);
        var crossCource = exchangeRateDao.getCrossCource(from, to);

        if (exchangeRate.isPresent()) {
            baseCurrency = exchangeRate.get().getBaseCurrency();
            targetCurrency = exchangeRate.get().getTargetCurrency();
            rate = exchangeRate.get().getRate();
        } else if (exchangeRateRevers.isPresent()) {
            baseCurrency = exchangeRateRevers.get().getTargetCurrency();
            targetCurrency = exchangeRateRevers.get().getBaseCurrency();
            rate = getReverseRate(exchangeRateRevers.get().getRate());
        } else if (crossCource.isPresent()) {
            var currencies = exchangeRateDao.getCurrenciesByCodes(from, to);
            baseCurrency = currencies.stream().filter(currencyEntity -> currencyEntity.getCode().equals(from)).findFirst().get();
            targetCurrency = currencies.stream().filter(currencyEntity -> currencyEntity.getCode().equals(to)).findFirst().get();
            rate = crossCource.get();
        }

        return ExchangeDto.builder()
                .baseCurrency(baseCurrency)
                .targetCurrency(targetCurrency)
                .amount(new BigDecimal(amount))
                .rate(rate.setScale(2, RoundingMode.UP))
                .result((new BigDecimal(amount).multiply(rate)).setScale(2, RoundingMode.UP))
                .build();
    }

    public ReadExchangeRateDto createExchangeRate(CreateExchangeRateDto exchangeRateDto) throws SQLException {
        ValidationResult validationResult = createExchangeRateValidator.isValid(exchangeRateDto);

        if (!validationResult.isValid()) {
            throw new ValidationException(validationResult.getErrors());
        }
        ExchangeRateEntity exchangeRateEntity = createExchangeRateMapper.mapFrom(exchangeRateDto);
        var saveEntity = exchangeRateDao.save(exchangeRateEntity);

        return readExchangeRateMapper.mapFrom(saveEntity);
    }

    public ReadExchangeRateDto updateExchangeRate(CreateExchangeRateDto createExchangeRateDto) throws SQLException {

        var validationResult = createExchangeRateValidator.isValid(createExchangeRateDto);
        if (!validationResult.isValid()) {
            throw new ValidationException(validationResult.getErrors());
        }
        if (!exchangeRateDao.findByCodesCurrencies(createExchangeRateDto.getBaseCurrencyCode(), createExchangeRateDto.getTargetCurrencyCode()).isPresent()) {
            throw new ValidationException(List.of(Error.of(SC_NOT_FOUND, "Курс не найден")));
        }
        var exchangeRate = createExchangeRateMapper.mapFrom(createExchangeRateDto);
        var updateExchangeRate = exchangeRateDao.update(exchangeRate);
        var readExchangeRateDto = readExchangeRateMapper.mapFrom(updateExchangeRate);

        return readExchangeRateDto;
    }

    public void isValidRequest(String baseCurrencyCode, String targetCurrencycode, String rate) {
        var validPostRequest = createExchangeRateValidator.isValidRequest(baseCurrencyCode, targetCurrencycode, rate);
        if (!validPostRequest.isValid()) {
            throw new ValidationException(validPostRequest.getErrors());
        }
    }

    public void isValidRequest(String сurrencyСodes) {
        var validGetRequest = readExchangeRateValidator.isValidCurrenciesCodes(сurrencyСodes);
        if (!validGetRequest.isValid()) {
            throw new ValidationException(validGetRequest.getErrors());
        }
    }

    public BigDecimal getReverseRate(BigDecimal rate) {
        return BigDecimal.valueOf(1 / rate.doubleValue()).setScale(6, BigDecimal.ROUND_HALF_UP);
    }

    public static ExchangeRateService getInstance() {return INSTANCE;}
}
