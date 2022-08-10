package com.example.uisimplu;
{

    //paint and path attributes related to graphics
    private Paint mPaint;
    private Paint mStrokePaint;
    private Path mArrowPath;

    //numerical attributes related to shapes dimensions
    private int cR = 10; //circle radius
    private int arrowR = 20; //arrow range
    private float mCurX = 3300;//x side length of rectangle
    private float mCurY = 3100;//y side length of rectangle
    private int mOrient;
    //a list designed for storing the points objects
    private List<PointF> mPointList = new ArrayList<>();


    private Paint background;

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


    }





    //overridden method onDraw dedicated for drawing in canvas area
    @Override
    protected void onDraw(android.graphics.Canvas canvas) {
        for (PointF p : mPointList) {
            canvas.drawCircle(p.x, p.y, cR, mPaint);//drawing
        }
        canvas.save();
        canvas.translate(mCurX, mCurY);
        canvas.rotate(mOrient);
        canvas.drawPath(mArrowPath, mPaint);
        canvas.drawArc(new RectF(-arrowR * 0.8f, -arrowR * 0.8f, arrowR * 0.8f, arrowR * 0.8f),
                0, 360, false, mStrokePaint);
        canvas.restore();
    }

    //overridden method onTouchEvent which want to know if the canvas is touched based on motion event
    @Override
    public boolean onTouchEvent(MotionEvent event) {

//        mCurX = event.getX();
//        mCurY = event.getY();
//        invalidate();
      return true;
    }


    public void autoAddPoint(float stepLen) {
        mCurX += (float) (stepLen * Math.sin(Math.toRadians(mOrient)));
        mCurY += -(float) (stepLen * Math.cos(Math.toRadians(mOrient)));
        mPointList.add(new PointF(mCurX, mCurY));
        invalidate();
    }

    //method designed for drawing starting from orient position
    public void autoDrawArrow(int orient) {
        mOrient = orient;
        invalidate();
    }

}
