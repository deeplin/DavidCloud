package com.async.davidconsole.dao;

import com.async.common.serial.BaseSerialMessage;
import com.async.davidconsole.serial.command.CommandChar;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;

/**
 * author: Ling Lin
 * created on: 2017/7/18 16:27
 * email: 10525677@qq.com
 * description:
 */

@Entity
public class AnalogCommand extends BaseSerialMessage {

    @Transient
    public static final byte[] COMMAND = ("~ANALOG ALL" + CommandChar.ENTER).getBytes();

//      OK;T1=269;T2=269;T3=269;VR=2747;VU=2300;VB=559;A3=NA;S3=NA;S1A=NA;S1B=93;S2=NA;A1=NA;A2=93;
//      F1=NA;H1=NA;O1=27;O2=1;SC=751;SP=0;PR=0;PI=0;
    @Id(autoincrement = true)
    private Long id;

    private long time;
    private int S1A;
    private int S1B;
    private int S2;
    private int S3;
    private int A1;
    private int A2;
    private int A3;
    private int F1;
    private int H1;
    private int O1;
    private int O2;
    private int O3;
    private int SP;
    private int PR;
    private int PI;
    private int VB;
    private int VR;
    private int VU;
    private int T1;
    private int T2;
    private int T3;

    @Transient
    private int SC;

    @Generated(hash = 430715615)
    public AnalogCommand(Long id, long time, int S1A, int S1B, int S2, int S3, int A1, int A2, int A3,
                         int F1, int H1, int O1, int O2, int O3, int SP, int PR, int PI, int VB, int VR, int VU,
                         int T1, int T2, int T3) {
        this.id = id;
        this.time = time;
        this.S1A = S1A;
        this.S1B = S1B;
        this.S2 = S2;
        this.S3 = S3;
        this.A1 = A1;
        this.A2 = A2;
        this.A3 = A3;
        this.F1 = F1;
        this.H1 = H1;
        this.O1 = O1;
        this.O2 = O2;
        this.O3 = O3;
        this.SP = SP;
        this.PR = PR;
        this.PI = PI;
        this.VB = VB;
        this.VR = VR;
        this.VU = VU;
        this.T1 = T1;
        this.T2 = T2;
        this.T3 = T3;
    }

    @Generated(hash = 1330323325)
    public AnalogCommand() {
    }


    @Override
    public byte[] getRequest() {
        return COMMAND;
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

    public int getS1A() {
        return this.S1A;
    }

    public void setS1A(int S1A) {
        this.S1A = S1A;
    }

    public int getS1B() {
        return this.S1B;
    }

    public void setS1B(int S1B) {
        this.S1B = S1B;
    }

    public int getS2() {
        return S2;
    }

    public void setS2(int s2) {
        S2 = s2;
    }

    public int getA1() {
        return this.A1;
    }

    public void setA1(int A1) {
        this.A1 = A1;
    }

    public int getF1() {
        return this.F1;
    }

    public void setF1(int F1) {
        this.F1 = F1;
    }

    public int getA2() {
        return this.A2;
    }

    public void setA2(int a2) {
        this.A2 = a2;
    }

    public int getH1() {
        return this.H1;
    }

    public void setH1(int H1) {
        this.H1 = H1;
    }

    public int getO1() {
        return this.O1;
    }

    public void setO1(int O1) {
        this.O1 = O1;
    }

    public int getO3() {
        return this.O3;
    }

    public void setO3(int O3) {
        this.O3 = O3;
    }

    public int getSP() {
        return this.SP;
    }

    public void setSP(int SP) {
        this.SP = SP;
    }

    public int getPR() {
        return this.PR;
    }

    public void setPR(int PR) {
        this.PR = PR;
    }

    public int getPI() {
        return this.PI;
    }

    public void setPI(int PI) {
        this.PI = PI;
    }

    public int getSC() {
        return this.SC;
    }

    public void setSC(int SC) {
        this.SC = SC;
    }

    public int getO2() {
        return this.O2;
    }

    public void setO2(int O2) {
        this.O2 = O2;
    }

    public int getVB() {
        return VB;
    }

    public void setVB(int VB) {
        this.VB = VB;
    }

    public int getT1() {
        return this.T1;
    }

    public void setT1(int t1) {
        T1 = t1;
    }

    public int getT2() {
        return this.T2;
    }

    public void setT2(int t2) {
        T2 = t2;
    }

    public int getT3() {
        return this.T3;
    }

    public void setT3(int t3) {
        T3 = t3;
    }

    public int getVR() {
        return this.VR;
    }

    public void setVR(int VR) {
        this.VR = VR;
    }

    public int getVU() {
        return this.VU;
    }

    public void setVU(int VU) {
        this.VU = VU;
    }

    public int getA3() {
        return this.A3;
    }

    public void setA3(int a3) {
        A3 = a3;
    }

    public int getS3() {
        return this.S3;
    }

    public void setS3(int s3) {
        S3 = s3;
    }
}
