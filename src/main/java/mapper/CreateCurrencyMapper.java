package mapper;

import dto.CurrencyDto;
import entity.CurrencyEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateCurrencyMapper implements Mapper<CurrencyDto, CurrencyEntity>{
    private static final CreateCurrencyMapper INSTANCE = new CreateCurrencyMapper();
    public static CreateCurrencyMapper getInstance(){return INSTANCE;}


    @Override
    public CurrencyEntity mapFrom(CurrencyDto object) {
        return CurrencyEntity.builder()
                .code(object.getCode())
                .fullName(object.getFullName())
                .sign(object.getSign())
                .build();
    }
}
