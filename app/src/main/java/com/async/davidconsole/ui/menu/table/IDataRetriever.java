package com.async.davidconsole.ui.menu.table;

import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * author: Ling Lin
 * created on: 2018/1/2 10:04
 * email: 10525677@qq.com
 * description:
 */
public interface IDataRetriever {
    int getRowSize();

    int getColumnSize();

    int getColoredValue();

    void goPrevious();

    void goNext();

    List<String> getHeadList();

    void setConsumer(Consumer<List<String>> consumer);

}
