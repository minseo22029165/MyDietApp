package com.example.mydietapp.decorator;

import android.graphics.Canvas;
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
        
    }
}
