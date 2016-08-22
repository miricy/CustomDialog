package com.yekertech.customdialog;

/******************************************************************
 * Copyright (C) 2016-2017 Yekertech INC.
 * All rights reserved
 * <p/>
 * Description:
 * <p/>
 * <p/>
 * Created by alva on 16-8-18.
 */
/**
 *author:alva
 *by yekertech.com
 */

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.SystemClock;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextClock;

import java.util.ArrayList;
import java.util.List;


public class LockScreenDialog extends Dialog {

    GradientTextView gtxtView=null;
    private List<View> mViewList = new ArrayList<>();//页卡视图集合
    ViewPager mViewPager;
    TextClock mTextClock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        Window window = getWindow();
        window.requestFeature(Window.FEATURE_NO_TITLE);
//        window.setType(WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG);
        window.setTitle("lock screen");

        final WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(params);
        window.setFlags(0, WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        window.setBackgroundDrawable(new ColorDrawable(0x10000000));

        LinearLayout rl=new LinearLayout(this.getContext());
        rl.layout(0,0,720,1280);
        rl.setOrientation(LinearLayout.VERTICAL);
        rl.setGravity(Gravity.CENTER);
        rl.setBackgroundColor(0xa0000000);
        LinearLayout.LayoutParams rllp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        rllp.setMargins(100,400,100,100);

        mTextClock=new TextClock(getContext());
        mTextClock.setTextSize(32);
        mTextClock.setTextColor(Color.WHITE);
        gtxtView=new GradientTextView(getContext(),null);
        gtxtView.setText(" 请滑动解锁 ");
        gtxtView.setPadding(50,10,50,10);

        gtxtView.setTextSize(48);
        RoundRectShape roundRectShape=new RoundRectShape(new float[]{20,20,20,20,20,20,20,20},new RectF(100,100,100,100),null);
        ShapeDrawable shapeDrawable=new ShapeDrawable(roundRectShape);
        shapeDrawable.getPaint().setStyle(Paint.Style.STROKE);
        shapeDrawable.getPaint().setColor(0x666666);
        shapeDrawable.setBounds(0,0,gtxtView.getWidth(),gtxtView.getHeight());
//        gtxtView.setBackground(shapeDrawable);
        gtxtView.setBackground(getContext().getResources().getDrawable(R.drawable.shape_lock));
        rl.addView(mTextClock,rllp);
        rl.addView(gtxtView,rllp);

        ImageView imageView=new ImageView(getContext());
        imageView.setBackgroundColor(0x10000000);
        imageView.setMinimumWidth(100);
        imageView.setMinimumHeight(100);

        mViewList.add(imageView);
        mViewList.add(rl);
        mViewPager=new ViewPager(getContext());
        mViewPager.layout(0,0,720,1280);
//        mViewPager.setBackgroundColor(0x00000000);
        mViewPager.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        MyPagerAdapter mAdapter = new MyPagerAdapter(mViewList);
        mViewPager.setAdapter(mAdapter);//给ViewPager设置适配器
        mViewPager.setCurrentItem(1);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                 if(position==0)
                 {
                     dismiss();
                 }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        FrameLayout ff=new FrameLayout(this.getContext());
        ff.layout(0,0,720,1280);
        FrameLayout.LayoutParams tvlp = new FrameLayout.LayoutParams(720,1280);
//                FrameLayout.LayoutParams.MATCH_PARENT,
//                FrameLayout.LayoutParams.MATCH_PARENT);
        tvlp.gravity=Gravity.CENTER;
        ff.addView(mViewPager,tvlp);


        this.setContentView(ff);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return true;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return true;
    }

    @Override
    public void onAttachedToWindow() {
        Log.i("CustomDialog","alva power off attached");
        super.onAttachedToWindow();
    }

    public LockScreenDialog(Context context) {
        super(context);
    }



    //ViewPager适配器
    class MyPagerAdapter extends PagerAdapter {
        private List<View> mViewList;

        public MyPagerAdapter(List<View> mViewList) {
            this.mViewList = mViewList;
        }

        @Override
        public int getCount() {
            return mViewList.size();//页卡数
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;//官方推荐写法
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mViewList.get(position));//添加页卡
            return mViewList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mViewList.get(position));//删除页卡
        }
    }

}

