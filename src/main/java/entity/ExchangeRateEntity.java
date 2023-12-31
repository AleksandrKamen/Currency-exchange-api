package entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder

public class ExchangeRateEntity {

    private Integer id;
    private CurrencyEntity baseCurrency;
    private CurrencyEntity targetCurrency;
    private BigDecimal rate;
}
