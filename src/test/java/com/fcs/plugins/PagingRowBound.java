package com.fcs.plugins;

import org.apache.ibatis.session.RowBounds;

public class PagingRowBound extends RowBounds {

    public PagingRowBound() {
    }

    public PagingRowBound(int offset, int limit) {
        super(offset, limit);
    }
}
