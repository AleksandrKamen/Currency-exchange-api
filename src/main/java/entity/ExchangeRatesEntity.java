package entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Builder
@Table(name = "exchangerates")
public class ExchangeRatesEntity {
    @Id
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "baseCurrencyId")
    private CurrenciesEntity baseCurrency;
    @ManyToOne
    @JoinColumn(name = "targetCurrencyId")
    private CurrenciesEntity targetCurrency;
    private BigDecimal rate;

}
