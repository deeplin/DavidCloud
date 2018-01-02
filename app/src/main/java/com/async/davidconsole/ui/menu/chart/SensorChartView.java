package com.async.davidconsole.ui.menu.chart;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import com.async.common.utils.LogUtil;
import com.async.davidconsole.utils.SystemConfig;

import org.xclcharts.chart.CustomLineData;
import org.xclcharts.chart.LineChart;
import org.xclcharts.chart.LineData;
import org.xclcharts.common.DensityUtil;
import org.xclcharts.renderer.XEnum;
import org.xclcharts.renderer.axis.DataAxis;
import org.xclcharts.view.ChartView;

import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;

/**
 * author: Ling Lin
 * created on: 2018/1/1 10:46
 * email: 10525677@qq.com
 * description:
 */
public class SensorChartView extends ChartView {

    protected final LineChart mChart = new LineChart();
    protected final List<CustomLineData> mCustomLineDataSet = new LinkedList<>();
    private final List<String> mXAxisLabels = new LinkedList<>();
    private final List<LineData> mChartData = new LinkedList<>();
    public SensorChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        //设置绘图区默认缩进px值,留置空间显示Axis,Axis title....
        int[] ltrb = getBarLnDefaultSpadding();
        mChart.setPadding(ltrb[0], ltrb[1], ltrb[2], ltrb[3]);

        //数据源
        mChart.setDataSource(mChartData);
        mChart.setCategories(mXAxisLabels);
        mChart.setCustomLines(mCustomLineDataSet);

        //定义数据轴标签显示格式
        mChart.getDataAxis().setLabelFormatter(value -> {
            Double tmp = Double.parseDouble(value);
            DecimalFormat df = new DecimalFormat("#0");
            return (df.format(tmp));
        });

        //标签旋转45度
//        mChart.getCategoryAxis().setTickLabelRotateAngle(45f);
        mChart.getCategoryAxis().getTickLabelPaint().setTextSize(15);

        //设定格式
        mChart.setItemLabelFormatter(value -> {
            DecimalFormat df = new DecimalFormat("#0.0");
            return df.format(value);
        });

        //禁用平移模式
        mChart.disablePanMode();
        //提高性能
        mChart.disableHighPrecision();

        //柱形和标签居中方式
        mChart.setBarCenterStyle(XEnum.BarCenterStyle.TICKMARKS);

        mChart.getDataAxis().setAxisLineStyle(XEnum.AxisLineStyle.FILLCAP);
        mChart.getCategoryAxis().setAxisLineStyle(XEnum.AxisLineStyle.FILLCAP);

        mChart.setLineAxisIntersectVisible(false);
    }

    public void resetXAxix() {
        mXAxisLabels.clear();
    }

    public void addXAxisLabel(String label) {
        mXAxisLabels.add(label);
    }

    public void setYAxisLabels(double max, double min, double step, double detailStep) {
        DataAxis dataAxis = mChart.getDataAxis();
        dataAxis.setAxisMax(max);
        dataAxis.setAxisMin(min);
        dataAxis.setAxisSteps(step);
        //指隔多少个轴刻度(即细刻度)后为主刻度
        dataAxis.setDetailModeSteps(detailStep);
        dataAxis.hideFirstTick();
    }

    public void addDataSet(List<Double> dataSeries, int color) {
        clearDataSet(color);
        LineData lineData = new LineData("", dataSeries, color);
        lineData.setDotStyle(XEnum.DotStyle.HIDE);
        lineData.getLinePaint().setStrokeWidth(2);
        mChartData.add(lineData);
    }

    public void rePaint() {
        this.invalidate();
    }

    public void clearDataSet(int color) {
        for (LineData lineData : mChartData) {
            if (lineData.getLineColor() == color) {
                mChartData.remove(lineData);
                break;
            }
        }
    }

    public void clearAllDataSet() {
        mChartData.clear();
        mCustomLineDataSet.clear();
    }

    /**
     * 期望线/分界线
     */
    public void setObjectiveLines(double objective) {
        mCustomLineDataSet.clear();
        if (objective >= 0) {
            CustomLineData customLineData = new CustomLineData(
                    "", objective, SystemConfig.AXIS_COLOR, 2);
            customLineData.setLineStyle(XEnum.LineStyle.DASH);
            mCustomLineDataSet.add(customLineData);
        }
    }

    protected int getLeftOffset(){
        return 60;
    }
    //偏移出来的空间用于显示tick,axistitle....
    protected int[] getBarLnDefaultSpadding() {
        int[] ltrb = new int[4];
        ltrb[0] = DensityUtil.dip2px(getContext(), getLeftOffset()); //left
        ltrb[1] = DensityUtil.dip2px(getContext(), 50); //top
        ltrb[2] = DensityUtil.dip2px(getContext(), 44); //right
        ltrb[3] = DensityUtil.dip2px(getContext(), 50); //bottom
        return ltrb;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //图所占范围大小
        mChart.setChartRange(w, h);
    }

    @Override
    public void render(Canvas canvas) {
        try {
            mChart.render(canvas);
        } catch (Exception e) {
            LogUtil.e(this, e);
        }
    }
}