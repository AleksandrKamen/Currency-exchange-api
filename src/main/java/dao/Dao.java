package dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface Dao<K, E> {
    E save(E entity) throws SQLException;
    List<E> findAll() throws SQLException;
    Optional<E> findById(K id) throws SQLException;
    E update(E entity) throws SQLException;
    boolean delete(K id) throws SQLException;
}
