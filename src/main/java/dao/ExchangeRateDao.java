package dao;

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
                                                select id,baseCurrencyId, targetCurrencyId, rate from exchangerates
                                                 """;
    private final String FIND_BY_ID_EXCHANGERATE_SQL = """
                                                select id,baseCurrencyId, targetCurrencyId, rate from exchangerates
                                                where id = ?
                                                 """;
    private final String FIND_EXCHANGERATE_BY_CODE_CURRENCYS_SQL = """
                                                select id,baseCurrencyId, targetCurrencyId, rate from exchangerates
                                                where baseCurrencyId = (select currencies.id from currencies where code = ?) and  targetCurrencyId = (select currencies.id from currencies where code = ?)
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
                                                where id = ?
                                                 """;
    private final String UPDATE_EXCHANGERATE_BY_CODE_CURRENCYS_SQL = """
                                                update exchangerates set rate = ?
                                                where baseCurrencyId = (select id from currencies where code = ?) and targetCurrencyId = (select id from currencies where code = ?)
                                                 """;
    @SneakyThrows
    @Override
    public ExchangeRateEntity save(ExchangeRateEntity entity) {
        try (Connection connection = JDBCUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SAVE_EXCHANGERATE_SQL)) {
            preparedStatement.setInt(1,entity.getBaseCurrencyId());
            preparedStatement.setInt(2,entity.getTargetCurrencyId());
            preparedStatement.setBigDecimal(3,entity.getRate());
            preparedStatement.executeUpdate();
            return entity;
        }
    }
    @SneakyThrows
    @Override
    public List<ExchangeRateEntity> findAll() {
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
    public ExchangeRateEntity update(ExchangeRateEntity entity, Integer id) {
        try (Connection connection = JDBCUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_EXCHANGERATE_BY_ID_SQL)) {
            preparedStatement.setBigDecimal(1,entity.getRate());
            preparedStatement.setInt(2,id);
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
        return ExchangeRateEntity.builder()
                .id(resultSet.getInt("id"))
                .baseCurrencyId(resultSet.getInt("baseCurrencyId"))
                .targetCurrencyId(resultSet.getInt("targetCurrencyId"))
                .rate(resultSet.getBigDecimal("rate"))
                .build();
    }
}
