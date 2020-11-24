package com.example.zipperlayoutdemo;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.window.DisplayFeature;
import androidx.window.WindowLayoutInfo;
import androidx.window.WindowManager;

import java.util.List;

/**
 * Simple dual-screen layout based on LinearLayout. On a single screen it behaves normally,
 * but when spanned across two screens the child elements will be split across both screens.
 * Choose which elements appear on the right by adding an attribute to the layout:
 *   app:layout_rightSpanned="true"
 */
public class ZipperLayout extends LinearLayout {
    static final String TAG = "ZipperLayout";

    WindowManager wm;

    public ZipperLayout(Context context) {
        super(context);
    }

    public ZipperLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ZipperLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        wm = new WindowManager(getContext(),null);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        wm=null;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ZipperLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        int rr=r;

        WindowLayoutInfo windowLayoutInfo=null;

        int splitR=0;
        boolean split=false;
        if (wm != null){
            windowLayoutInfo = wm.getWindowLayoutInfo();

            List<DisplayFeature> displayFeatures = windowLayoutInfo.getDisplayFeatures();

            if(displayFeatures.size() > 0)
            {
                float ratio = (float)displayFeatures.get(0).getBounds().width()/(float)displayFeatures.get(0).getBounds().height();
                if (ratio < 1.0f) {
                    rr = (rr * 2) + displayFeatures.get(0).getBounds().width();
                    splitR = r+displayFeatures.get(0).getBounds().width();
                    split = true;
                }
            }
        }

        Log.i(TAG,"onLayout rr "+rr+" " + changed+","+l+","+t+","+r+","+b);
        setRight(rr);

        int leftHeight=0,rightHeight=0;
        for(int i=0;i<getChildCount();i++)
        {
            final View childAt = this.getChildAt(i);
            final LayoutParams lp = (LayoutParams) childAt.getLayoutParams();

            if (lp.isRightSpanned() && split) {
                int right = childAt.getRight();
                childAt.setLeft(childAt.getLeft() + ( splitR));
                childAt.setRight(right + (splitR));
                int height = childAt.getHeight();
                childAt.setTop(rightHeight);
                childAt.setBottom(rightHeight+height);
                rightHeight += height;
            } else {
                int height = childAt.getHeight();
                childAt.setTop(leftHeight);
                childAt.setBottom(leftHeight+height);
                leftHeight += height;

            }
            Log.i(TAG,"onLayout Children " + i + " " + childAt.getTop() + "," +childAt.getLeft()  );
        }

        Log.i(TAG,"onLayout right " + getRight()  );
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width=widthMeasureSpec;
        int height=heightMeasureSpec;

        WindowLayoutInfo windowLayoutInfo=null;

        if (wm != null){
            windowLayoutInfo = wm.getWindowLayoutInfo();
            List<DisplayFeature> displayFeatures = windowLayoutInfo.getDisplayFeatures();
            if(displayFeatures.size() >0)
            {
                //wide (1350, 0 - 1434, 1800)  tall (0, 1350 - 1800, 1434)
                Rect hinge = displayFeatures.get(0).getBounds();
                if (hinge.left > 0) {   // ignore the hinge in tall/dual-landscape mode, just a regular vertical LinearLayout
                    // set the width spec to the width of a single screen. assumes the control is full-screen, or at least centered on the hinge.
                    width = MeasureSpec.makeMeasureSpec(displayFeatures.get(0).getBounds().left, MeasureSpec.getMode(widthMeasureSpec));
                    // we have roughly twice the height to fill with children, because they're only half as wide as the view (assuming the developer has chosen for them to be split evenly left & right)
                    height = MeasureSpec.makeMeasureSpec(height * 2, MeasureSpec.getMode(heightMeasureSpec));
                }
            }
        }
        super.onMeasure(width, height);

        Log.i(TAG,"Measure " + MeasureSpec.toString(width) + " , " + heightMeasureSpec + " chi "+getChildCount());
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.i(TAG,"onDraw " + canvas.getWidth() + " , " + canvas.getHeight());
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new LayoutParams(this.getContext(), attributeSet);
    }

    @Override
    protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    public static class LayoutParams extends android.widget.LinearLayout.LayoutParams  {
        @ViewDebug.ExportedProperty(mapping = {
                @ViewDebug.IntToString(from = Gravity.NO_GRAVITY, to = "NONE"),
                @ViewDebug.IntToString(from = Gravity.TOP, to = "TOP"),
                @ViewDebug.IntToString(from = Gravity.BOTTOM, to = "BOTTOM"),
                @ViewDebug.IntToString(from = Gravity.LEFT, to = "LEFT"),
                @ViewDebug.IntToString(from = Gravity.RIGHT, to = "RIGHT"),
                @ViewDebug.IntToString(from = Gravity.CENTER_VERTICAL, to = "CENTER_VERTICAL"),
                @ViewDebug.IntToString(from = Gravity.FILL_VERTICAL, to = "FILL_VERTICAL"),
                @ViewDebug.IntToString(from = Gravity.CENTER_HORIZONTAL, to = "CENTER_HORIZONTAL"),
                @ViewDebug.IntToString(from = Gravity.FILL_HORIZONTAL, to = "FILL_HORIZONTAL"),
                @ViewDebug.IntToString(from = Gravity.CENTER, to = "CENTER"),
                @ViewDebug.IntToString(from = Gravity.FILL, to = "FILL")
        })

        private boolean rightSpanned = false;
        private int gravity = Gravity.NO_GRAVITY;
        private float weight = -1.0f;

        public LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            this.readStyleParameters(context, attributeSet);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(ViewGroup.LayoutParams layoutParams) {
            super(layoutParams);
        }

        private void readStyleParameters(Context context, AttributeSet attributeSet) {
            TypedArray a = context.obtainStyledAttributes(attributeSet, R.styleable.ZipperLayout_LayoutParams);
            try {
                this.rightSpanned = a.getBoolean(R.styleable.ZipperLayout_LayoutParams_layout_rightSpanned, false);
                this.gravity = a.getInt(R.styleable.ZipperLayout_LayoutParams_android_layout_gravity, Gravity.NO_GRAVITY);
                this.weight = a.getFloat(R.styleable.ZipperLayout_LayoutParams_layout_weight, -1.0f);
            } finally {
                a.recycle();
            }
        }

        public int getGravity() {
            return gravity;
        }

        public void setGravity(int gravity) {
            this.gravity = gravity;
        }

        public float getWeight() {
            return weight;
        }

        public void setWeight(float weight) {
            this.weight = weight;
        }

        public boolean isRightSpanned() {
            return rightSpanned;
        }

        public void setRightSpanned(boolean rightSpanned) {
            this.rightSpanned = rightSpanned;
        }
    }
}
