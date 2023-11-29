package dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface Dao<K, E> {
    E save(E entity) throws SQLException;
    List<E> findAll() throws SQLException;
    Optional<E> findById(K id);
    E update(E entity);
    boolean delete(K id);
}
