package com.example.ryanm.homecontrol;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class PlugView extends View {
    public int PlugID = 0;
    private String IP;
    private Plug plug;
    private boolean status1 = false;
    private boolean status2 = false;
    private CharSequence plugName;
    private Paint paint= new Paint();
    private Paint paint2= new Paint();
    private int width;
    Context context = null;

    public PlugView(Context context) {
        super(context);
        this.context = context;
    }

    public void setPlug(Plug plug) {
        this.plug = plug;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        width = MeasureSpec.getSize(widthMeasureSpec);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setColor(Color.WHITE);
        paint2.setColor(Color.LTGRAY);
        canvas.drawRect(0, 0, width, 20, paint2);

    }
}

