package com.async.davidconsole.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * author: Ling Lin
 * created on: 2017/7/20 22:21
 * email: 10525677@qq.com
 * description:
 */

@Entity
public class SensorRange {

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getSkinUpper() {
        return skinUpper;
    }

    public void setSkinUpper(int skinUpper) {
        this.skinUpper = skinUpper;
    }

    public int getSkinLowerChinese() {
        return skinLowerChinese;
    }

    public void setSkinLowerChinese(int skinLowerChinese) {
        this.skinLowerChinese = skinLowerChinese;
    }

    public int getSkinLowerNonChinese() {
        return skinLowerNonChinese;
    }

    public void setSkinLowerNonChinese(int skinLowerNonChinese) {
        this.skinLowerNonChinese = skinLowerNonChinese;
    }

    public int getAirUpper() {
        return airUpper;
    }

    public void setAirUpper(int airUpper) {
        this.airUpper = airUpper;
    }

    public int getAirLower() {
        return airLower;
    }

    public void setAirLower(int airLower) {
        this.airLower = airLower;
    }

    public int getSpo2UpperTop() {
        return spo2UpperTop;
    }

    public void setSpo2UpperTop(int spo2UpperTop) {
        this.spo2UpperTop = spo2UpperTop;
    }

    public int getSpo2UpperBottom() {
        return spo2UpperBottom;
    }

    public void setSpo2UpperBottom(int spo2UpperBottom) {
        this.spo2UpperBottom = spo2UpperBottom;
    }

    public int getSpo2LowerTop() {
        return spo2LowerTop;
    }

    public void setSpo2LowerTop(int spo2LowerTop) {
        this.spo2LowerTop = spo2LowerTop;
    }

    public int getSpo2LowerBottom() {
        return spo2LowerBottom;
    }

    public void setSpo2LowerBottom(int spo2LowerBottom) {
        this.spo2LowerBottom = spo2LowerBottom;
    }

    public int getPrUpperTop() {
        return prUpperTop;
    }

    public void setPrUpperTop(int prUpperTop) {
        this.prUpperTop = prUpperTop;
    }

    public int getPrUpperBottom() {
        return prUpperBottom;
    }

    public void setPrUpperBottom(int prUpperBottom) {
        this.prUpperBottom = prUpperBottom;
    }

    public int getPrLowerTop() {
        return prLowerTop;
    }

    public void setPrLowerTop(int prLowerTop) {
        this.prLowerTop = prLowerTop;
    }

    public int getPrLowerBottom() {
        return prLowerBottom;
    }

    public void setPrLowerBottom(int prLowerBottom) {
        this.prLowerBottom = prLowerBottom;
    }

    public int getOxygenUpper() {
        return oxygenUpper;
    }

    public void setOxygenUpper(int oxygenUpper) {
        this.oxygenUpper = oxygenUpper;
    }

    public int getOxygenLower() {
        return oxygenLower;
    }

    public void setOxygenLower(int oxygenLower) {
        this.oxygenLower = oxygenLower;
    }

    public int getHumidityUpper() {
        return humidityUpper;
    }

    public void setHumidityUpper(int humidityUpper) {
        this.humidityUpper = humidityUpper;
    }

    public int getHumidityLower() {
        return humidityLower;
    }

    public void setHumidityLower(int humidityLower) {
        this.humidityLower = humidityLower;
    }

    public byte getLanguageIndex() {
        return languageIndex;
    }

    public void setLanguageIndex(byte languageIndex) {
        this.languageIndex = languageIndex;
    }

    @Id
    private Long id = 0L;
    private int skinUpper = 380;
    private int skinLowerChinese = 320;
    private int skinLowerNonChinese = 340;
    private int airUpper = 390;
    private int airLower = 200;
    private int spo2UpperTop = 1000;
    private int spo2UpperBottom = 500;
    private int spo2LowerTop = 950;
    private int spo2LowerBottom = 450;
    private int prUpperTop = 240;
    private int prUpperBottom = 80;
    private int prLowerTop = 180;
    private int prLowerBottom = 35;
    private int oxygenUpper = 650;
    private int oxygenLower = 200;
    private int humidityUpper = 950;
    private int humidityLower = 0;
    private byte languageIndex;
    @Generated(hash = 798195027)
    public SensorRange(Long id, int skinUpper, int skinLowerChinese,
            int skinLowerNonChinese, int airUpper, int airLower, int spo2UpperTop,
            int spo2UpperBottom, int spo2LowerTop, int spo2LowerBottom,
            int prUpperTop, int prUpperBottom, int prLowerTop, int prLowerBottom,
            int oxygenUpper, int oxygenLower, int humidityUpper, int humidityLower,
            byte languageIndex) {
        this.id = id;
        this.skinUpper = skinUpper;
        this.skinLowerChinese = skinLowerChinese;
        this.skinLowerNonChinese = skinLowerNonChinese;
        this.airUpper = airUpper;
        this.airLower = airLower;
        this.spo2UpperTop = spo2UpperTop;
        this.spo2UpperBottom = spo2UpperBottom;
        this.spo2LowerTop = spo2LowerTop;
        this.spo2LowerBottom = spo2LowerBottom;
        this.prUpperTop = prUpperTop;
        this.prUpperBottom = prUpperBottom;
        this.prLowerTop = prLowerTop;
        this.prLowerBottom = prLowerBottom;
        this.oxygenUpper = oxygenUpper;
        this.oxygenLower = oxygenLower;
        this.humidityUpper = humidityUpper;
        this.humidityLower = humidityLower;
        this.languageIndex = languageIndex;
    }

    @Generated(hash = 1754299684)
    public SensorRange() {
    }


}
