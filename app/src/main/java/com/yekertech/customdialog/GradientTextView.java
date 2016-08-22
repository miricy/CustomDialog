package com.yekertech.customdialog;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by alva on 16-7-15.
 */
public class GradientTextView extends TextView
{

    private LinearGradient mLinearGradient;
    private Matrix mGradientMatrix;
    private Paint mPaint;
    private Paint mPaint1;
    private int mViewWidth = 0;
    private int mViewHeight = 0;
    private int mTranslate = 0;

    private boolean mAnimating = true;

    public GradientTextView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        mPaint1=new Paint();
        mPaint1.setColor(0x666666);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);
        if (mViewWidth == 0)
        {
            //getWidth得到是某个view的实际尺寸.
            //getMeasuredWidth是得到某view想要在parent view里面占的大小.
            mViewWidth = getMeasuredWidth();
            mViewHeight = getMeasuredHeight();
            if (mViewWidth > 0)
            {
                mPaint = getPaint();
                //线性渐变
                mLinearGradient = new LinearGradient(-mViewWidth, 0, 0, 0, new int[]
                        { 0xff000000, 0xffffffff, 0xff000000 }, new float[]
                        { 0, 0.5f, 1 }, Shader.TileMode.CLAMP);
                mPaint.setShader(mLinearGradient);
                mGradientMatrix = new Matrix();

            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
//        canvas.drawRoundRect(0,0,mViewWidth,mViewHeight,mViewHeight/2,mViewHeight/2,mPaint1);
//        canvas.save();
//        RoundRectShape roundRectShape=new RoundRectShape(new float[]{20,20,20,20,20,20,20,20},null,null);
//        ShapeDrawable shapeDrawable=new ShapeDrawable(roundRectShape);
//        shapeDrawable.getPaint().setColor(0xffff0000);
//        shapeDrawable.setBounds(0,0,mViewWidth,mViewHeight);
//        shapeDrawable.draw(canvas);
//        canvas.restore();
        if (mAnimating && mGradientMatrix != null)
        {
            mTranslate += mViewWidth / 10;
            if (mTranslate > 2 * mViewWidth)
            {
                mTranslate = -mViewWidth;
            }
            mGradientMatrix.setTranslate(mTranslate, 0);
            mLinearGradient.setLocalMatrix(mGradientMatrix);
            //50ms刷新一次
            postInvalidateDelayed(100);
        }
    }

}
