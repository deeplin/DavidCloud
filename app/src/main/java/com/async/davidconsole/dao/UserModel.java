package com.async.davidconsole.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * author: Ling Lin
 * created on: 2017/7/20 11:36
 * email: 10525677@qq.com
 * description:
 */

@Entity
public class UserModel {

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    public boolean getSex() {
        return this.sex;
    }

    @Id(autoincrement = true)
    private Long id;
    private String userId;
    private String name;
    private boolean sex;
    @Generated(hash = 1996463721)
    public UserModel(Long id, String userId, String name, boolean sex) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.sex = sex;
    }

    @Generated(hash = 782181818)
    public UserModel() {
    }
}
