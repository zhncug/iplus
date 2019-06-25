package com.iplus.wechat.common.data;

import java.util.Collections;
import java.util.List;

public class PagedResult<T> {
    private final List<T> results;
    private final long total;

    public PagedResult(List<T> results, long total) {
        this.results = results == null ? Collections.<T>emptyList() : results;
        this.total = total;
    }

    public List<T> getList() {
        return results;
    }

    public long getTotal() {
        return total;
    }
}
