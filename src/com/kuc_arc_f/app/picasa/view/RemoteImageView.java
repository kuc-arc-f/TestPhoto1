package com.kuc_arc_f.app.picasa.view;

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

import com.kuc_arc_f.app.picasa.db.ImageCacheDB;
import com.kuc_arc_f.app.picasa.db.ImageCacheDB.CacheColumn;
import com.kuc_arc_f.app.picasa.ItemPT;

//abstract public class RemoteImageView extends ImageView implements AnimationListener {
abstract public class RemoteImageView extends ImageView{
    private static final String TAG = "RemoteImageView";
    
    public static final int IMG_DOWNLOADING = 1;
    
    public final Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            // 1秒毎にキャッシュからヒットするか検索する
            // ヒットしたら、検索をやめる
            // 画像表示に10回挑戦してダメだったらNG
            if (msg.what == IMG_DOWNLOADING) {
                Context cxt = getContext();
                final ItemPT item = (ItemPT)msg.obj;
                final String s_id = item.getId().toString();
//Log.d(TAG, "s_id=" +s_id);
                int count = msg.arg1;
                ImageCacheDB db = ImageCacheDB.getInstance(cxt);
                if (s_id != null && !s_id.equals("")) {
                    final Cursor c = db.existsFile_byId(s_id);
                    if (c.moveToFirst()) {
                        final String filename = c.getString(c.getColumnIndex(CacheColumn.NAME));
                        final String type = c.getString(c.getColumnIndex(CacheColumn.TYPE));
                        if (type.equals("image/jpg")
                                || type.equals("image/jpeg")
                                || type.equals("image/png")
                                || type.equals("image/gif")) {
//Log.d(TAG, "type.equals=OK");
                            Drawable drawable = Drawable.createFromPath(cxt.getFileStreamPath(filename).getAbsolutePath());
                            setImageDrawable(drawable);
//                            setScaleType(ScaleType.CENTER_CROP);
                            setScaleType(ScaleType.FIT_CENTER );
                            setVisibility(RemoteImageView.VISIBLE);
                            //proc_startAnimation();
                        } else {
                            setImageNotFound();
                        }
                    } else {
                        if (count <= 20) {
                            setImageNowLoading();
                            msg = obtainMessage(IMG_DOWNLOADING, ++count, 0, item);
                            long current = SystemClock.uptimeMillis();
//                            long nextTime = current + 1000;
                            long nextTime = current + 500;
                            sendMessageAtTime(msg, nextTime);
                        } else {
                            //失敗扱い
                            setImageNotFound();
                        }
                    }
                    c.close();
                } else {
                    setImageNotFound();
                }
            }
        }
    };

    public RemoteImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public RemoteImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RemoteImageView(Context context) {
        super(context);
    }

    abstract public void setImageNotFound();
    
    abstract public void setImageNowLoading();
    
    /*
	@Override
	public void onAnimationEnd(Animation animation) {
		try
		{
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	@Override
	public void onAnimationRepeat(Animation animation) {
	}
	@Override
	public void onAnimationStart(Animation animation) {
	}
	
	private void proc_startAnimation(){
		try
		{
			AnimationSet set = new AnimationSet(true);
			AlphaAnimation alpha = new AlphaAnimation(0.1f, 1.0f);

			set.addAnimation(alpha);
//			set.setDuration( 1200 );
			set.setDuration( 200 );
			set.setAnimationListener(this);
			set.setFillAfter(true);
			startAnimation(set);			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
    */

}
