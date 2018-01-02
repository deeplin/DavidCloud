package com.async.davidconsole.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * author: Ling Lin
 * created on: 2017/7/20 11:36
 * email: 10525677@qq.com
 * description:
 */

@Entity
public class WeightModel {

    @Id(autoincrement = true)
    private Long id;
    private long time;
    private int SC;

    @Generated(hash = 1558175112)
    public WeightModel(Long id, long time, int SC) {
        this.id = id;
        this.time = time;
        this.SC = SC;
    }

    @Generated(hash = 448621343)
    public WeightModel() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getTime() {
        return this.time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getSC() {
        return this.SC;
    }

    public void setSC(int SC) {
        this.SC = SC;
    }
}
