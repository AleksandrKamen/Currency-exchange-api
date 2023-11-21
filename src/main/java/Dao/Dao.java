package Dao;

import java.util.List;
import java.util.Optional;

public interface Dao<K, E> {
    E save(E entity);
    List<E>findAll();
    Optional<E> findById(K id);
    E update(E entity);
    boolean delete(K id);






}