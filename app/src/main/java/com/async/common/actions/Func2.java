package com.async.common.actions;

/**
 * author: Ling Lin
 * created on: 2017/7/25 21:15
 * email: 10525677@qq.com
 * description:
 */

public interface Func2<U, V> {
    V accept(U t);
}