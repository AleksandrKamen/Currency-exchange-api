package entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Builder
@Table(name = "currencies")
public class CurrenciesEntity {
    @Id
    private Integer id;
    @Column(nullable = false, unique = true)
    private String code;
    @Column(nullable = false, unique = true)
    private String fullName;
    @Column(nullable = false, unique = true)
    private String sign;

    @OneToMany(mappedBy = "baseCurrency", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ExchangeRatesEntity> exchangeRatesB= new ArrayList<>();

    @OneToMany(mappedBy = "targetCurrency", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ExchangeRatesEntity> exchangeRatesT = new ArrayList<>();



}
