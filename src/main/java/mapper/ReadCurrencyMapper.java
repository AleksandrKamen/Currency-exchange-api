package mapper;

import dto.CurrencyDto;
import entity.CurrencyEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReadCurrencyMapper implements Mapper<CurrencyEntity, CurrencyDto> {
private static final ReadCurrencyMapper INSTANCE = new ReadCurrencyMapper();
public static ReadCurrencyMapper getInstance(){return INSTANCE;}

    @Override
    public CurrencyDto mapFrom(CurrencyEntity object) {
        return CurrencyDto.builder()
                .id(object.getId())
                .fullName(object.getFullName())
                .code(object.getCode())
                .sign(object.getSign())
                .build();
    }
}
