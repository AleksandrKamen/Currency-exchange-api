package mapper.currency_mapper;

import dto.currency.ReadCurrencyDto;
import entity.CurrencyEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import mapper.Mapper;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReadCurrencyMapper implements Mapper<CurrencyEntity, ReadCurrencyDto> {
private static final ReadCurrencyMapper INSTANCE = new ReadCurrencyMapper();
public static ReadCurrencyMapper getInstance(){return INSTANCE;}
    @Override
    public ReadCurrencyDto mapFrom(CurrencyEntity object) {
        return ReadCurrencyDto.builder()
                .id(object.getId())
                .name(object.getFullName())
                .code(object.getCode())
                .sign(object.getSign())
                .build();
    }
}
