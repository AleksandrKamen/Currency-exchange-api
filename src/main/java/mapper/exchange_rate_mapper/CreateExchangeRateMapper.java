package mapper.exchange_rate_mapper;

import dao.CurrencyDao;
import dto.CreateExchangeRateDto;
import dto.ReadExchangeRateDto;
import entity.ExchangeRateEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import mapper.Mapper;
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateExchangeRateMapper implements Mapper<CreateExchangeRateDto, ExchangeRateEntity> {
    private static final CreateExchangeRateMapper INSTANCE = new CreateExchangeRateMapper();

   private final CurrencyDao currencyDao = CurrencyDao.getInstance();

    public static CreateExchangeRateMapper getInstance(){return INSTANCE;}


    @Override
    public ExchangeRateEntity mapFrom(CreateExchangeRateDto object) {
        return ExchangeRateEntity.builder()
                .baseCurrencyId(currencyDao.findByCode(object.getBaseCurrencyCode()).get())
                .targetCurrencyId(currencyDao.findByCode(object.getTargetCurrencyCode()).get())
                .rate(object.getRate())
                .build();
    }
}
