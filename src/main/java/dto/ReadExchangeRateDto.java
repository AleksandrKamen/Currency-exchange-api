package dto;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
public class ReadExchangeRateDto {
    private Integer id;
    private String baseCurrencyName;
    private String targetCurrencyName;
    private String baseCurrencyCode;
    private String targetCurrencyCode;
    private BigDecimal rate;
}
