package mapper.exchange_rate_mapper;

import dto.ExchangeRateDto;
import entity.ExchangeRateEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import mapper.Mapper;
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReadExchangeRateMapper implements Mapper<ExchangeRateEntity, ExchangeRateDto> {
    private static final ReadExchangeRateMapper INSTANCE = new ReadExchangeRateMapper();

    public static ReadExchangeRateMapper getInstance(){return INSTANCE;}
    @Override
    public ExchangeRateDto mapFrom(ExchangeRateEntity object) {
        return ExchangeRateDto.builder()
                .id(object.getId())
                .baseCurrency(object.getBaseCurrencyId().getFullName())
                .targetCurrency(object.getTargetCurrencyId().getFullName())
                .rate(object.getRate())
                .build();
    }
}
