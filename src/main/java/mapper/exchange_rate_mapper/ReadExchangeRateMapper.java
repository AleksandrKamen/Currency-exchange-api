package mapper.exchange_rate_mapper;

import dto.exchangeRate.ReadExchangeRateDto;
import entity.ExchangeRateEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import mapper.Mapper;
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReadExchangeRateMapper implements Mapper<ExchangeRateEntity, ReadExchangeRateDto> {
    private static final ReadExchangeRateMapper INSTANCE = new ReadExchangeRateMapper();

    public static ReadExchangeRateMapper getInstance(){return INSTANCE;}
    @Override
    public ReadExchangeRateDto mapFrom(ExchangeRateEntity object) {
        return ReadExchangeRateDto.builder()
                .id(object.getId())
                .baseCurrencyName(object.getBaseCurrencyId().getFullName())
                .targetCurrencyName(object.getTargetCurrencyId().getFullName())
                .baseCurrencyCode(object.getBaseCurrencyId().getCode())
                .targetCurrencyCode(object.getTargetCurrencyId().getCode())
                .rate(object.getRate())
                .build();
    }
}
