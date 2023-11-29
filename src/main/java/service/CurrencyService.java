package service;

import dao.CurrencyDao;
import dto.currency.CreateCurrencyDto;
import dto.currency.ReadCurrencyDto;
import exception.ValidationException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import mapper.currency_mapper.CreateCurrencyMapper;
import mapper.currency_mapper.ReadCurrencyMapper;
import validator.ValidationResult;
import validator.currency.CreateCurrencyValidator;
import validator.currency.ReadCurrencyValidator;

import java.sql.SQLException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CurrencyService {
    private static final CurrencyService INSTANCE = new CurrencyService();
    private final CurrencyDao currencyDao = CurrencyDao.getInstance();
    private final CreateCurrencyMapper createCurrencyMapper = CreateCurrencyMapper.getInstance();
    private final ReadCurrencyMapper readCurrencyMapper = ReadCurrencyMapper.getInstance();
    private final CreateCurrencyValidator createCurrencyValidator = CreateCurrencyValidator.getInstance();
    private final ReadCurrencyValidator readCurrencyValidator = ReadCurrencyValidator.getInstance();

    public Object[] readAllCurrencies() throws SQLException {
        var allCurrencies = currencyDao.findAll();
        return allCurrencies.stream()
                .map(readCurrencyMapper::mapFrom).toArray();
    }
    public ReadCurrencyDto readCurrencyByCode(String code) throws SQLException {
        ValidationResult validationResult = readCurrencyValidator.isValid(code);

        if (!validationResult.isValid()) {
            throw new ValidationException(validationResult.getErrors());
        }
        return currencyDao.findByCode(code)
                .map(readCurrencyMapper::mapFrom).get();
    }

    public CreateCurrencyDto create(CreateCurrencyDto currencyDto) throws SQLException {
        var validationResult = createCurrencyValidator.isValid(currencyDto);

        if (!validationResult.isValid()) {
            throw new ValidationException(validationResult.getErrors());
        }
        var currency = createCurrencyMapper.mapFrom(currencyDto);
        currencyDao.save(currency);
        return currencyDto;
    }
    public static CurrencyService getInstance() {
        return INSTANCE;
    }
}
