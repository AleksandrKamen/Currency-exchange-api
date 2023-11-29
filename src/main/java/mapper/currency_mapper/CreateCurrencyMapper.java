package mapper.currency_mapper;

import dto.currency.CreateCurrencyDto;
import entity.CurrencyEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import mapper.Mapper;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateCurrencyMapper implements Mapper<CreateCurrencyDto, CurrencyEntity> {
    private static final CreateCurrencyMapper INSTANCE = new CreateCurrencyMapper();
    public static CreateCurrencyMapper getInstance(){return INSTANCE;}
    @Override
    public CurrencyEntity mapFrom(CreateCurrencyDto object) {
        return CurrencyEntity.builder()
                .code(object.getCode())
                .fullName(object.getName())
                .sign(object.getSign())
                .build();
    }
}
