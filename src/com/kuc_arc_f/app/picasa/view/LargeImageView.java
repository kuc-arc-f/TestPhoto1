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

import com.kuc_arc_f.app.picasa.DatArray;
import com.kuc_arc_f.app.picasa.ItemPT;
import com.kuc_arc_f.app.picasa.db.ShowCacheDB.CacheColumn;
import com.kuc_arc_f.app.picasa.db.ShowCacheDB;
import com.kuc_arc_f.fw.AppConst;

abstract public class LargeImageView extends ImageView{
    private static final String TAG = "LargeImageView";
    
    public static final int IMG_DOWNLOADING = 1;
    
    private static String m_TypLand="";
    static com.kuc_arc_f.fw.AppConst m_Const= new AppConst();
    
    public final Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == IMG_DOWNLOADING) {
                Context cxt = getContext();
                final ItemPT item = (ItemPT)msg.obj;
                final String s_id = item.getId().toString();
//Log.d(TAG, "s_id=" +s_id);
                int count = msg.arg1;
                ShowCacheDB db = ShowCacheDB.getInstance(cxt);
                if (s_id != null && !s_id.equals("")) {
                    final Cursor c = db.existsFile_byId(s_id);
                    if (c.moveToFirst()) {
                        final String filename = c.getString(c.getColumnIndex(CacheColumn.NAME));
//Log.d(TAG,  "handleMessage.filename="+ filename);
                        final String type = c.getString(c.getColumnIndex(CacheColumn.TYPE));
                        if (type.equals("image/jpg")
                                || type.equals("image/jpeg")
                                || type.equals("image/png")
                                || type.equals("image/gif")) {
//Log.d(TAG, "type.equals=OK");
                            Drawable drawable = Drawable.createFromPath(cxt.getFileStreamPath(filename).getAbsolutePath());
                            setImageDrawable(drawable);
                            setScaleType(ScaleType.FIT_CENTER);
                            setVisibility(LargeImageView.VISIBLE);
                            stop_progress();
                        } else {
                            setImageNotFound();
                            stop_progress();
                        }
                    } else {
//Log.d(TAG ,  "handleMessage.count="+ String.valueOf(count));
                        if (count <= 40) {
                            //setImageNowLoading();
                            msg = obtainMessage(IMG_DOWNLOADING, ++count, 0, item);
                            long current = SystemClock.uptimeMillis();
                            long nextTime = current + 500;
                            sendMessageAtTime(msg, nextTime);
                        } else {
                            //失敗扱い
                            //setImageNotFound();
                            stop_progress();
                        }
                    }
                    c.close();
                } else {
                    setImageNotFound();
                }
            }
        }
    };

    public LargeImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public LargeImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LargeImageView(Context context) {
        super(context);
//        m_TypLand= DatArray.getTYP_LAND();
    }

    abstract public void setImageNotFound();
    
    abstract public void setImageNowLoading();

    abstract public void start_progress();
    abstract public void stop_progress();
}
