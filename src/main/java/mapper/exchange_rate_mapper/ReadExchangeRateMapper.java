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
                .baseCurrency(object.getBaseCurrency())
                .targetCurrency(object.getTargetCurrency())
                .rate(object.getRate())
                .build();
    }
}
