package dto;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
public class CourceDto {
    private String fromCurrencyName;
    private String toCurrencyName;
    private String fromCurrencyCode;
    private String toCurrencyCode;
    private BigDecimal rate;
    private Double amount;
    private Double result;


}
