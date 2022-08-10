package com.example.uisimplu;
//import the libraries related to graphics
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

//create the Canvas class which extends the base class View(defined in Android Studio)
public class Canvas extends View {

    //paint and path attributes related to graphics
    private Paint mPaint;
    private Paint mStrokePaint;
    private Path mArrowPath;
    private Paint mPP;
    //numerical attributes related to shapes dimensions
    private int cR = 10; //circle radius
    private int arrowR = 20; //arrow range
    private float mCurX = 3300;//x side length of rectangle
    private float mCurY = 3100;//y side length of rectangle
    private int mOrient;
    //a list designed for storing the points objects
    private List<PointF> mPointList = new ArrayList<>();
    protected android.graphics.Canvas canvas;
    private List<PointF> ppPoints = new ArrayList<>();


    //create constructors for Canvas class
    public Canvas(Context context) {
        this(context, null);
    }

    public Canvas(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Canvas(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);//inherited constructor parameters

        mPaint = new Paint();//paint object
        mPaint.setColor(Color.BLUE);//paint colour
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);

        mStrokePaint = new Paint(mPaint);
        mStrokePaint.setStyle(Paint.Style.STROKE);
        mStrokePaint.setStrokeWidth(5);

        mArrowPath = new Path();
        mArrowPath.arcTo(new RectF(-arrowR, -arrowR, arrowR, arrowR), 0, -180);
        mArrowPath.lineTo(0, -3 * arrowR);
        mArrowPath.close();


        mPP = new Paint();
        mPP.setColor(Color.RED);
        mPP.setAntiAlias(true);
        mPP.setStyle(Paint.Style.FILL);


    }





    //overridden method onDraw dedicated for drawing in canvas area
    @Override
    protected void onDraw(android.graphics.Canvas canvas) {
        for (PointF p : mPointList) {
            canvas.drawCircle(p.x, p.y, cR, mPaint);//drawing
        }
        for(PointF p : ppPoints){
            canvas.drawCircle(p.x, p.y, 15, mPP);
        }
        setCanvas(canvas);
        canvas.save();
        canvas.translate(mCurX, mCurY);
        canvas.rotate(mOrient);
        canvas.drawPath(mArrowPath, mPaint);
        canvas.drawArc(new RectF(-arrowR * 0.8f, -arrowR * 0.8f, arrowR * 0.8f, arrowR * 0.8f),
                0, 360, false, mStrokePaint);
        canvas.restore();


    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        mCurX = event.getX();
        mCurY = event.getY();
        invalidate();
        return true;
    }


    public void autoAddPoint(float stepLen) {
        mCurX += (float) (stepLen * Math.sin(Math.toRadians(mOrient)));
        mCurY += -(float) (stepLen * Math.cos(Math.toRadians(mOrient)));
        mPointList.add(new PointF(mCurX, mCurY));

        invalidate();
    }


    public void adPP(){

        ppPoints.add(new PointF(mCurX, mCurY));
        canvas.save();
        canvas.restore();
        invalidate();
    }

    //method designed for drawing starting from orient position
    public void autoDrawArrow(int orient) {
        mOrient = orient;
        invalidate();
    }
    public void setCanvas(android.graphics.Canvas canvas) {
        this.canvas = canvas;
    }

    public android.graphics.Canvas getCanvas() {
        return canvas;
    }

}
