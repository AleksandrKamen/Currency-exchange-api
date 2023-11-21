package Dao;

import entity.CurrencyEntity;
import lombok.SneakyThrows;
import util.JDBCUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class CurrencyDao implements Dao<Integer, CurrencyEntity> {

    private final String SAVE_CURRENCY_SQL = """
                                             insert into currencies (code, fullName, sign) 
                                             VALUES (?,?,?) 
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

    @Override
    public List<CurrencyEntity> findAll() {
        return Collections.emptyList();
    }

    @Override
    public Optional<CurrencyEntity> findById(Integer id) {
        return null;
    }

    @Override
    public CurrencyEntity update(CurrencyEntity entity) {
        return null;
    }

    @Override
    public boolean delete(Integer id) {
        return true;

    }
}
