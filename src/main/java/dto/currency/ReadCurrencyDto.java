package dto.currency;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ReadCurrencyDto {
    private Integer id;
    private String code;
    private String name;
    private String sign;




}
