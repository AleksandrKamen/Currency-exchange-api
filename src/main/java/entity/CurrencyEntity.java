package entity;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder

public class CurrencyEntity {

    private Integer id;
    private String code;
    private String fullName;
    private String sign;
}
