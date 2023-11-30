package dto.exchangeRate;

import entity.CurrencyEntity;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
public class ReadExchangeRateDto {
    private Integer id;

    private CurrencyEntity baseCurrency;
    private CurrencyEntity targetCurrency;

    private BigDecimal rate;
}
