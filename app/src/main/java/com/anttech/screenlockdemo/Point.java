package com.anttech.screenlockdemo;

/**
 * Created by Hyper on 2016/5/18.
 */
public class Point {
    public static final int STATE_NORMAL=0;
    public static final int STATE_PRESS=1;
    public static final int STATE_ERROR=2;

   int state=STATE_NORMAL;
   float x;
   float y;

    public Point(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float distance(Point a){
        float distance=(float)Math.sqrt((x-a.x)*(x-a.x)+(y-a.y)+(y-a.y));
        return distance;
    }
}
