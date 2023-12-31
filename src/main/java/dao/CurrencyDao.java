package dao;

import entity.CurrencyEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import util.JDBCUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CurrencyDao implements Dao<Integer, CurrencyEntity> {
   private static final CurrencyDao INSTANCE = new CurrencyDao();
   public static CurrencyDao getInstance(){return INSTANCE;}

    private final String SAVE_CURRENCY_SQL = """
                                             insert into currencies (code, fullName, sign) 
                                             VALUES (?,?,?) returning id
                                             """;
    private final String FIND_ALL_CURRENCIES_SQL = """
                                                select id,code,fullName,sign from currencies
                                                 """;
    private final String FIND_BY_ID_CURRENCY_SQL = """
                                                select id,code,fullName,sign from currencies
                                                where id = ?
                                                 """;
    private final String FIND_BY_CODE_CURRENCY_SQL = """
                                                select id,code,fullName,sign from currencies
                                                where code = ?
                                                 """;
    private final String DELETE_CURRENCY_BY_ID_SQL = """
                                                delete from currencies
                                                where id = ?
                                                 """;
    private final String UPDATE_CURRENCY_BY_ID_SQL = """
                                                update currencies set code = ?, fullName = ?, sign = ? 
                                                where code = ?
                                                 """;
    @Override
    public CurrencyEntity save(CurrencyEntity entity) throws SQLException {
        try (Connection connection = JDBCUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SAVE_CURRENCY_SQL)) {
            preparedStatement.setString(1,entity.getCode());
            preparedStatement.setString(2,entity.getFullName());
            preparedStatement.setString(3,entity.getSign());
            var resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                entity.setId(resultSet.getInt(1));
            }
            return entity;
        }
    }
    @Override
    public List<CurrencyEntity> findAll() throws SQLException {
        ArrayList<CurrencyEntity> currencyEntities = new ArrayList<>();
        try (Connection connection = JDBCUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_CURRENCIES_SQL)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                currencyEntities.add(currencyBuild(resultSet));
            }
            return currencyEntities;
        }
    }
    @Override
    public Optional<CurrencyEntity> findById(Integer id) throws SQLException {
        CurrencyEntity currency = null;
        try (Connection connection = JDBCUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID_CURRENCY_SQL)) {
            preparedStatement.setInt(1,id);
            ResultSet resultSet = preparedStatement.executeQuery();
           if (resultSet.next()){
               currency = currencyBuild(resultSet);
           }
            return Optional.ofNullable(currency);
        }
    }
    public Optional<CurrencyEntity> findByCode(String code) throws SQLException {
        CurrencyEntity currency = null;
        try (Connection connection = JDBCUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_CODE_CURRENCY_SQL)) {
            preparedStatement.setString(1,code);
            ResultSet resultSet = preparedStatement.executeQuery();
           if (resultSet.next()){
               currency = currencyBuild(resultSet);
           }
            return Optional.ofNullable(currency);
        }
    }
    @Override
    public CurrencyEntity update(CurrencyEntity entity) throws SQLException {
        try (Connection connection = JDBCUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_CURRENCY_BY_ID_SQL)) {
            preparedStatement.setString(1,entity.getCode());
            preparedStatement.setString(2,entity.getFullName());
            preparedStatement.setString(3,entity.getSign());
            preparedStatement.setString(4,entity.getCode());
            preparedStatement.executeUpdate();
            return entity;
        }
    }
     @Override
    public boolean delete(Integer id) throws SQLException {
        try (Connection connection = JDBCUtil.getConnection();
           PreparedStatement preparedStatement = connection.prepareStatement(DELETE_CURRENCY_BY_ID_SQL)) {
           preparedStatement.setInt(1,id);
           return preparedStatement.executeUpdate() > 0;
        }
    }
    private static CurrencyEntity currencyBuild(ResultSet resultSet) throws SQLException {
       return CurrencyEntity.builder()
                .id(resultSet.getInt("id"))
                .code(resultSet.getString("code"))
                .fullName(resultSet.getString("fullName"))
                .sign(resultSet.getString("sign"))
                .build();
    }
}
