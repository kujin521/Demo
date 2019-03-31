package com.kj.myimagviewdemo;

import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.FloatMath;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    Matrix m,oldM;
    ImageView imageview;
    PointF start=new PointF();
    PointF mid=new PointF();
    static final int DRAG=1;
    static final int ZOOM=2;
    static final int NONE=3;
    int mode=NONE;
    float oldDist;

    float[] values=new float[10];
    private GestureDetector detector;

    float lastScale = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        ScaleImageView imageView = new ScaleImageView(this);
//        imageView.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.googlelogo_color_272x92dp));

//        setContentView(imageView);

        imageview = findViewById(R.id.imageView1);
        m = new Matrix(imageview.getImageMatrix());
        oldM = new Matrix(imageview.getImageMatrix());

        this.setListener();

    }

    private void setListener() {
        detector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener());
        detector.setOnDoubleTapListener(new GestureDetector.OnDoubleTapListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                return false;
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                Log.e(TAG, "onDoubleTap: " + imageview.getScaleX() );
//                m.postScale(1, 1, mid.x, mid.y);
                if (imageview.getScaleX() != 1) {
                    imageview.setScaleY(1);
                    imageview.setScaleX(1);
                } else {
                    imageview.setScaleY(1.5f);
                    imageview.setScaleX(1.5f);
                }
                imageview.setImageMatrix(m);
                return false;
            }

            @Override
            public boolean onDoubleTapEvent(MotionEvent e) {
                return false;
            }
        });

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        imageview.setScaleType(ImageView.ScaleType.MATRIX);
        detector.onTouchEvent(event);
        switch(event.getAction() & MotionEvent.ACTION_MASK)
        {
            case MotionEvent.ACTION_DOWN:
                start.set(event.getX(),event.getY());
                m.set(imageview.getImageMatrix());
                oldM.set(imageview.getImageMatrix());
                mode=DRAG;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                oldDist=spacing(event);
                if(oldDist>10f)
                {
                    mid=mid(event);
                    oldM.set(m);
                    mode=ZOOM;
                    lastScale = imageview.getScaleX();
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_UP:
                mode=NONE;
                break;
            case MotionEvent.ACTION_MOVE:
                if(mode==DRAG)
                {
                    m.set(oldM);
                    m.postTranslate(event.getX()-start.x, event.getY()-start.y);
                    imageview.setImageMatrix(m);
                }
                else if(mode==ZOOM)
                {
                    float newDist=spacing(event);
                    if(newDist>10f)
                    {
                        float scale= lastScale - 1 + newDist/oldDist;

                        if (scale <= 1) {
                            scale = 1;
                        }

                        imageview.setScaleX(scale);
                        imageview.setScaleY(scale);
                    }
                }
                break;
        }
        return false;
    }

    private static final String TAG = "MainActivity";

    public float spacing(MotionEvent event)
    {
        float x=event.getX(0)-event.getX(1);
        float y=event.getY(0)-event.getY(1);
        return (float) Math.sqrt(x*x+y*y);
    }

    public PointF mid(MotionEvent event)
    {
        PointF mid=new PointF();
        float x=event.getX(0)+event.getX(1);
        float y=event.getY(0)+event.getY(1);
        mid.set(x/2,y/2);
        return mid;
    }

}
