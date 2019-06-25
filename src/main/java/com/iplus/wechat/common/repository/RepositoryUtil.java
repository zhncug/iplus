package com.iplus.wechat.common.repository;

import com.iplus.wechat.common.data.DateRange;
import com.iplus.wechat.common.data.DurationRange;
import com.iplus.wechat.common.data.RateRange;
import com.iplus.wechat.common.model.CommonFields;
import com.iplus.wechat.common.utils.DateRangeParser;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.Range;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.limit;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.skip;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.sort;

public class RepositoryUtil {
    private static final int MIN_IN_SECOND = 60;

    public static String concatFields(String alias, String field) {
        return alias + "." + field;
    }

    public static String toSqlLike(String pattern) {
        return String.format("%%%s%%", pattern);
    }

    public static Pattern toPatternLike(String pattern) {
        pattern = Pattern.quote(pattern);
        return Pattern.compile(String.format(".*%s.*", pattern), Pattern.CASE_INSENSITIVE);
    }

    public static void applyDateRange(org.springframework.data.mongodb.core.query.Criteria mongoCriteria,
                                      String field,
                                      DateRange dateRange) {
        DateRangeParser.parse(dateRange, new DateRangeParser.ParserClient() {
            @Override
            public void setOnlyStart(Date start) {
                mongoCriteria.and(field).gte((start.getTime() / 1000));
            }

            @Override
            public void setOnlyEnd(Date end) {
                mongoCriteria.and(field).lte((end.getTime() / 1000));
            }

            @Override
            public void setBetweenStartAndEnd(Date start, Date end) {
                mongoCriteria.and(field).gte(start.getTime() / 1000).lte(end.getTime() / 1000);
            }
        });
    }

    public static void applyDateRangeWithDate(org.springframework.data.mongodb.core.query.Criteria mongoCriteria,
                                              String field,
                                              DateRange dateRange) {
        DateRangeParser.parse(dateRange, new DateRangeParser.ParserClient() {
            @Override
            public void setOnlyStart(Date start) {
                mongoCriteria.and(field).gte((start));
            }

            @Override
            public void setOnlyEnd(Date end) {
                mongoCriteria.and(field).lte((end));
            }

            @Override
            public void setBetweenStartAndEnd(Date start, Date end) {
                mongoCriteria.and(field).gte(start).lte(end);
            }
        });
    }

    public static void
    applyDurationRange(org.springframework.data.mongodb.core.query.Criteria criteria,
                       String field,
                       List<DurationRange> durationRanges) {

        if (CollectionUtils.isEmpty(durationRanges)) {
            return;
        }

        List<org.springframework.data.mongodb.core.query.Criteria> criteriaList = new ArrayList<>();

        for (DurationRange durationRange : durationRanges) {
            switch (durationRange) {
                case LESS_THAN_1_MIN:
                    criteriaList.add(
                            org.springframework.data.mongodb.core.query.Criteria.where(field).lt(MIN_IN_SECOND));
                    break;
                case BETWEEN_1_TO_3_MIN:
                    criteriaList.add(
                            org.springframework.data.mongodb.core.query.Criteria
                                    .where(field).gte(MIN_IN_SECOND).lt(3 * MIN_IN_SECOND));
                    break;
                case BETWEEN_3_TO_5_MIN:
                    criteriaList.add(
                            org.springframework.data.mongodb.core.query.Criteria
                                    .where(field).gte(3 * MIN_IN_SECOND).lt(5 * MIN_IN_SECOND));
                    break;
                case BETWEEN_5_TO_10_MIN:
                    criteriaList.add(
                            org.springframework.data.mongodb.core.query.Criteria
                                    .where(field).gte(5 * MIN_IN_SECOND).lt(10 * MIN_IN_SECOND));
                    break;
                case BETWEEN_10_TO_3O_MIN:
                    criteriaList.add(
                            org.springframework.data.mongodb.core.query.Criteria
                                    .where(field).gte(10 * MIN_IN_SECOND).lt(30 * MIN_IN_SECOND));
                    break;
                case LAGER_THAN_30_MIN:
                    criteriaList.add(
                            org.springframework.data.mongodb.core.query.Criteria
                                    .where(field).gte(30 * MIN_IN_SECOND));
                    break;
            }
        }

        criteria.andOperator(new org.springframework.data.mongodb.core.query.Criteria()
                .orOperator(criteriaList.toArray(new org.springframework.data.mongodb.core.query.Criteria[0])));
    }

    public static void applyPageable(List<AggregationOperation> aggregationOperations,
                                     Pageable pageable) {
        if (pageable != null) {
            if (pageable.getSort() != null) {
                Sort sort = pageable.getSort();
                SortOperation sortOperation = sort(sort);
                Iterator<Sort.Order> orders = sort.iterator();
                boolean needToAddSecondOrder = true;
                //增加次要排序字段，避免主要排序字段重复数据过多导致分页排序结果不稳定
                while (orders.hasNext()) {
                    //如果排序中已经有了根据_id进行排序则不需要再增加次要排序，否则会导致排序结果不正确
                    if (orders.next().getProperty().equals(CommonFields.MONGODB_ID)) {
                        needToAddSecondOrder = false;
                    }
                }

                if (needToAddSecondOrder) {
                    sortOperation.and(Sort.Direction.ASC, CommonFields.MONGODB_ID);
                }
                aggregationOperations.add(sortOperation);
            }

            if (pageable.getOffset() > 0
                    || (pageable.getOffset() == 0 && pageable.getPageSize() < Integer.MAX_VALUE)) {
                aggregationOperations.add(skip((long) pageable.getOffset()));
                aggregationOperations.add(limit(pageable.getPageSize()));
            }
        }
    }

    /**
     * 百分率区间排序
     *
     * @param criteria
     * @param field
     * @param rateRange
     */
    public static org.springframework.data.mongodb.core.query.Criteria applyRateRange(
            org.springframework.data.mongodb.core.query.Criteria criteria,
            String field,
            RateRange rateRange) {

        if (RateRange.BETWEEN_0_TO_20.equals(rateRange)) {

            criteria.orOperator(
                    criteria.where(field).gte(rateRange.getStartRate()).lt(rateRange.getEndRate())
                    , criteria.where(field).is(null));
        } else {

            criteria.and(field).gte(rateRange.getStartRate()).lt(rateRange.getEndRate());
        }
        return criteria;
    }

    public static org.springframework.data.mongodb.core.query.Criteria between(
            org.springframework.data.mongodb.core.query.Criteria criteria,
            String field,
            Range<?> range) {
        if (range != null) {
            criteria.and(field).gte(range.getMinimum()).lt(range.getMaximum());
        }
        return criteria;
    }

    /**
     * 大于
     *
     * @param criteria
     * @param field
     */
    public static org.springframework.data.mongodb.core.query.Criteria greater(
            org.springframework.data.mongodb.core.query.Criteria criteria,
            String field,
            Integer count) {
        if (count != null) {
            criteria.and(field).gt(count);
        }
        return criteria;
    }

    /**
     * 小于
     */
    public static org.springframework.data.mongodb.core.query.Criteria longLess(
            org.springframework.data.mongodb.core.query.Criteria criteria,
            String field,
            Long l) {
        if (l != null) {
            criteria.and(field).lt(l);
        }
        return criteria;
    }


    public static org.springframework.data.mongodb.core.query.Criteria
    stringEq(org.springframework.data.mongodb.core.query.Criteria criteria,
             String field,
             String value) {
        if (StringUtils.isNotEmpty(value)) {
            return criteria.and(field).is(value);
        }

        return criteria;
    }

    /**
     * 不等于
     *
     * @param criteria
     * @param field
     * @param value
     * @return
     */
    public static org.springframework.data.mongodb.core.query.Criteria
    stringNe(org.springframework.data.mongodb.core.query.Criteria criteria,
             String field,
             String value) {
        if (StringUtils.isNotEmpty(value)) {
            return criteria.and(field).ne(value);
        }

        return criteria;
    }

    public static org.springframework.data.mongodb.core.query.Criteria
    stringLike(org.springframework.data.mongodb.core.query.Criteria criteria,
               String field,
               String value) {
        if (StringUtils.isNotEmpty(value)) {
            return criteria.and(field).regex(toPatternLike(value));
        }

        return criteria;
    }

    public static <T> org.springframework.data.mongodb.core.query.Criteria
    commonEq(org.springframework.data.mongodb.core.query.Criteria criteria,
             String field,
             T value) {
        if (value != null) {
            return criteria.and(field).is(value);
        }

        return criteria;
    }

    public static org.springframework.data.mongodb.core.query.Criteria
    collectionIn(org.springframework.data.mongodb.core.query.Criteria criteria,
                 String field,
                 Collection<?> value) {
        if (CollectionUtils.isNotEmpty(value)) {
            return criteria.and(field).in(value);
        }

        return criteria;
    }

    public static org.springframework.data.mongodb.core.query.Criteria
    collectionInLike(org.springframework.data.mongodb.core.query.Criteria criteria,
                     String field,
                     Collection<?> values) {
        if (CollectionUtils.isNotEmpty(values)) {
            org.springframework.data.mongodb.core.query.Criteria[] criterias =
                    new org.springframework.data.mongodb.core.query.Criteria[0];
            List<org.springframework.data.mongodb.core.query.Criteria> criteriaList =
                    values.stream().map(
                            value -> org.springframework.data.mongodb.core.query.Criteria.where(field)
                                    .regex(toPatternLike((String) value))
                    ).collect(Collectors.toList());
            return criteria.orOperator(criteriaList.toArray(criterias));
        }

        return criteria;
    }

    /**
     * 限定timestamp范围
     * TODO 与applyDateRange功能一致，后续考虑合并
     *
     * @param criteria
     * @param field
     * @param dateRange
     * @return
     */
    public static org.springframework.data.mongodb.core.query.Criteria
    inDateRangeLong(org.springframework.data.mongodb.core.query.Criteria criteria,
                    String field,
                    DateRange dateRange) {
        if (dateRange != null) {
            return criteria.and(field).gte(dateRange.getStartLong()).lte(dateRange.getEndLong());
        }

        return criteria;
    }

    /**
     * 限定Date范围
     *
     * @param criteria
     * @param field
     * @param dateRange
     * @return
     */
    public static org.springframework.data.mongodb.core.query.Criteria
    inDateRange(org.springframework.data.mongodb.core.query.Criteria criteria,
                String field,
                DateRange dateRange) {
        DateRangeParser.parse(dateRange, new DateRangeParser.ParserClient() {
            @Override
            public void setOnlyStart(Date start) {
                criteria.and(field).gte(start);
            }

            @Override
            public void setOnlyEnd(Date end) {
                criteria.and(field).lte(end);
            }

            @Override
            public void setBetweenStartAndEnd(Date start, Date end) {
                criteria.and(field).gte(start).lte(end);
            }
        });
        return criteria;
    }

    public static org.springframework.data.mongodb.core.query.Criteria
    fieldExists(org.springframework.data.mongodb.core.query.Criteria criteria,
                String field,
                Boolean exists) {
        if (exists != null) {
            return criteria.and(field).exists(exists);
        }

        return criteria;
    }
}
