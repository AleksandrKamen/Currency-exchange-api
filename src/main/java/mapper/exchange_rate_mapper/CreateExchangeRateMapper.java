package mapper.exchange_rate_mapper;

import dao.CurrencyDao;
import dto.ExchangeRateDto;
import entity.ExchangeRateEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import mapper.Mapper;
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateExchangeRateMapper implements Mapper<ExchangeRateDto, ExchangeRateEntity> {
    private static final CreateExchangeRateMapper INSTANCE = new CreateExchangeRateMapper();

   private final CurrencyDao currencyDao = CurrencyDao.getInstance();

    public static CreateExchangeRateMapper getInstance(){return INSTANCE;}


    @Override
    public ExchangeRateEntity mapFrom(ExchangeRateDto object) {
        return ExchangeRateEntity.builder()
                .baseCurrencyId(currencyDao.findByCode(object.getBaseCurrency()).get())
                .targetCurrencyId(currencyDao.findByCode(object.getTargetCurrency()).get())
                .rate(object.getRate())
                .build();
    }
}
