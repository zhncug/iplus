package com.iplus.common.repository;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.mongodb.core.query.Criteria;

@Getter
@Builder
public class FieldGroupFilter {

    public static final FieldGroupFilter IGNORE_EMPTY =
            FieldGroupFilter.builder().ignoreEmpty(true).build();

    public enum CompareOperator {
        EQ,
        GT,
        GTE,
        LT,
        LTE,
        NE
    }

    private boolean ignoreEmpty = false;

    private int count;
    private CompareOperator compareOperator;

    public Criteria toMongoCriteria(String field) {
        if (compareOperator == null) {
            return null;
        }

        Criteria criteria = Criteria.where(field);
        switch (compareOperator) {
            case EQ:
                criteria.is(count);
                break;
            case GT:
                criteria.gt(count);
                break;
            case GTE:
                criteria.gte(count);
                break;
            case LT:
                criteria.lt(count);
                break;
            case LTE:
                criteria.lte(count);
                break;
            case NE:
                criteria.ne(count);
                break;
            default:
                return null;
        }

        return criteria;
    }

}
