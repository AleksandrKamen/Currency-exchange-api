package dao;

import entity.CurrencyEntity;
import entity.ExchangeRateEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import util.JDBCUtil;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExchangeRateDao implements Dao<Integer, ExchangeRateEntity> {
   private static final ExchangeRateDao INSTANCE = new ExchangeRateDao();
   public  static ExchangeRateDao getInstance(){return INSTANCE;}
    private final String SAVE_EXCHANGERATE_SQL = """
                                             insert into exchangerates (baseCurrencyId, targetCurrencyId, rate) 
                                             VALUES (?,?,?) 
                                             """;
    private final String FIND_ALL_EXCHANGERATES_SQL = """
                                                select exchangerates.id as id, 
                                                c.id as base_id, c.code as base_code, c.fullName as base_name, c.sign as base_sign, 
                                                c2.id as target_id,c2.code as target_code, c2.fullName as target_name, c2.sign as target_sign, rate 
                                                from exchangerates
                                                JOIN currencies c on c.id = exchangerates.baseCurrencyId
                                                join currencies c2 on c2.id = exchangerates.targetCurrencyId
                                                 """;
    private final String FIND_BY_ID_EXCHANGERATE_SQL = """
                                                select exchangerates.id as id, 
                                                c.id as base_id, c.code as base_code, c.fullName as base_name, c.sign as base_sign, 
                                                c2.id as target_id,c2.code as target_code, c2.fullName as target_name, c2.sign as target_sign, rate 
                                                from exchangerates
                                                JOIN currencies c on c.id = exchangerates.baseCurrencyId
                                                join currencies c2 on c2.id = exchangerates.targetCurrencyId
                                                where exchangerates.id = ?
                                                 """;
    private final String FIND_EXCHANGERATE_BY_CODE_CURRENCYS_SQL = """
                                                select exchangerates.id as id, 
                                                c.id as base_id, c.code as base_code, c.fullName as base_name, c.sign as base_sign, 
                                                c2.id as target_id,c2.code as target_code, c2.fullName as target_name, c2.sign as target_sign, rate 
                                                from exchangerates
                                                JOIN currencies c on c.id = exchangerates.baseCurrencyId
                                                join currencies c2 on c2.id = exchangerates.targetCurrencyId
                                                where base_code = ? and target_code = ?
                                                 """;
    private final String FIND_CROSS_COURCE_SQL = """
                                                select round(e2.rate*1.0/e1.rate,6) as cource  from
                                                (select  c.code, rate
                                                from exchangerates
                                                JOIN currencies c on c.id = exchangerates.baseCurrencyId
                                                join currencies c2 on c2.id = exchangerates.targetCurrencyId
                                                where c2.code = ?) as e1
                                                join
                                                (select c.code, rate
                                                from exchangerates
                                                JOIN currencies c on c.id = exchangerates.baseCurrencyId
                                                 join currencies c2 on c2.id = exchangerates.targetCurrencyId
                                                 where c2.code = ?) as e2 where e1.code = e2.code limit 1
                                                 """;
    private final String DELETE_EXCHANGERATE_BY_ID_SQL = """
                                                delete from exchangerates
                                                where id = ?
                                                 """;
    private final String DELETE_EXCHANGERATE_BY_CODES_CURRENCIES_SQL = """
                                                delete from exchangerates
                                                where baseCurrencyId = (select id from currencies where code = ?) and targetCurrencyId = (select id from currencies where code = ?)
                                                 """;
    private final String UPDATE_EXCHANGERATE_BY_ID_SQL = """
                                                update exchangerates set rate = ?
                                                where baseCurrencyId = ? and targetCurrencyId = ?
                                                 """;
    private final String UPDATE_EXCHANGERATE_BY_CODE_CURRENCYS_SQL = """
                                                update exchangerates set rate = ?
                                                where baseCurrencyId = (select id from currencies where code = ?) and targetCurrencyId = (select id from currencies where code = ?)
                                                 """;
    private final String SELECT_CURRENCYS_BY_CODE_SQL = """
                                                 select id, code, fullName, sign from currencies
                                                 where code = ? or code = ?
                                                 """;
    @SneakyThrows
    public List<CurrencyEntity> getCurrenciesByCodes(String from, String to){
        var currencyEntities = new ArrayList<CurrencyEntity>();
        try (Connection connection = JDBCUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_CURRENCYS_BY_CODE_SQL)) {
            preparedStatement.setString(1,from);
            preparedStatement.setString(2,to);
            var resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
               currencyEntities.add(CurrencyEntity.builder()
                        .id(resultSet.getInt("id"))
                        .code(resultSet.getString("code"))
                        .fullName(resultSet.getString("fullName"))
                        .sign(resultSet.getString("sign"))
                        .build());
            }
            return currencyEntities;
        }
    }
    @SneakyThrows
    public Optional<BigDecimal> getCrossCource(String from, String to) {
        try (Connection connection = JDBCUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_CROSS_COURCE_SQL)) {
            preparedStatement.setString(1,from);
            preparedStatement.setString(2,to);
            var resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return Optional.ofNullable(resultSet.getBigDecimal("cource"));
        }
    }

    @Override
    public ExchangeRateEntity save(ExchangeRateEntity entity) throws SQLException {
        try (Connection connection = JDBCUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SAVE_EXCHANGERATE_SQL)) {
            preparedStatement.setInt(1,entity.getBaseCurrency().getId());
            preparedStatement.setInt(2,entity.getTargetCurrency().getId());
            preparedStatement.setBigDecimal(3,entity.getRate());
            preparedStatement.executeUpdate();
            return entity;
        }
    }

    @Override
    public List<ExchangeRateEntity> findAll() throws SQLException {
        ArrayList<ExchangeRateEntity> exchangeRateEntities = new ArrayList<>();
        try (Connection connection = JDBCUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_EXCHANGERATES_SQL)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                exchangeRateEntities.add(exchangeRateBuild(resultSet));
            }
            return exchangeRateEntities;
        }
    }
    @SneakyThrows
    @Override
    public Optional<ExchangeRateEntity> findById(Integer id) {
        ExchangeRateEntity exchangeRateEntity = null;
        try (Connection connection = JDBCUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID_EXCHANGERATE_SQL)) {
            preparedStatement.setInt(1,id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                exchangeRateEntity = exchangeRateBuild(resultSet);
            }
            return Optional.ofNullable(exchangeRateEntity);
        }
    }
    @SneakyThrows
    public Optional<ExchangeRateEntity> findByCodesCurrencies(String codeBase, String codeTarget) {
        ExchangeRateEntity exchangeRateEntity = null;
        try (Connection connection = JDBCUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_EXCHANGERATE_BY_CODE_CURRENCYS_SQL)) {
            preparedStatement.setString(1,codeBase);
            preparedStatement.setString(2,codeTarget);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                exchangeRateEntity = exchangeRateBuild(resultSet);
            }
            return Optional.ofNullable(exchangeRateEntity);
        }
    }
    @SneakyThrows
    @Override
    public ExchangeRateEntity update(ExchangeRateEntity entity) {
        try (Connection connection = JDBCUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_EXCHANGERATE_BY_ID_SQL)) {
            preparedStatement.setBigDecimal(1,entity.getRate());
            preparedStatement.setInt(2,entity.getBaseCurrency().getId());
            preparedStatement.setInt(3, entity.getTargetCurrency().getId());
            preparedStatement.executeUpdate();
            return entity;
        }
    }
    @SneakyThrows
    public Optional<ExchangeRateEntity> updateByCodesCurrencies(BigDecimal rate, String codeBase, String codeTarget) {
        try (Connection connection = JDBCUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_EXCHANGERATE_BY_CODE_CURRENCYS_SQL)) {
            preparedStatement.setBigDecimal(1,rate);
            preparedStatement.setString(2,codeBase);
            preparedStatement.setString(3,codeTarget);
            preparedStatement.executeUpdate();
            return findByCodesCurrencies(codeBase,codeTarget);
        }
    }
    @SneakyThrows
    @Override
    public boolean delete(Integer id) {
        try (Connection connection = JDBCUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_EXCHANGERATE_BY_ID_SQL)) {
            preparedStatement.setInt(1,id);
            return preparedStatement.executeUpdate() > 0;
        }
    }
    @SneakyThrows
    public boolean deleteByCodesCurrency(String codeBase, String codeTarget) {
        try (Connection connection = JDBCUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_EXCHANGERATE_BY_CODES_CURRENCIES_SQL)) {
            preparedStatement.setString(1,codeBase);
            preparedStatement.setString(2,codeTarget);
            return preparedStatement.executeUpdate() > 0;
        }
    }
    private ExchangeRateEntity exchangeRateBuild(ResultSet resultSet) throws SQLException {

        var baseCurrency = CurrencyEntity.builder()
                .id(resultSet.getInt("base_id"))
                .code(resultSet.getString("base_code"))
                .fullName(resultSet.getString("base_name"))
                .sign(resultSet.getString("base_sign"))
                .build();
        var targetCurrency = CurrencyEntity.builder()
                .id(resultSet.getInt("target_id"))
                .code(resultSet.getString("target_code"))
                .fullName(resultSet.getString("target_name"))
                .sign(resultSet.getString("target_sign"))
                .build();

        return ExchangeRateEntity.builder()
                .id(resultSet.getInt("id"))
                .baseCurrency(baseCurrency)
                .targetCurrency(targetCurrency)
                .rate(resultSet.getBigDecimal("rate"))
                .build();
    }
}
