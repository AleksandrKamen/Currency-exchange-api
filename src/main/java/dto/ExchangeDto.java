package dto;

import entity.CurrencyEntity;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
public class ExchangeDto {
    private CurrencyEntity baseCurrency;
    private CurrencyEntity targetCurrency;
    private BigDecimal rate;
    private BigDecimal amount;
    private BigDecimal result;
}
