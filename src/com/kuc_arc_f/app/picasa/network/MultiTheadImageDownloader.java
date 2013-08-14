package com.kuc_arc_f.app.picasa.network;

import java.util.ArrayList;

import com.kuc_arc_f.app.picasa.ItemPT;

import android.content.Context;

public class MultiTheadImageDownloader {

    public static void execute(Context context, ArrayList<ItemPT> items) throws Exception {
        ArrayList<ItemPT> list1 = new ArrayList<ItemPT>();
        ArrayList<ItemPT> list2 = new ArrayList<ItemPT>();
        ArrayList<ItemPT> list3 = new ArrayList<ItemPT>();
        //ArrayList<ItemPT> list4 = new ArrayList<ItemPT>();
        int i = 0;
        for(int j=0; i<items.size(); j++){
        	ItemPT item = items.get(j);
            switch (i % 3) {
	            case 0: list1.add(item); break;
	            case 1: list2.add(item); break;
	            case 2: list3.add(item); break;
	            //case 3: list4.add(item); break;
	        }
            i++;
        }
        //new ImageDownloadCacheTask(context).execute(list1);
        //new ImageDownloadCacheTask(context).execute(list2);
        //new ImageDownloadCacheTask(context).execute(list3);
        try
        {
            new DownloadTask(context).execute(list1);
            new DownloadTask(context).execute(list2);
            new DownloadTask(context).execute(list3);
        }catch(Exception e){
//        	e.printStackTrace();
        	throw e;
        }
    }
}
