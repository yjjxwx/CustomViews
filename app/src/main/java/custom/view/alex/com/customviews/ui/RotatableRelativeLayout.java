/*
 * Copyright (C) 2014 The LEWA OS Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package custom.view.alex.com.customviews.ui;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import custom.view.alex.com.customviews.util.Gusterpolator;

/**
 * Define a relative layout can rotate its content view.
 * <
 * Author: Alex Liu
 * Date: 15-1-23
 * Version: 1.0
 */
public class RotatableRelativeLayout extends RelativeLayout implements Rotatable{

    public RotatableRelativeLayout(Context context) {
        super(context);
    }

    public RotatableRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RotatableRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private ValueAnimator mAnimator = null;
    private float mCurrentDegree = 0;
    private final static int ANIMATION_SPEED = 270;//270
    private float mTargetDegree = 0;

    @Override
    public void setOrientation(int degree, boolean animation) {
        if (degree == 270) {
            degree = 90;
        } else if (degree == 90) {
            degree = 270;
        }
        if (mCurrentDegree == degree) {
            return;
        }
        if (mAnimator != null) {
            mAnimator.end();
        }
        mTargetDegree = degree;
        float diff = mTargetDegree - mCurrentDegree;
        diff = diff >= 0 ? diff : 360 + diff; // make it in range [0, 359]

        // Make it in range [-179, 180]. That's the shorted distance between the
        // two angles
        diff = diff > 180 ? diff - 360 : diff;

        mTargetDegree = mCurrentDegree +  diff;

        mAnimator = ObjectAnimator.ofFloat(this,"rotation", mCurrentDegree, mTargetDegree);
        mCurrentDegree = mTargetDegree;
        mAnimator.setDuration(ANIMATION_SPEED);
        mAnimator.setInterpolator(Gusterpolator.INSTANCE);
        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mCurrentDegree =(float) ((ValueAnimator) animation).getAnimatedValue();
                setRotation(mCurrentDegree);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mAnimator.start();
    }
}
