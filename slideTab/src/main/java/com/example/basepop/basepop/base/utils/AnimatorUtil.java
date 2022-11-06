package com.example.basepop.basepop.base.utils;

import android.animation.ValueAnimator;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

public class AnimatorUtil {
    public static ValueAnimator createCLValueAnimator(final View v, int start, int end,boolean isH) {   //变长动画
        ValueAnimator valueAnimator = ValueAnimator.ofInt(start, end);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = v.getLayoutParams();
                if (isH){
                    layoutParams.height = value;
                }else {
                    layoutParams.width = value;
                }

                v.setLayoutParams(layoutParams);
            }
        });
        return valueAnimator;
    }

    /**
     * 根据fraction值来计算当前的颜色。
     */
    public static int getCurrentColor(float fraction, int startColor, int endColor) {

        return Color.argb((int) (Color.alpha(startColor) + fraction * (Color.alpha(endColor) - Color.alpha(startColor)))
                , (int) (Color.red(startColor) + fraction * (Color.red(endColor) - Color.red(startColor)))
                ,  (int) (Color.green(startColor) + fraction * (Color.green(endColor) - Color.green(startColor))),
                (int) (Color.blue(startColor) + fraction * (Color.blue(endColor) - Color.blue(startColor))));
    }


    public static  void showAnimate2(boolean open, View view){   //改变大小

        ValueAnimator valueAnimatorX=ValueAnimator.ofFloat(open?1f:0.88f,open?0.88f:1f);
        valueAnimatorX.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                view.setScaleX((float) valueAnimator.getAnimatedValue());
                view.setScaleY((float) valueAnimator.getAnimatedValue());
            }
        });
        valueAnimatorX.setDuration(200).start();

    }

    public static void setClickA(View view){  //点击动画
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                view.getParent().requestDisallowInterceptTouchEvent(false);
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:

                        showAnimate2(true,view);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        showAnimate2(false,view);
                        break;
                }
                return false;
            }
        });
    }


    public static interface Onclick{
        void onClick(View view);
    }

    public void makeTranslationAnimate(){
        //TransitionManager.beginDelayedTransition((ViewGroup) LoadingView.this.getParent(),new Fade().setDuration(200));
    }

}
