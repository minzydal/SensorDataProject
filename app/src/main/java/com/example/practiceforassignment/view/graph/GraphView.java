package com.example.practiceforassignment.view.graph;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class GraphView extends View {

    private ScaleGestureDetector mScaleDetector;
    private float mScaleFactor = 1.f;

    Paint paint = new Paint();
    Path path = new Path();

    int color;
    boolean callOnDraw = true;

    ArrayList<Integer> graphValueList = new ArrayList<>();

    private int width;
    private int center;
    private int frame;
    private int xSize = 130;
    private int ySize = 10;
    int diff;

    public GraphView(Context context) {
        super(context);
    }

    public GraphView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
    }

    public GraphView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawColor(Color.WHITE);

        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeWidth(10f);
        paint.setColor(Color.BLACK);

        if (callOnDraw) {
            width = getWidth(); //width : 1275, height : 768
            center = getHeight() / 2; // center : 384
            frame = getWidth() / 100; //frame : 12

            if (graphValueList != null && graphValueList.size() > 0) {
                try {
                    path.reset();
                    Thread.sleep(100);
                    diff = Math.max(graphValueList.size() - frame, 0);

                    for (int i = 0; i < frame; i++) {
                        path.moveTo((i * xSize), center + graphValueList.get(i + diff) * ySize);
                        path.lineTo(((i + 1) * xSize), center + graphValueList.get((i + 1) + diff) * ySize);
                        canvas.drawPath(path, paint);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public void setStopOnDraw() {
        callOnDraw = false;
    }

    public void setStartOnDraw() {
        callOnDraw = true;
    }

    public void setGetValueList(ArrayList<Integer> getValueList) {
        this.graphValueList = getValueList;
        invalidate();
    }


    public boolean onTouchEvent(MotionEvent event) {
        mScaleDetector.onTouchEvent(event);

        final int action = event.getAction();
        switch (action & MotionEvent.ACTION_MASK) {
            // zoom in - touching by two finger
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_POINTER_UP: {

                if (event.getPointerCount() == 2) {
                    center = 0;
                    ySize = 35;
                    xSize = 120;
                }
                break;
            }
            // zoom out
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                ySize = 10;
                xSize = 100;
        }
        return true;
    }

    //gesture detector
    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mScaleFactor *= detector.getScaleFactor();
            // Don't let the object get too small or too large.
            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 5.0f));
            invalidate();
            return true;
        }
    }

}