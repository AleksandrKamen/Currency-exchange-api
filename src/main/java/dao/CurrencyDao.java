package dao;

import entity.CurrencyEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import util.JDBCUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CurrencyDao implements Dao<Integer, CurrencyEntity> {
   private static final CurrencyDao INSTANCE = new CurrencyDao();
   public static CurrencyDao getInstance(){return INSTANCE;}

    private final String SAVE_CURRENCY_SQL = """
                                             insert into currencies (code, fullName, sign) 
                                             VALUES (?,?,?) 
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
    private final String FIND_BY_NAME_CURRENCY_SQL = """
                                                select id,code,fullName,sign from currencies
                                                where fullName = ?
                                                 """;
    private final String FIND_BY_SIGN_CURRENCY_SQL = """
                                                select id,code,fullName,sign from currencies
                                                where sign = ?
                                                 """;
    private final String DELETE__CURRENCY_BY_ID_SQL = """
                                                delete from currencies
                                                where id = ?
                                                 """;
    private final String DELETE__CURRENCY_BY_CODE_SQL = """
                                                delete from currencies
                                                where code = ?
                                                 """;
    private final String UPDATE_CURRENCY_BY_ID_SQL = """
                                                update currencies set code = ?, fullName = ?, sign = ? 
                                                where id = ?
                                                 """;


    @SneakyThrows
    @Override
    public CurrencyEntity save(CurrencyEntity entity) {
        try (Connection connection = JDBCUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SAVE_CURRENCY_SQL)) {
            preparedStatement.setString(1,entity.getCode());
            preparedStatement.setString(2,entity.getFullName());
            preparedStatement.setString(3,entity.getSign());
            preparedStatement.executeUpdate();
            return entity;
        }
    }
    @SneakyThrows
    @Override
    public List<CurrencyEntity> findAll() {
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


    @SneakyThrows
    @Override
    public Optional<CurrencyEntity> findById(Integer id) {
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
    @SneakyThrows
    public Optional<CurrencyEntity> findByCode(String code) {
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
    @SneakyThrows
    public Optional<CurrencyEntity> findByName(String name) {
        CurrencyEntity currency = null;
        try (Connection connection = JDBCUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_NAME_CURRENCY_SQL)) {
            preparedStatement.setString(1,name);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                currency = currencyBuild(resultSet);
            }
            return Optional.ofNullable(currency);

        }
    }    @SneakyThrows
    public Optional<CurrencyEntity> findBySign(String sign) {
        CurrencyEntity currency = null;
        try (Connection connection = JDBCUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_SIGN_CURRENCY_SQL)) {
            preparedStatement.setString(1,sign);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                currency = currencyBuild(resultSet);
            }
            return Optional.ofNullable(currency);

        }
    }
    @SneakyThrows
    @Override
    public CurrencyEntity update(CurrencyEntity entity, Integer id) {
        try (Connection connection = JDBCUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_CURRENCY_BY_ID_SQL)) {
            preparedStatement.setString(1,entity.getCode());
            preparedStatement.setString(2,entity.getFullName());
            preparedStatement.setString(3,entity.getSign());
            preparedStatement.setInt(4,id);
            preparedStatement.executeUpdate();
            return entity;
        }
    }
    @SneakyThrows
    @Override
    public boolean delete(Integer id) {
        try (Connection connection = JDBCUtil.getConnection();
           PreparedStatement preparedStatement = connection.prepareStatement(DELETE__CURRENCY_BY_ID_SQL)) {
           preparedStatement.setInt(1,id);
           return preparedStatement.executeUpdate() > 0;
        }

    }
    @SneakyThrows
    public boolean deleteByCode(String code) {
        try (Connection connection = JDBCUtil.getConnection();
           PreparedStatement preparedStatement = connection.prepareStatement(DELETE__CURRENCY_BY_CODE_SQL)) {
           preparedStatement.setString(1,code);
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
