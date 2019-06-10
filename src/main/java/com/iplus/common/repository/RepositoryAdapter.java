package com.iplus.common.repository;

import com.iplus.common.data.GroupCount;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class RepositoryAdapter<D, Q, I extends Serializable> implements BaseRepository<D, Q, I> {

    @Override
    public D findById(I id) {
        throw new NotImplementedException("Not Implemented");
    }

    @Override
    public D save(D entity) {
        throw new NotImplementedException("Not Implemented");
    }

    @Override
    public List<D> find(Q query) {
        return find(query, null);
    }

    @Override
    public List<D> find(Q query, Pageable pageable) {
        throw new NotImplementedException("Not Implemented");
    }

    @Override
    public List<I> findIds(Q query, Pageable pageable) {
        throw new NotImplementedException("Not Implemented");
    }

    @Override
    public Optional<D> findOne(Q query) {
        throw new NotImplementedException("Not Implemented");
    }

    @Override
    public long count(Q query) {
        throw new NotImplementedException("Not Implemented");
    }

    @Override
    public <T> List<GroupCount<T>> groupByField(Q query, String field, Pageable pageable) {
        throw new NotImplementedException("Not Implemented");
    }

    @Override
    public <T> List<GroupCount<T>> groupByField(Q query, String field, Pageable pageable,
                                                FieldGroupFilter filter) {
        throw new NotImplementedException("Not Implemented");
    }

    @Override
    public long countGroup(Q query, String field) {
        throw new NotImplementedException("Not Implemented");
    }

    @Override
    public long countGroup(Q query, String field, FieldGroupFilter filter) {
        throw new NotImplementedException("Not Implemented");
    }

    @Override
    public int delete(I id) {
        throw new NotImplementedException("Not Implemented");
    }

    @Override
    public int deleteMany(Q query) {
        throw new NotImplementedException("Not Implemented");
    }

    @Override
    public boolean exists(I id) {
        throw new NotImplementedException("Not Implemented");
    }

    @Override
    public void update(Q query, Map<String, Object> fieldAndValue) {
        throw new NotImplementedException("Not Implemented");
    }

    @Override
    public <K, V> List<Map<K, V>> findOptionalField(Q query, Pageable pageable, List<String> fields) {
        throw new NotImplementedException("Not Implemented");
    }

}
