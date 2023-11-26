package dto;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
public class CreateExchangeRateDto {
    private String baseCurrencyCode;
    private String targetCurrencyCode;
    private BigDecimal rate;
}
