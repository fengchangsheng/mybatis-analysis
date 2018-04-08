package com.fcs.plugins.paging;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by fengcs on 2018/4/8.
 */
public class PagingList<E> extends ArrayList<E> {

    private Paging paging;

    public PagingList() {
    }

    public PagingList(Collection<? extends E> c, Paging paging) {
        super(c);
        this.paging = paging;
    }

    public PagingList(Paging paging) {
        this.paging = paging;
    }

    public Paging getPaging() {
        return paging;
    }

    public void setPaging(Paging paging) {
        this.paging = paging;
    }
}
