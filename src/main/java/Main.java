import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dao.CurrencyDao;
import entity.CurrencyEntity;
import service.CurrencyService;

public class Main {
    public static void main(String[] args) throws JsonProcessingException {

//        ObjectMapper mapper = new ObjectMapper();
//        var currencyEntity = CurrencyEntity.builder()
//                .fullName("Ruble")
//                .code("rub")
//                .sign("rubles")
//                .build();
//        System.out.println(mapper.writeValueAsString(currencyEntity));
        var string = CurrencyService.getInstance().readAllCurrencies();
        System.out.println(string);


    }
}
