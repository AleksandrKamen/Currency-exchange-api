package dto;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
public class ExchangeRateDto {
    private Integer id;
    private String baseCurrency;
    private String targetCurrency;
    private BigDecimal rate;
}
