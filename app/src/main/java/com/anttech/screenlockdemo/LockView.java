package com.anttech.screenlockdemo;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO: document your custom view class.
 */
public class LockView extends View {
    private Point[][] points = new Point[3][3];
    private Boolean initTAG = false;

    private Bitmap bitmapNORMAL;
    private Bitmap bitmapERROR;
    private Bitmap bitmapPRESS;
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint pressPaint = new Paint();
    private Paint errorPaint = new Paint();

    private float bitmapR;
    private ArrayList<Point> pointList = new ArrayList<Point>();
    private ArrayList<Integer> passList = new ArrayList<Integer>();
    private onDrawFinishListener listener;

    float mouseX, mouseY;
    private Boolean isDraw = false;

    public LockView(Context context) {
        this(context, null);
    }

    public LockView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LockView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void init() {
        bitmapERROR = BitmapFactory.decodeResource(getResources(), R.mipmap.error);
        bitmapNORMAL = BitmapFactory.decodeResource(getResources(), R.mipmap.normal);
        bitmapPRESS = BitmapFactory.decodeResource(getResources(), R.mipmap.press);

        pressPaint.setColor(Color.YELLOW);
        pressPaint.setStrokeWidth(5);

        errorPaint.setColor(Color.RED);
        errorPaint.setStrokeWidth(5);

        bitmapR = bitmapERROR.getHeight() / 2;

        int width = getWidth();
        int height = getHeight();
        int offset = Math.abs(width - height) / 2;
        int offsetX, offsetY;
        int space;
        if (width > height) {
            space = height / 4;
            offsetX = offset;
            offsetY = 0;
        } else {
            space = width / 4;
            offsetY = offset;
            offsetX = 0;
        }

        points[0][0] = new Point(offsetX + space, offsetY + space);
        points[0][1] = new Point(offsetX + space * 2, offsetY + space);
        points[0][2] = new Point(offsetX + space * 3, offsetY + space);

        points[1][0] = new Point(offsetX + space, offsetY + space * 2);
        points[1][1] = new Point(offsetX + space * 2, offsetY + space * 2);
        points[1][2] = new Point(offsetX + space * 3, offsetY + space * 2);

        points[2][0] = new Point(offsetX + space, offsetY + space * 3);
        points[2][1] = new Point(offsetX + space * 2, offsetY + space * 3);
        points[2][2] = new Point(offsetX + space * 3, offsetY + space * 3);

        initTAG = true;
    }

    public void drawPoint(Canvas canvas) {

        for (int i = 0; i < points.length; i++) {

            for (int j = 0; j < points[i].length; j++) {

                if (points[i][j].state == Point.STATE_NORMAL) {
                    canvas.drawBitmap(bitmapNORMAL, points[i][j].x - bitmapR, points[i][j].y - bitmapR, paint);

                } else if (points[i][j].state == Point.STATE_PRESS) {
                    canvas.drawBitmap(bitmapPRESS, points[i][j].x - bitmapR, points[i][j].y - bitmapR, paint);

                } else {
                    canvas.drawBitmap(bitmapERROR, points[i][j].x - bitmapR, points[i][j].y - bitmapR, paint);
                }
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (!initTAG) {
            init();
        }
        drawPoint(canvas);
        if (!pointList.isEmpty()) {
            Point a = pointList.get(0);
            for (int i = 1; i < pointList.size(); i++) {
                Point b = pointList.get(i);
                drawLine(canvas, a, b);
                a = b;
            }
            if (isDraw) {
                drawLine(canvas, a, new Point(mouseX, mouseY));
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mouseX = event.getX();
        mouseY = event.getY();
        int[] ij = new int[2];

        int i, j;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                resetPoints();
                ij = getSelectionPoint();
                if (ij != null) {
                    isDraw = true;
                    i = ij[0];
                    j = ij[1];

                    points[i][j].state = Point.STATE_PRESS;
                    pointList.add(points[i][j]);
                    passList.add(i * 3 + j);
                }
                break;

            case MotionEvent.ACTION_MOVE:
                if (isDraw) {
                    ij = getSelectionPoint();
                    if (ij != null) {
                        i = ij[0];
                        j = ij[1];
                        if (!pointList.contains(points[i][j])) {
                            points[i][j].state = Point.STATE_PRESS;
                            pointList.add(points[i][j]);
                            passList.add(i * 3 + j);
                        }
                    }
                }
                break;

            case MotionEvent.ACTION_UP:
                boolean value=false;
                if (listener!=null&&isDraw){
                    value=listener.onDrawFinish(passList);
                }
                if (!value){
                    for (Point p:pointList) {
                        p.state=Point.STATE_ERROR;
                    }
                }
                isDraw = false;
                break;
        }

        this.postInvalidate();
        return true;
    }

    public int[] getSelectionPoint() {
        Point pMouse = new Point(mouseX, mouseY);
        for (int i = 0; i < points.length; i++) {
            for (int j = 0; j < points[i].length; j++) {
                if (points[i][j].distance(pMouse) < bitmapR) {
                    int[] result = new int[2];
                    result[0] = i;
                    result[1] = j;
                    return result;
                }
            }
        }
        return null;
    }

    public void drawLine(Canvas canvas, Point a, Point b) {
        if (a.state == Point.STATE_PRESS) {
            canvas.drawLine(a.x, a.y, b.x, b.y, pressPaint);
        } else if (a.state == Point.STATE_ERROR) {
            canvas.drawLine(a.x, a.y, b.x, b.y, errorPaint);
        }
    }

    public void resetPoints() {
        pointList.clear();
        passList.clear();
        for (int i = 0; i < points.length; i++) {
            for (int j = 0; j < points[i].length; j++) {
                points[i][j].state = Point.STATE_NORMAL;
            }
        }
        invalidate();
    }

    public interface onDrawFinishListener {
        boolean onDrawFinish(List<Integer> passList);
    }

    public void setOnDrawFinishListener(onDrawFinishListener listener){
        this.listener=listener;
    }
}
