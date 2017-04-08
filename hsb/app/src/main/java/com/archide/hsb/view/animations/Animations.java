package com.archide.hsb.view.animations;

import android.view.animation.Animation;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.TranslateAnimation;

/**
 * Created by Nithish on 05/04/17.
 */

public class Animations {

    public Animation fromAtoB(float fromX, float fromY, float toX, float toY, Animation.AnimationListener l, int speed){


        Animation fromAtoB = new TranslateAnimation(
                Animation.ABSOLUTE, //from xType
                fromX,
                Animation.ABSOLUTE, //to xType
                toX,
                Animation.ABSOLUTE, //from yType
                fromY,
                Animation.ABSOLUTE, //to yType
                toY
        );

        fromAtoB.setDuration(speed);
        fromAtoB.setInterpolator(new AnticipateOvershootInterpolator(1.0f));


        if(l != null)
            fromAtoB.setAnimationListener(l);
        return fromAtoB;
    }


    Animation.AnimationListener animL = new Animation.AnimationListener() {

        @Override
        public void onAnimationStart(Animation animation) {
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            //this is just a method call you can create to delete the animated view or hide it until you need it again.
           // clearAnimation();
        }
    };

}
