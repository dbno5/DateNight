package com.billyji.datenight.ui;

import android.content.Context;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ViewFlipper;

import com.billyji.datenight.R;
import com.billyji.datenight.ui.ViewIndicator.CirclePageIndicator;

public class OnSwipeTouchListener implements OnTouchListener {

    private final GestureDetector gestureDetector;
    private ViewFlipper viewFlipper;
    private CirclePageIndicator circlePageIndicator;
    private Context context;

    public OnSwipeTouchListener (Context ctx, ViewFlipper viewFlipper, CirclePageIndicator circlePageIndicator){
        gestureDetector = new GestureDetector(ctx, new GestureListener());
        this.viewFlipper = viewFlipper;
        this.circlePageIndicator = circlePageIndicator;
        context = ctx;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    private final class GestureListener extends SimpleOnGestureListener {
        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            boolean result = false;
            try {
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            onSwipeRight();
                        } else {
                            onSwipeLeft();
                        }
                        result = true;
                    }
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return result;
        }
    }

    public void onSwipeRight() {
        int displayedChild = viewFlipper.getDisplayedChild();
        if(displayedChild != 2) {
            viewFlipper.setInAnimation(context, R.anim.in_from_left);
            viewFlipper.setOutAnimation(context, R.anim.out_from_right);
            viewFlipper.showNext();
            circlePageIndicator.setCurrentDisplayedChild(displayedChild + 1);
        }
    }

    public void onSwipeLeft() {
        int displayedChild = viewFlipper.getDisplayedChild();
        if(displayedChild != 0) {
            viewFlipper.setInAnimation(context, R.anim.in_from_right);
            viewFlipper.setOutAnimation(context, R.anim.out_from_left);
            viewFlipper.showPrevious();
            circlePageIndicator.setCurrentDisplayedChild(displayedChild - 1);
        }
    }
}