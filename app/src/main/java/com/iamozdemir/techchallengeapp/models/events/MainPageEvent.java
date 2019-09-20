/*
 * Created by Mehmet Ozdemir on 9/20/19 10:39 PM
 * Copyright (c) 2019 . All rights reserved.
 * Last modified 9/18/19 12:19 PM
 */

package com.iamozdemir.techchallengeapp.models.events;

public class MainPageEvent {

    private MainPages page;
    private Object[] pageItems;

    public MainPageEvent(MainPages page, Object... pageItems) {
        this.page = page;
        this.pageItems = pageItems;
    }

    public MainPages getPage() {
        return page;
    }

    public Object[] getPageItems() {
        return pageItems;
    }

    public enum MainPages {
        LOGIN,
        ORDER_LIST
    }
}
