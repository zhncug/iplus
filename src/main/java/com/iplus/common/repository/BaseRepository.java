package com.iplus.common.repository;

import com.iplus.common.data.GroupCount;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface BaseRepository<D, Q, I extends Serializable> {
    D findById(I id);

    D save(D entity);

    List<D> find(Q query);

    List<D> find(Q query, Pageable pageable);

    List<I> findIds(Q query, Pageable pageable);

    Optional<D> findOne(Q query);

    long count(Q query);

    <T> List<GroupCount<T>> groupByField(Q query, String field, Pageable pageable);

    <T> List<GroupCount<T>> groupByField(Q query, String field, Pageable pageable,
                                         FieldGroupFilter filter);

    long countGroup(Q query, String field);

    long countGroup(Q query, String field, FieldGroupFilter filter);

    int delete(I id);

    int deleteMany(Q query);

    boolean exists(I id);

    void update(Q query, Map<String, Object> fieldAndValue);

    <K, V> List<Map<K, V>> findOptionalField(Q query, Pageable pageable, List<String> fields);
}
