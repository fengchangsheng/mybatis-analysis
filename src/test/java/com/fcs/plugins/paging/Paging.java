package com.fcs.plugins.paging;

/**
 * Created by fengcs on 2018/4/8.
 */
public class Paging {

    private int offset;
    private int limit;
    private int totalCount;
    private int toatalPage;

    public Paging(PagingRowBound pagingRowBound, int totalCount) {
        this.offset = pagingRowBound.getOffset();
        this.limit = pagingRowBound.getLimit();
        this.totalCount = totalCount;
        this.toatalPage = (totalCount - 1) / limit + 1;
    }

    public Paging(int offset, int limit, int totalCount) {
        this.offset = offset;
        this.limit = limit;
        this.totalCount = totalCount;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getToatalPage() {
        return toatalPage;
    }

    public void setToatalPage(int toatalPage) {
        this.toatalPage = toatalPage;
    }
}
