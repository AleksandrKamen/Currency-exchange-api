package mapper.exchange_rate_mapper;

import dto.ExchangeRateDto;
import entity.ExchangeRateEntity;
import mapper.Mapper;

public class CreateExchangeRateMapper implements Mapper<ExchangeRateDto, ExchangeRateEntity> {
    @Override
    public ExchangeRateEntity mapFrom(ExchangeRateDto object) {
        return ExchangeRateEntity.builder()
                .baseCurrencyId(object.getBaseCurrencyId())
                .targetCurrencyId(object.getTargetCurrencyId())
                .rate(object.getRate())
                .build();
    }
}
