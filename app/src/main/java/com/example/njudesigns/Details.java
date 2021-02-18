package com.example.njudesigns;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MotionEventCompat;

import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.Log;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.njudesigns.R;
import com.squareup.picasso.Picasso;

import static android.view.MotionEvent.INVALID_POINTER_ID;

public class Details extends AppCompatActivity  implements View.OnTouchListener {
    ImageView zoom_img;
    Integer poster;
    private ScaleGestureDetector scaleGestureDetector;
    private float mScaleFactor = 1.0f;
    private int mActivePointerId = INVALID_POINTER_ID;
//    float mLastTouchX;
//    float mLastTouchY;
//    float mPosX;
//    float mPosY;
private static final String TAG = "Touch";

    private static final float MIN_ZOOM = 1.0f;
    private static final float MAX_ZOOM = 5.0f;
    // These matrices will be used to move and zoom image
    Matrix matrix = new Matrix();
    Matrix savedMatrix = new Matrix();

    // We can be in one of these 3 states
    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    int mode = NONE;

    // Remember some things for zooming
    PointF start = new PointF();
    PointF mid = new PointF();
    float oldDist = 1f;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_details);
        zoom_img = findViewById(R.id.details_image);

        Bundle arguments=getIntent().getExtras();
        poster = Integer.valueOf(arguments.getString("imageUrl"));
        Picasso.with(this).load(poster).into(zoom_img);
        //scaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());
        zoom_img.setScaleType(ImageView.ScaleType.FIT_CENTER);
        zoom_img.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch (View v, MotionEvent event){
        ImageView zoom_img = (ImageView) v;

        zoom_img.setScaleType(ImageView.ScaleType.MATRIX);
        float scale;

        // Dump touch event to log
        dumpEvent(event);

        // Handle touch events here...
        switch (event.getAction() & MotionEvent.ACTION_MASK) {

            case MotionEvent.ACTION_DOWN: //first finger down only
                savedMatrix.set(matrix);
                start.set(event.getX(), event.getY());
                Log.d(TAG, "mode=DRAG" );
                mode = DRAG;
                break;
            case MotionEvent.ACTION_UP: //first finger lifted
            case MotionEvent.ACTION_POINTER_UP: //second finger lifted
                mode = NONE;
                Log.d(TAG, "mode=NONE" );
                break;
            case MotionEvent.ACTION_POINTER_DOWN: //second finger down
                oldDist = spacing(event);
                Log.d(TAG, "oldDist=" + oldDist);
                if (oldDist > 5f) {
                    savedMatrix.set(matrix);
                    midPoint(mid, event);
                    mode = ZOOM;
                    Log.d(TAG, "mode=ZOOM" );
                }
                break;

            case MotionEvent.ACTION_MOVE:
                if (mode == DRAG) { //movement of first finger
                    matrix.set(savedMatrix);
                    if (zoom_img.getLeft() >= -392){
                        matrix.postTranslate(event.getX() - start.x, event.getY() - start.y);
                    }
                }
                else if (mode == ZOOM) { //pinch zooming
                    float newDist = spacing(event);
                    Log.d(TAG, "newDist=" + newDist);
                    if (newDist > 5f) {
                        matrix.set(savedMatrix);
                        scale = newDist / oldDist; //thinking i need to play around with this value to limit it**
                        matrix.postScale(scale, scale, mid.x, mid.y);
                    }
                }
                break;
        }

        // Perform the transformation
        zoom_img.setImageMatrix(matrix);

        return true; // indicate event was handled
    }

    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float)Math.sqrt(x * x + y * y);
    }

    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

    /** Show an event in the LogCat view, for debugging */
    private void dumpEvent(MotionEvent event) {
        String names[] = { "DOWN" , "UP" , "MOVE" , "CANCEL" , "OUTSIDE" ,
                "POINTER_DOWN" , "POINTER_UP" , "7?" , "8?" , "9?" };
        StringBuilder sb = new StringBuilder();
        int action = event.getAction();
        int actionCode = action & MotionEvent.ACTION_MASK;
        sb.append("event ACTION_" ).append(names[actionCode]);
        if (actionCode == MotionEvent.ACTION_POINTER_DOWN
                || actionCode == MotionEvent.ACTION_POINTER_UP) {
            sb.append("(pid " ).append(
                    action >> MotionEvent.ACTION_POINTER_ID_SHIFT);
            sb.append(")" );
        }
        sb.append("[" );
        for (int i = 0; i < event.getPointerCount(); i++) {
            sb.append("#" ).append(i);
            sb.append("(pid " ).append(event.getPointerId(i));
            sb.append(")=" ).append((int) event.getX(i));
            sb.append("," ).append((int) event.getY(i));
            if (i + 1 < event.getPointerCount())
                sb.append(";" );
        }
        sb.append("]" );
        Log.d(TAG, sb.toString());
    }
    }




//    @Override
//    public boolean onTouchEvent(MotionEvent ev) {
//        scaleGestureDetector.onTouchEvent(ev);
//        final int action = MotionEventCompat.getActionMasked(ev);
//
//        switch (action) {
//            case MotionEvent.ACTION_DOWN:
//
//            {
//                final int pointerIndex = MotionEventCompat.getActionIndex(ev);
//                final float x = MotionEventCompat.getX(ev, pointerIndex);
//                final float y = MotionEventCompat.getY(ev, pointerIndex);
//
//                // Remember where we started (for dragging)
//                mLastTouchX = x;
//                mLastTouchY = y;
//                // Save the ID of this pointer (for dragging)
//                mActivePointerId = MotionEventCompat.getPointerId(ev, 0);
//                break;
//            }
//
//            case MotionEvent.ACTION_MOVE: {
//                // Find the index of the active pointer and fetch its position
//                final int pointerIndex =
//                        MotionEventCompat.findPointerIndex(ev, mActivePointerId);
//
//                final float x = MotionEventCompat.getX(ev, pointerIndex);
//                final float y = MotionEventCompat.getY(ev, pointerIndex);
//
//                // Calculate the distance moved
//                final float dx = x - mLastTouchX;
//                final float dy = y - mLastTouchY;
//
//                mPosX += dx;
//                mPosY += dy;
//
//                //invalidate();
//
//                // Remember this touch position for the next move event
//                mLastTouchX = x;
//                mLastTouchY = y;
//
//                break;
//            }
//
//            case MotionEvent.ACTION_UP: {
//                mActivePointerId = INVALID_POINTER_ID;
//                break;
//            }
//
//            case MotionEvent.ACTION_CANCEL: {
//                mActivePointerId = INVALID_POINTER_ID;
//                break;
//            }
//
//            case MotionEvent.ACTION_POINTER_UP: {
//
//                final int pointerIndex = MotionEventCompat.getActionIndex(ev);
//                final int pointerId = MotionEventCompat.getPointerId(ev, pointerIndex);
//
//                if (pointerId == mActivePointerId) {
//                    // This was our active pointer going up. Choose a new
//                    // active pointer and adjust accordingly.
//                    final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
//                    mLastTouchX = MotionEventCompat.getX(ev, newPointerIndex);
//                    mLastTouchY = MotionEventCompat.getY(ev, newPointerIndex);
//                    mActivePointerId = MotionEventCompat.getPointerId(ev, newPointerIndex);
//                }
//                break;
//            }
//        }
//        return true;
//    }

//    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
//        @Override
//        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
//            mScaleFactor *= scaleGestureDetector.getScaleFactor();
//            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 10.0f));
//            zoom_img.setScaleX(mScaleFactor);
//            zoom_img.setScaleY(mScaleFactor);
//            return true;
//        }
//    }