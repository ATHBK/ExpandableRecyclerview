package com.athbk.expandablerecyclerview;

import java.util.List;

/**
 * Created by athbk on 4/12/17.
 */

public interface Parent<C> {

    List<C> getListChild();

    boolean isExpand();
}
