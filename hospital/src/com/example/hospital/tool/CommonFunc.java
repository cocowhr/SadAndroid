/*
 * Copyright (C) 2013-2015 北京爱百车信息技术服务有限公司
 * 
 * 		http://www.aibaiche.com
 *
 * Unless required by applicable law or agreed to in writing, you 
 * may not use or modify this file. 
 */
package com.example.hospital.tool;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Collection;
import java.util.Iterator;

import org.json.JSONArray;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Bitmap.CompressFormat;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hospital.R;
import com.example.hospital.model.MainFragment;


/**
 * 静态函数库
 * @author tennkei
 *
 */
public class CommonFunc {
	private static final String CLASS_TAG="CommonFunc";
	
	/**
	 * 检查是否有网络连接
	 * @param showmsg: 断网时是否提示信息
	 * @return 网络连接是否成功
	 */
	public static boolean checkNetCommon(Context context,boolean showmsg){
        if(context!=null){
        	ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);  //启动网络连接服务
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();//获取当前网络是否可用
            if (mNetworkInfo!=null && mNetworkInfo.isConnected())  //网络已连接
                return true;
            else{
            	if(showmsg) Toast.makeText(context, "网络未连接", Toast.LENGTH_LONG).show();  //网络未连接时如果需要输出提示信息即通过Toast信息提示框进行提示
            }
        }
        return false;
    }
	
	/**
	 * 获取当前窗口的宽度（像素）
	 * @param ac 需要获取宽度的Activity
	 * @return 屏幕宽度的像素值
	 */
	
	public static int getWindowWidth(Activity ac){
		DisplayMetrics dm = new DisplayMetrics();  //DisplayMetircs 类可以很方便的获取分辨率
        ac.getWindowManager().getDefaultDisplay().getMetrics(dm); //将当前窗口的一些信息放在DisplayMetrics类中
        return dm.widthPixels; //返回当前窗口的宽度（像素）
	}
	
	/**
	 * dp(dp即dip:设备独立像素值)向px(像素值:绝对像素)进行转换
	 * @param dipValue 设备独立像素值
	 * @param context 上下文
	 * @return dp值转换成的像素值
	 */
	public static float dp2px(float dipValue,Context context){
		final float scale = context.getResources().getDisplayMetrics().density;   
        return (int)(dipValue * scale + 0.5f); 
	}
	
	
	/**
	 * <p>显示网络加载时的loading提示</p>
	 * @param context 上下文
	 * @param txt 对话框提示信息
	 * @return AlertDialog对话框
	 */
	public static AlertDialog getLoadingDLG(Context context,String txt){

		AlertDialog.Builder builder = new Builder(context); //不能直接通过AlertDialog的构造函数得到AlertDialog对象，通过builder进行create
		AlertDialog dlg = builder.create();  //通过AlerDialog.Builder创建AlerDialog对话框
		//dlg.setCancelable(false); //设置这个对话框不能够被用户按[返回键]而取消掉
		dlg.show();  //显示AlertDialog对话框
		dlg.getWindow().setContentView(R.layout.dlg_loading);
		TextView tv = (TextView)dlg.getWindow().findViewById(R.id.tvLoadingMsg);
		tv.setText(txt);  //在textview中显示对话框提示信息
		return dlg;
	}
	
	/**
	 * <p>弹出确认框，类似js alert</p>
	 * @param context 上下文
	 * @param msg 
	 * @return void
	 */
	public static void showAlertDlg(Context context,String msg){
		AlertDialog.Builder builder = new Builder(context);
		builder.setMessage(msg).setCancelable(false).setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.cancel();
			}
		});
		//setPositiveButton给对话框添加“YES”按钮
		builder.show(); //对话框显示msg的消息内容
	}
	
	/**
	 * <p>判断手机号码格式是否正确</p>
	 * @param s 手机号码（字符串）
	 * @return 手机号码格式是否正确
	 */
	public static boolean isPhone(String s){	
		return s.length()==11 && s.charAt(0)=='1';
	}
	
	
	/**
	 * <p>获得 AndroidManifest中meta标签的数据</p>
	 * @param context 上下文
	 * @param metaKey 对应meta data中的androd:name
	 * @return meta data中metaKey对应的androd:value的值
	 */
    public static String getMetaValue(Context context, String metaKey) {
        
    	Bundle metaData = null;
        String apiKey = null;
        if (context == null || metaKey == null) {
            return null;
        }
        try {
            ApplicationInfo ai = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);
            //<meta-data>元素可以作为子元素，
        	//被包含在<activity>、<application> 、<service>和<receiver>元素中,定义组件相关配置值
            if (null != ai) { //如果获取到application（应用程序）的信息，存储metaData信息
                metaData = ai.metaData;
            }
            if (null != metaData) {  //如果metaData内容不空，将其转换为String
                apiKey = metaData.getString(metaKey);
            }
        } catch (NameNotFoundException e) { //捕捉并处理可能存在的异常
             
        	//TTLog.e(CommonFunc.CLASS_TAG,"This is an error!",e);
        }
        return apiKey; //返回meta-data key-value对中的value值
    }
    
    /**
     * 移除Json数据中的某个index处的Json对象
     * equal to <b>jsonarray.remove(index)</b>, which is added in Api 19
     * @param arr 
     * @param index
     */
    public static JSONArray removeJsonAtIndex(JSONArray arr,int index){
    	JSONArray narr = new JSONArray();
    	try{
    		for(int i=0;i<arr.length();i++)
    			if(i != index) narr.put(arr.getJSONObject(i));
    	}catch (Exception e){}
    	arr = null;
    	return narr;
    }
    /**
     * Appends JsonArray b to the end of JsonArray a.
     * @param a
     * @param b
     * @return
     */
    public static JSONArray connectJson(JSONArray a ,JSONArray b){
    	try {
			for(int i=0;i<b.length();i++)a.put(b.getJSONObject(i));
		} catch (Exception e) {
			
			e.printStackTrace();
		}
    	return a;
    }
    /**
     * for php
     * @param str
     * @return
     */
    public static String htmlSpecialChars_decode(String str) {
        str = str.replaceAll("&amp;","&");
        str = str.replaceAll("&lt;","<");
        str = str.replaceAll("&gt;", ">");
        str = str.replaceAll("&quot;","\"");
        //str = str.replaceAll("'", "&#x27;");
        return str;
    } 
    
    /**
     * Same like php implode
     * Accept a raw type for compatibility
     * @param c	a collection
     * @param character the string which should be used to connect the collection
     * @return like "9;89;a"
     */
    public static String implode(Collection c,String character){
    	StringBuffer b = new StringBuffer();
    	if (c != null) {
            for (Iterator it = c.iterator(); it.hasNext();) {
               b.append(it.next().toString());
               if(it.hasNext())b.append(character);
            }
        }
    	return b.toString();
    }
    
    /**
     * 该静态函数用来缓存多种View对象，形成(Id,View)键值对,减少findViewById的多次调用,提高效率
     * @param v 表示存储控件的xml对应的View对象
     * @param id 不定长度参数，表示View对象中的相关控件的R.id
     * @return {@link SparseArray} 函数返回R.id与控件对应的View对象的键值对数组
     * 
     */
    public static SparseArray<View> getViewsByID (View v,int... ids){
    	SparseArray<View> spv = new SparseArray<View>();
    	for(int id: ids){
    		spv.put(id, v.findViewById(id));
    	}
    	return spv;
    }
    
    /**
     * 该函数用于隐藏界面中全部的fragment,使代码更加清晰
     * @param transaction 表示Fragment的transaction
     * @param fragmts 表示不定长的参数,表示不同的MainFragment
     */
    public static void hideFragment(FragmentTransaction transaction,MainFragment... frgmts){
    	for(MainFragment fgt:frgmts){
    		if(fgt!=null)
    		   transaction.hide(fgt);
    	}
    }
    
    /**
     * 获取设备唯一识别码
     * @return IMEI号+MAC地址
     */
    public static String getDeviceId(Context cnt){
    	StringBuffer b = new StringBuffer("A_"); // for android
    	TelephonyManager tm = (TelephonyManager)cnt.getSystemService(android.content.Context.TELEPHONY_SERVICE);
    	String deviceId = tm.getDeviceId();
    	//b.append(deviceId);
    	// mac
    	WifiManager wm = (WifiManager) cnt.getSystemService(Context.WIFI_SERVICE);  
        WifiInfo info = wm.getConnectionInfo(); 
        String mac = info.getMacAddress();
        
        if( TextUtils.isEmpty(deviceId) ){
	    	deviceId = mac;
	    }
	
        if( TextUtils.isEmpty(deviceId) ){
        	deviceId = android.provider.Settings.Secure.getString(cnt.getContentResolver(),android.provider.Settings.Secure.ANDROID_ID);
        }
        
        b.append(deviceId).append("_").append(mac);
        
    	return b.toString();
    }
    
    /**
	 * 通过id获取父视图中的子视图 主要用于 类似 ListView render view
	 * 
	 * @param parentView
	 * @param ids
	 *            子视图id
	 * @return SparseArray Object
	 */
	public static SparseArray<View> getViewsById(View parentView, int... ids) {
		SparseArray<View> s = new SparseArray<View>();
		for (int id : ids) {
			s.put(id, parentView.findViewById(id));
		}
		return s;
	}
    
    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		bmp.compress(CompressFormat.PNG, 100, output);
		if (needRecycle) {
			bmp.recycle();
		}
		
		byte[] result = output.toByteArray();
		try {
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
    
    /**
	 * 生成声音文件字符串
	 */
	public static String getVoiceFileStr(File audioFile) {
		String s = "";
		FileInputStream inputStream = null;
		try {
			inputStream = new FileInputStream(audioFile);

			ByteArrayOutputStream bStream = new ByteArrayOutputStream();
			int len;
			byte[] buffer = new byte[100];
			while ((len = inputStream.read(buffer)) != -1) {
				bStream.write(buffer, 0, len);
			}
			s = Base64.encodeToString(bStream.toByteArray(), Base64.DEFAULT);
			inputStream.close();
		} catch (Exception e) {
			// TTLog.e(TAG, "reform audio file:" + e.getMessage());
			e.printStackTrace();
		}
		return s;
	}
	/**
	 * 压缩图片, 成为Base64 String
	 */
	public static String compressPic(Bitmap oriBitmap) {
		int wid = oriBitmap.getWidth(), hgt = oriBitmap.getHeight();
		// System.out.println("w="+wid+",h="+hgt);
		Bitmap rb = null;
		if (wid > 800 || hgt > 800) {
			float max = 800;
			float scale;
			if (wid > hgt) {
				scale = max / wid;
			} else {
				scale = max / hgt;
			}
			Matrix matrix = new Matrix();
			matrix.postScale(scale, scale); // 参数为比率

			rb = Bitmap.createBitmap(oriBitmap, 0, 0, wid, hgt, matrix, true);
		} else
			rb = oriBitmap;

		ByteArrayOutputStream bStream = new ByteArrayOutputStream();

		int prs = 100;
		rb.compress(CompressFormat.JPEG, 100, bStream);

		while (bStream.toByteArray().length > 1024 * 100 && prs > 0) { // 大于100k
			prs -= 10;
			bStream.reset();
			rb.compress(CompressFormat.JPEG, prs, bStream);
		}
		// System.out.println(bStream.toByteArray().length);
		String s = Base64.encodeToString(bStream.toByteArray(), Base64.DEFAULT);

		return s;
	}
}
