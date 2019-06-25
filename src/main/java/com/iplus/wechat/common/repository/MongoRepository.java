package com.iplus.wechat.common.repository;

import com.iplus.wechat.common.data.GroupCount;
import com.iplus.wechat.common.model.CommonFields;
import com.iplus.wechat.common.utils.ReflectUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

public abstract class MongoRepository<D, Q, I extends Serializable> extends RepositoryAdapter<D, Q, I> {

    protected MongoTemplate mongoTemplate;
    protected final String collectionName;
    protected Class<D> dataClass;

    public MongoRepository(String collectionName,
                           MongoTemplate mongoTemplate) {
        dataClass = ReflectUtils.getGenericClass(this, MongoRepository.class, 0);
        if (StringUtils.isEmpty(collectionName)) {
            this.collectionName = mongoTemplate.getCollectionName(dataClass);
        } else {
            this.collectionName = collectionName;
        }

        this.mongoTemplate = mongoTemplate;
    }

    public MongoRepository(MongoTemplate mongoTemplate) {
        this(null, mongoTemplate);
    }

    @Override
    public D findById(I id) {
        return mongoTemplate.findById(id, dataClass);
    }

    @Override
    public List<D> find(Q query) {
        return find(query, null);
    }

    @Override
    public List<D> find(Q query, Pageable pageable) {
        List<AggregationOperation> aggregationOperations = createAggregationOperations(query);
        applyPageable(aggregationOperations, pageable);
        Aggregation aggregation = Aggregation.newAggregation(aggregationOperations);
        return mongoTemplate.aggregate(aggregation, collectionName, dataClass).getMappedResults();
    }

    protected List<AggregationOperation> createAggregationOperations(Q query) {
        List<AggregationOperation> operations = new ArrayList<>();
        operations.add(match(createCriteria(query)));

        return operations;
    }

    protected abstract Criteria createCriteria(Q query);

    protected void applyPageable(List<AggregationOperation> aggregationOperations, Pageable pageable) {
        RepositoryUtil.applyPageable(aggregationOperations, pageable);
    }

    @Override
    public Optional<D> findOne(Q query) {
        List<AggregationOperation> aggregationOperations = createAggregationOperations(query);
        aggregationOperations.add(limit(1));
        Aggregation aggregation = Aggregation.newAggregation(aggregationOperations);
        List<D> result = mongoTemplate.aggregate(aggregation, collectionName, dataClass).getMappedResults();

        return CollectionUtils.isEmpty(result) ? Optional.empty() : Optional.of(result.get(0));
    }

    @Override
    public List<I> findIds(Q query, Pageable pageable) {
        String idField = getIdField();
        List<AggregationOperation> aggregationOperations = createAggregationOperations(query);
        applyPageable(aggregationOperations, pageable);
        aggregationOperations.add(project(idField));
        Aggregation aggregation = Aggregation.newAggregation(aggregationOperations);

        List<Map> idMaps = mongoTemplate.aggregate(
                aggregation, collectionName, Map.class).getMappedResults();
        return idMaps.stream().map(m -> (I) m.get(idField)).collect(Collectors.toList());
    }

    protected String getIdField() {
        return CommonFields.MONGODB_ID;
    }

    @Override
    public long count(Q query) {
        List<AggregationOperation> aggregationOperations = createAggregationOperations(query);
        aggregationOperations.add(Aggregation.group().count().as(CommonFields.COUNT));
        Aggregation aggregation = Aggregation.newAggregation(aggregationOperations);
        GroupCount result = mongoTemplate.aggregate(aggregation, collectionName, GroupCount.class)
                .getUniqueMappedResult();
        return result == null ? 0 : result.getCount();
    }

    @Override
    public D save(D entity) {
        mongoTemplate.save(entity);
        return entity;
    }

    public void save(List<D> entities) {
        if (CollectionUtils.isNotEmpty(entities)) {
            BulkOperations bulkOperations =
                    mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, dataClass);
            bulkOperations.insert(entities);
            bulkOperations.execute();
        }
    }

    @Override
    public <T> List<GroupCount<T>> groupByField(Q query, String field, Pageable pageable) {
        return groupByField(query, field, pageable, null);
    }

    @Override
    public <T> List<GroupCount<T>> groupByField(Q query, String field, Pageable pageable,
                                                FieldGroupFilter filter) {
        List<AggregationOperation> operations = createGroupOperations(query, field);

        applyGroupFilter(operations, field, filter);
        applyPageable(operations, pageable);

        Aggregation aggregation = Aggregation.newAggregation(operations);
        AggregationResults<GroupCount> aggregationResults
                = mongoTemplate.aggregate(aggregation, collectionName, GroupCount.class);

        return aggregationResults.getMappedResults().stream()
                .map(gc -> (GroupCount<T>) gc).collect(Collectors.toList());
    }

    protected List<AggregationOperation> createGroupOperations(Q query, String field) {
        List<AggregationOperation> operations = prepareGroupOperations(query, field);
        operations.add(group(field).count().as(CommonFields.COUNT));

        return operations;
    }

    protected List<AggregationOperation> prepareGroupOperations(Q query, String field) {
        return createAggregationOperations(query);
    }

    private void applyGroupFilter(List<AggregationOperation> operations, String field,
                                  FieldGroupFilter filter) {
        if (filter == null) {
            return;
        }

        if (filter.isIgnoreEmpty()) {

            Criteria andCriteria = new Criteria().andOperator(
                    Criteria.where(field).ne(null),
                    Criteria.where(field).ne(""));

            operations.add(0, match(andCriteria));
        }

        Criteria having = filter.toMongoCriteria(CommonFields.COUNT);
        if (having != null) {
            operations.add(match(having));
        }
    }

    @Override
    public long countGroup(Q query, String field) {
        return countGroup(query, field, null);
    }

    @Override
    public long countGroup(Q query, String field, FieldGroupFilter filter) {
        List<AggregationOperation> operations = createGroupOperations(query, field);
        operations.add(group().count().as(CommonFields.COUNT));

        applyGroupFilter(operations, field, filter);
        Aggregation aggregation = Aggregation.newAggregation(operations);

        AggregationResults<GroupCount> aggregationResults
                = mongoTemplate.aggregate(aggregation, collectionName, GroupCount.class);

        GroupCount groupCount = aggregationResults.getUniqueMappedResult();

        return groupCount == null ? 0 : groupCount.getCount();
    }

    @Override
    public int delete(I id) {
        return mongoTemplate.remove(idQuery(id), dataClass).getN();
    }

    @Override
    public int deleteMany(Q query) {
        return mongoTemplate.remove(Query.query(createCriteria(query)), collectionName).getN();
    }

    @Override
    public boolean exists(I id) {
        return mongoTemplate.exists(idQuery(id), dataClass, collectionName);
    }

    private Query idQuery(I id) {
        return Query.query(Criteria.where(getIdField()).is(id));
    }

    @Override
    public void update(Q query, Map<String, Object> fieldAndValue) {
        List<I> ids = findIds(query, null);

        List<Object> realIds = ids.stream()
                .map(MongoRepository::convertToObjectIdIfNecessary)
                .collect(Collectors.toList());

        Criteria criteria = Criteria.where(getIdField()).in(realIds);
        Query q = Query.query(criteria);
        Update update = new Update();
        for (Map.Entry<String, Object> entry: fieldAndValue.entrySet()) {
            update.set(entry.getKey(), entry.getValue());
        }
        mongoTemplate.updateMulti(q, update, collectionName);
    }

    private static Object convertToObjectIdIfNecessary(Object id) {
        String strId = String.valueOf(id);
        try {
            return new ObjectId(strId);
        } catch (IllegalArgumentException ex) {
            return id;
        }
    }


    //统一修改业务组
    public void updateGroup(List<String> agentIds, int group) {
        if (CollectionUtils.isEmpty(agentIds)) {
            return;
        }
        Criteria criteria = Criteria.where(CommonFields.AGENT_ID).in(agentIds);
        mongoTemplate.updateMulti(
                Query.query(criteria),
                Update.update(CommonFields.BIZGROUP, group),
                collectionName
        );
    }

    @Override
    public <K, V> List<Map<K, V>> findOptionalField(Q query, Pageable pageable, List<String> fields) {
        List<AggregationOperation> operations = createAggregationOperations(query);
        operations.add(project(fields.toArray(new String[fields.size()])));
        applyPageable(operations, pageable);
        Aggregation aggregation = Aggregation.newAggregation(operations);
        AggregationResults<Map> aggregationResults
                = mongoTemplate.aggregate(aggregation, collectionName, Map.class);
        return aggregationResults.getMappedResults().stream()
                .map(map -> (Map<K, V>) map).collect(Collectors.toList());
    }
}
