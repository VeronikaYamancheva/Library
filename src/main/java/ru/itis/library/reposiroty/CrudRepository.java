package ru.itis.library.reposiroty;

import ru.itis.library.exceptions.DbException;

import java.util.List;
import java.util.Optional;

public interface CrudRepository<T, K> {
    T save(T item) throws DbException;
    Optional<T> findById(K id) throws DbException;
    List<T> getAll() throws DbException;
    void deleteById(K id) throws DbException;
}
