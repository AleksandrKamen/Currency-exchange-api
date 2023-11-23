package dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CurrencyDto {
    private Integer id;
    private String code;
    private String name;
    private String sign;
}
