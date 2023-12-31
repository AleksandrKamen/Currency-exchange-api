package mapper.exchange_rate_mapper;

import dao.ExchangeRateDao;
import dto.exchangeRate.CreateExchangeRateDto;
import entity.ExchangeRateEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import mapper.Mapper;

import java.sql.SQLException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateExchangeRateMapper implements Mapper<CreateExchangeRateDto, ExchangeRateEntity> {
   private static final CreateExchangeRateMapper INSTANCE = new CreateExchangeRateMapper();
   private final ExchangeRateDao exchangeRateDao = ExchangeRateDao.getInstance();
   public static CreateExchangeRateMapper getInstance(){return INSTANCE;}
    @Override
    public ExchangeRateEntity mapFrom(CreateExchangeRateDto object) {
        var currencies = exchangeRateDao.getCurrenciesByCodes(object.getBaseCurrencyCode(), object.getTargetCurrencyCode());

        var baseCurrency = currencies.stream()
                .filter(currencyEntity -> currencyEntity.getCode().equals(object.getBaseCurrencyCode()))
                .findFirst().get();

        var targetCurrency = currencies.stream()
                .filter(currencyEntity -> currencyEntity.getCode().equals(object.getTargetCurrencyCode()))
                .findFirst().get();

        return ExchangeRateEntity.builder()
                .baseCurrency(baseCurrency)
                .targetCurrency(targetCurrency)
                .rate(object.getRate())
                .build();
    }
}
