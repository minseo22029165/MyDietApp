package com.example.mydietapp.decorator;

import android.graphics.Canvas;
import android.graphics.Color;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.renderer.XAxisRenderer;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.ViewPortHandler;

public class ColoredLabelXAxisRenderer extends XAxisRenderer {

    public ColoredLabelXAxisRenderer(ViewPortHandler viewPortHandler, XAxis xAxis, Transformer trans) {
        super(viewPortHandler, xAxis, trans);
    }
    
    // 특정한 xAxis label에 스타일링하기
    @Override
    protected void drawLabels(Canvas c, float pos, MPPointF anchor) {
//        mAxisLabelPaint.setColor(Color.YELLOW);
    }
//    @Override
//    public void renderAxisLine(Canvas c) {
//
//
//        if (mXAxis.getPosition() == XAxis.XAxisPosition.BOTTOM) {
////            c.drawColor(Color.BLUE); // 배경 전체 색깔
//
//        }
//    }
//
//    @Override
//    protected void drawLabel(Canvas c, String formattedLabel, float x, float y, MPPointF anchor, float angleDegrees) {
//        super.drawLabel(c, formattedLabel, x, y, anchor, angleDegrees);
//    }
}
