import Dao.CurrencyDao;
import entity.CurrencyEntity;
import util.JDBCUtil;

public class Main {
    public static void main(String[] args) {

        CurrencyDao currencyDao = new CurrencyDao();
        CurrencyEntity currency = CurrencyEntity.builder()
                .code("888")
                .sign("8887")
                .fullName("dsadas")
                .build();
        currencyDao.save(currency);



    }
}
