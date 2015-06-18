/**
*author:alva
*by yekertech.com
*/
package com.yekertech.customdialog;

//package com.android.internal.policy.impl;
//import com.android.internal.R;
//import android.view.WindowManagerPolicy.WindowManagerFuncs;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;


public class CustomDialog extends Dialog {
//	private final WindowManagerFuncs mWindowManagerFuncs;
	private AudioManager mAudioManager = null;
	private PowerManager mPowerManager=null;
	RadioSelectView mRadioView=null;
	private int maxVolume = 15;
	private int currentVolume = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

        Window window = getWindow();
        window.requestFeature(Window.FEATURE_NO_TITLE);
//        window.setType(WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG);
//        window.setFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM,
//                WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        window.setTitle("PowerOff");

		//this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        final WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(params);
        window.setFlags(0, WindowManager.LayoutParams.FLAG_DIM_BEHIND);

		mPowerManager=(PowerManager)this.getContext().getSystemService(Context.POWER_SERVICE);
		mAudioManager = (AudioManager)this.getContext().getSystemService(Context.AUDIO_SERVICE);
		this.maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		mRadioView=new RadioSelectView(getContext());
		FrameLayout ff=new FrameLayout(this.getContext());
		FrameLayout.LayoutParams tvlp = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT);
		tvlp.gravity=Gravity.CENTER;
        ff.setBackgroundColor(0x161a20);
		ff.addView(mRadioView, tvlp);
		
		mRadioView.initView(1,currentVolume*100/maxVolume);
		this.setContentView(ff);
		mRadioView.setOnSelectListener(new OnSelectChangedListener() {
			
			@Override
			public void OnSelect(int index) {
				// TODO Auto-generated method stub
				Log.i("CustomDialog","selected:"+index);
				CustomDialog.this.dismiss();
				switch(index)
				{
				case 1://待机
//					mPowerManager.goToSleep(SystemClock.uptimeMillis() );

					break;
				case 2://关机
//					mWindowManagerFuncs.shutdown(false);
					break;
				case 3://重启
					mPowerManager.reboot("");
					break;
				}
			}

			@Override
			public void OnSetVolume(int percent) {
				// TODO Auto-generated method stub
				mAudioManager.setStreamVolume(AudioManager.STREAM_SYSTEM, percent*maxVolume/100, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
			}
		});
	}

	@Override
	public void onAttachedToWindow() {
		Log.i("CustomDialog","alva power off attached");
		super.onAttachedToWindow();
		currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		mRadioView.updateVolume(currentVolume*100/maxVolume);
		mRadioView.setSelected(0);
	}
	
	public CustomDialog(Context context/*, WindowManagerFuncs windowManagerFuncs*/) {
		super(context);
//		mWindowManagerFuncs = windowManagerFuncs;
	}
	
	public interface OnSelectChangedListener{
		public void OnSelect(int index);
		public void OnSetVolume(int percent);
	}
	
	/**
	 * 单选组合视图
	 * @author alva
	 *
	 */
	@SuppressLint("ClickableViewAccessibility")
	public class RadioSelectView extends ImageView {

		private Bitmap mBackGround=null;
		private Bitmap mSelectBox = null;
		private int mCurSelect = 1;
		private int mCurVolume=30;
		private int mHorizontalSpan=0;
		private int mVerticalSpan=0;
		private int mOptionCount = 4;	
		private String[] txtInfo=null;
		private Paint  pt=null;
		
		private Bitmap mReboot=null;
		private Bitmap mRebootSelect=null;
		private Bitmap mShutdown=null;
		private Bitmap mShutdownSelect=null;
		private Bitmap mSuspend=null;
		private Bitmap mSuspendSelect=null;
		private Bitmap mVolume=null;
		private Bitmap mVolumeSelectMin=null;
		private Bitmap mVolumeSelectMax=null;
		private Bitmap mVolumeSlide=null;
		private Bitmap mVolumeAdd=null;
		private Bitmap mVolumeSub=null;
		private Bitmap mVolumeTips=null;
		
		private OnSelectChangedListener mOnSelect=null;
		
		
		public void setOnSelectListener(OnSelectChangedListener l){
			mOnSelect = l;
		}

		public RadioSelectView(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
			init();
		}

		public RadioSelectView(Context context, AttributeSet attrs) {
			super(context, attrs);
			// TODO Auto-generated constructor stub
			init();
		}

		public RadioSelectView(Context context, AttributeSet attrs, int defStyle) {
			super(context, attrs, defStyle);
			// TODO Auto-generated constructor stub
			init();
		}
		
		private void init()
		{
			pt = new Paint();
			pt.setTypeface(Typeface.DEFAULT);
			pt.setTextSize(18);
			pt.setColor(Color.WHITE);
			this.setFocusable(true);		
		}
		
		public void updateVolume(int volume)
		{
			mCurVolume=volume;
			this.invalidate();
		}
		
		public void setSelected(int selIndex)
		{
			if(selIndex<0)
			{
				mCurSelect = 0;
			}
			else if(selIndex >= mOptionCount)
			{
				mCurSelect=mOptionCount;
			}
			else
			{
				mCurSelect=selIndex;
			}
			this.invalidate();
		}
		
		public int getSelected()
		{
			return mCurSelect;
		}
		
		public void recycle()
		{
			if(mBackGround!=null) 
			  mBackGround.recycle();
			if(mSelectBox!=null)
			{
				mSelectBox.recycle();
			}
			
			mBackGround=null;
			mSelectBox=null;
			this.destroyDrawingCache();
		}
		
		public void initView(int defselect,int curvolume)
		{
			mCurSelect=defselect;
			mCurVolume=curvolume;
			mBackGround = BitmapFactory.decodeResource(getResources(), R.drawable.power_background);
			mReboot=BitmapFactory.decodeResource(getResources(), R.drawable.power_reboot);;
			mRebootSelect=BitmapFactory.decodeResource(getResources(), R.drawable.power_reboot_select);;
			mShutdown=BitmapFactory.decodeResource(getResources(), R.drawable.power_shutdown);;
			mShutdownSelect=BitmapFactory.decodeResource(getResources(), R.drawable.power_shutdown_select);;
			mSuspend=BitmapFactory.decodeResource(getResources(), R.drawable.power_suspend);;
			mSuspendSelect=BitmapFactory.decodeResource(getResources(), R.drawable.power_suspend_select);;
			mVolume=BitmapFactory.decodeResource(getResources(), R.drawable.power_volume);;
			mVolumeSelectMin=BitmapFactory.decodeResource(getResources(), R.drawable.power_volume_min);;
			mVolumeSelectMax=BitmapFactory.decodeResource(getResources(), R.drawable.power_volume_max);
			mVolumeSlide=BitmapFactory.decodeResource(getResources(), R.drawable.power_volume_slide);
			mVolumeAdd=BitmapFactory.decodeResource(getResources(), R.drawable.power_volume_add);;
			mVolumeSub=BitmapFactory.decodeResource(getResources(), R.drawable.power_volume_sub);;
			mVolumeTips=BitmapFactory.decodeResource(getResources(), R.drawable.power_volume_tips);;
			
			FrameLayout ff=((FrameLayout)this.getParent());
			if(ff!=null)
			{
			ff.removeView(this);
			FrameLayout.LayoutParams tvlp = new FrameLayout.LayoutParams(
	                FrameLayout.LayoutParams.WRAP_CONTENT,
	                FrameLayout.LayoutParams.WRAP_CONTENT);
			tvlp.gravity=Gravity.CENTER;
			tvlp.width=mBackGround.getWidth();
			tvlp.height=mBackGround.getHeight();
			ff.addView(this, tvlp);
			}
		}
		
		
		@Override
		protected void onDraw(Canvas canvas) {
			// TODO Auto-generated method stub
			super.onDraw(canvas);
			if(mBackGround != null)
			{
				canvas.drawBitmap(mBackGround, 0,0,null);
			}		
			if(mCurSelect==0)
			{
				int height=mVolumeSelectMin.getHeight()*(100-mCurVolume)/100;
				int width=mVolumeSelectMin.getWidth();
				canvas.drawBitmap(mVolumeAdd,432,303,null);
				canvas.drawBitmap(mVolumeSub,432,700,null);
				canvas.drawBitmap(mVolumeSlide, 514, 489,null);
				canvas.drawText(""+mCurVolume,560,536,  pt);
				canvas.drawBitmap(mVolumeSelectMax,448,348,null);
				canvas.drawBitmap(mVolumeSelectMin,new Rect(0,0,width,height),new Rect(448,348,width+448,height+348), null);
				canvas.drawBitmap(mVolumeTips,634,822,null);
			}
			else 
			{
				 canvas.drawBitmap(mVolume, 410,405, null);
			}
			if(mCurSelect==1) canvas.drawBitmap(mSuspendSelect, 711,405, null);
			else canvas.drawBitmap(mSuspend, 711,405, null);
			if(mCurSelect==2) canvas.drawBitmap(mShutdownSelect, 1012,405, null);
			else canvas.drawBitmap(mShutdown, 1012,405, null);
			if(mCurSelect==3) canvas.drawBitmap(mRebootSelect, 1313,405, null);
			else canvas.drawBitmap(mReboot, 1313,405, null);
			
			if(txtInfo!=null)
			{
				for(int i=0;i<txtInfo.length && i<mOptionCount;i++)
				{
					float len = pt.measureText(txtInfo[i]);
					int   start =(mSelectBox.getWidth()-(int)len)/2;
					if(start < 0) start = 0;				
					canvas.drawText(txtInfo[i], start+mHorizontalSpan+i*mSelectBox.getWidth(), 
							mVerticalSpan+mSelectBox.getHeight()-20, pt);
				}
			}
			
		}

//		@Override
//		public boolean onTouchEvent(MotionEvent event) {
//			// TODO Auto-generated method stub
//			float x=event.getX()-mHorizontalSpan;
////			float y=event.getY();
//			int pos =0;
//			switch(event.getAction())
//			{
//			   case MotionEvent.ACTION_MOVE:
//				   pos=(int)x/mSelectBox.getWidth();
//				   if(pos>=mOptionCount)
//				   {
//					   mCurSelect=mOptionCount-1;
//				   }
//				   else
//				   {
//					   mCurSelect=pos;
//				   }
//				   this.invalidate();
//				   break;
//			   case MotionEvent.ACTION_DOWN:
//				   pos=(int)x/mSelectBox.getWidth();
//				   if(pos>=mOptionCount)
//				   {
//					   mCurSelect=mOptionCount-1;
//				   }
//				   else
//				   {
//					   mCurSelect=pos;
//				   }
//				   this.invalidate();
//				   break;
//			   case MotionEvent.ACTION_UP:
//				   pos=(int)x/mSelectBox.getWidth();
//				   if(pos>=mOptionCount)
//				   {
//					   mCurSelect=mOptionCount-1;
//				   }
//				   else
//				   {
//					   mCurSelect=pos;
//				   }
//				   this.invalidate();
//				   if(mOnSelect != null)
//				   {
//					   mOnSelect.OnSelect(mCurSelect);
//				   }
//				   break;
//			}
//			return true;//super.onTouchEvent(event);
//		}	
		
		@Override
		public boolean onKeyDown(int keyCode,KeyEvent event){
			switch (keyCode) {		
			case KeyEvent.KEYCODE_DPAD_LEFT:
				mCurSelect--;
				if(mCurSelect<1) mCurSelect=0;
				this.invalidate();
				break;
			case KeyEvent.KEYCODE_DPAD_RIGHT:
				mCurSelect++;
				if(mCurSelect>=mOptionCount) mCurSelect=mOptionCount-1;
				this.invalidate();
				break;
			case KeyEvent.KEYCODE_DPAD_UP:
				if(mCurSelect==0)
				{
					if(mCurVolume<100) mCurVolume++;
					else mCurVolume=100;
					this.invalidate();
				}
				break;
			case KeyEvent.KEYCODE_DPAD_DOWN:
				if(mCurSelect==0)
				{
					if(mCurVolume>0) mCurVolume--;
					else mCurVolume=0;
					this.invalidate();
				}
				break;
				
			
			}
			return super.onKeyUp(keyCode, event);
		}

		@Override
		public boolean onKeyUp(int keyCode, KeyEvent event) {
			
			switch (keyCode) {
			case KeyEvent.KEYCODE_DPAD_CENTER:			
				  this.invalidate();
				   if(mOnSelect != null)
				   {
					   mOnSelect.OnSelect(mCurSelect);
				   }
				break;
//			case KeyEvent.KEYCODE_DPAD_LEFT:
//				mCurSelect--;
//				if(mCurSelect<0) mCurSelect=0;
//				this.invalidate();
//				break;
//			case KeyEvent.KEYCODE_DPAD_RIGHT:
//				mCurSelect++;
//				if(mCurSelect>=mOptionCount) mCurSelect=mOptionCount-1;
//				this.invalidate();
//				break;
			case KeyEvent.KEYCODE_DPAD_UP:			
			case KeyEvent.KEYCODE_DPAD_DOWN:
				if(mCurSelect==0)
				{
					 if(mOnSelect != null)
					   {
						   mOnSelect.OnSetVolume(mCurVolume);
					   }
				}
				break;
				
			
			}
			return super.onKeyUp(keyCode, event);
		}
		
		public boolean postSelect(int select)
		{
			if(mOnSelect != null)
			   {
				   mOnSelect.OnSelect(select);
			   }
			return true;
		}
		
		public boolean postSelect()
		{
			if(mOnSelect != null)
			   {
				   if(mCurSelect<0) mCurSelect=0;
				   mOnSelect.OnSelect(mCurSelect);
			   }
			return true;
		}
	}

}
