
package com.nextnut.xyreader_udacity.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import android.widget.ImageView;

import com.nextnut.xyreader_udacity.R;

public final class CustomImageView extends ImageView {

    private float aspectRatio = 0;


    public CustomImageView(Context context) {
        super(context);
    }

    public CustomImageView(Context context, AttributeSet attrs) {
        super(context, attrs);

        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomImageView);
        aspectRatio = a.getFloat(R.styleable.CustomImageView_aspectRatio, 0);
        a.recycle();
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        float localRatio = aspectRatio;


        if (localRatio == 0.0) {
            super.onMeasure(widthSpec, heightSpec);
        } else {
            int frameWidth = MeasureSpec.getSize(widthSpec);
            int frameHeight = MeasureSpec.getSize(heightSpec);

            // Get the padding of the border background.
            int horizontalPadding = getPaddingLeft() + getPaddingRight();
            int verticalPadding = getPaddingTop() + getPaddingBottom();

            // Resize the preview frame with correct aspect ratio.
            frameWidth -= horizontalPadding;
            frameHeight -= verticalPadding;

            if (frameHeight > 0 && (frameWidth > frameHeight * localRatio)) {
                frameWidth = (int) (frameHeight * localRatio + .5);
            } else {
                frameHeight = (int) (frameWidth / localRatio + .5);
            }

            // Add the padding of the border.
            frameWidth += horizontalPadding;
            frameHeight += verticalPadding;

            // Ask children to follow the new preview dimension.
            super.onMeasure(MeasureSpec.makeMeasureSpec(frameWidth, MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(frameHeight, MeasureSpec.EXACTLY));
        }
    }


    public void setAspectRatio(float aspectRatio) {
        if (aspectRatio <= 0.0) {
            throw new IllegalArgumentException(
                    "aspect ratio must be positive");
        }

        if (this.aspectRatio != aspectRatio) {
            this.aspectRatio = aspectRatio;
            requestLayout();
        }
    }



}
