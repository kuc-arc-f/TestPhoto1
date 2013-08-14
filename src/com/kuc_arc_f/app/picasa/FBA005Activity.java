package com.kuc_arc_f.app.picasa;

import com.kuc_arc_f.fw.AppConst;
import com.kuc_arc_f.fw.ComUtil;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.TextView;

public class FBA005Activity extends Activity {
	
	private com.kuc_arc_f.fw.ComUtil  m_Util    = new ComUtil();
	
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.fba005);

        String versionName = m_Util.comGet_VersionName02(this);
        TextView t = (TextView) findViewById(R.id.about);
        String s_app = getResources().getString(R.string.app_name);
        t.setText( s_app + "\nversion " + versionName + "\n\n" + getText(R.string.about_text));
    }
    
    
}