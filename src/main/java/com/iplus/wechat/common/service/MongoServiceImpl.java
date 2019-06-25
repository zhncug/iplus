package com.iplus.wechat.common.service;

import com.iplus.wechat.common.data.GroupCount;
import com.iplus.wechat.common.repository.BaseRepository;
import com.iplus.wechat.common.repository.FieldGroupFilter;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class MongoServiceImpl<D, Q, I extends Serializable, R extends BaseRepository<D, Q, I>>
        implements BaseService<D, Q, I> {

    protected R repository;

    public MongoServiceImpl(R repository) {
        this.repository = repository;
    }

    @Override
    public D findById(I id) {
        return repository.findById(id);
    }

    @Override
    public D save(D entity) {
        return repository.save(entity);
    }

    public List<D> find(Q query) {
        return repository.find(query, null);
    }

    @Override
    public List<D> find(Q query, Pageable pageable) {
        return repository.find(query, pageable);
    }

    @Override
    public Optional<D> findOne(Q query) {
        return repository.findOne(query);
    }

    @Override
    public List<I> findIds(Q query, Pageable pageable) {
        return repository.findIds(query, pageable);
    }

    @Override
    public long count(Q query) {
        return repository.count(query);
    }

    @Override
    public <T> List<GroupCount<T>> groupByField(Q query, String field, Pageable pageable) {
        return repository.groupByField(query, field, pageable);
    }

    @Override
    public <T> List<GroupCount<T>> groupByField(Q query, String field, Pageable pageable,
                                                FieldGroupFilter filter) {
        return repository.groupByField(query, field, pageable, filter);
    }

    @Override
    public long countGroup(Q query, String field) {
        return repository.countGroup(query, field);
    }

    @Override
    public long countGroup(Q query, String field, FieldGroupFilter filter) {
        return repository.countGroup(query, field, filter);
    }

    @Override
    public int delete(I id) {
        return repository.delete(id);
    }

    @Override
    public int deleteMany(Q query) {
        return repository.deleteMany(query);
    }

    @Override
    public boolean exists(I id) {
        return repository.exists(id);
    }

    @Override
    public void update(Q query, Map<String, Object> fieldAndValue) {
        repository.update(query, fieldAndValue);
    }

    @Override
    public <K, V> List<Map<K, V>> findOptionalField(Q query, Pageable pageable, List<String> fields) {
        return repository.findOptionalField(query, pageable, fields);
    }

}
