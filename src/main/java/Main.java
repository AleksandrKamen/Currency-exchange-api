import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dao.CurrencyDao;
import dao.ExchangeRateDao;
import dto.CurrencyDto;
import entity.CurrencyEntity;
import entity.ExchangeRateEntity;
import service.CurrencyService;
import service.ExchangeRateService;

import java.util.List;
import java.util.Optional;

public class Main {
    public static void main(String[] args) throws JsonProcessingException {

        var exchangeRateDao = ExchangeRateDao.getInstance();
//        var exchangeRateDaoById = exchangeRateDao.findById(1);
//        System.out.println(exchangeRateDaoById.get().getBaseCurrencyId().toString());
//        System.out.println(exchangeRateDaoById.get().getTargetCurrencyId().toString());
        var all = exchangeRateDao.findAll();
        for (ExchangeRateEntity exchangeRateEntity : all) {
            System.out.println(exchangeRateEntity);
        }


    }
}
