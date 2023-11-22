package mapper.exchange_rate_mapper;

import dto.ExchangeRateDto;
import entity.ExchangeRateEntity;
import mapper.Mapper;

public class ReadExchangeRateMapper implements Mapper<ExchangeRateEntity, ExchangeRateDto> {
    @Override
    public ExchangeRateDto mapFrom(ExchangeRateEntity object) {
        return ExchangeRateDto.builder()
                .id(object.getId())
                .baseCurrencyId(object.getBaseCurrencyId())
                .targetCurrencyId(object.getTargetCurrencyId())
                .rate(object.getRate())
                .build();
    }
}
