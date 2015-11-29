/*
 * Copyright (C) 2013-2015 北京爱百车信息技术服务有限公司
 * 
 * 		http://www.aibaiche.com
 *
 * Unless required by applicable law or agreed to in writing, you 
 * may not use or modify this file. 
 */
package com.example.hospital.tool;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.hospital.tool.MsgCrypt;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import cn.trinea.android.common.entity.HttpRequest;
import cn.trinea.android.common.entity.HttpResponse;
import cn.trinea.android.common.service.HttpCache;
import cn.trinea.android.common.service.HttpCache.HttpCacheListener;
import cn.trinea.android.common.util.CacheManager;
/**
 * 网络操作类
 * 对外提供3个获取网络数据方法
 * <p>sendNetData_trinea(), sendNetData_trineaNoCache(),sendNetData_trineaNoStore()</p>
 * <p>需要实现NetHelperListener接口，异步更新主线程</p>
 * @author tennkei
 *
 */
public class GetNetDataHelper{
	
	private final String TAG = "GetNetDataHelper";
	private String url; //服务器的地址
	private Map<String,String> pairs ; //本地与网络交互中
	private String postPairs;//网络请求时加密后发送给服务器的数据
	private AsyncTask<Void, Void, String> mytask ;
	private NetHelperListener thishelper; //网络操作监听类
	private Context excuteContext = null;
	private String errMsg = null;
	//private String cacheWay = "max-stale=2592000"; // 30天

	//private String method; // POST , GET; 目前通用post即可
	
	//HttpRequest类用来请求信息类，可设置超时时间、请求参数、UserAgent、请求属性等
    //HttpResponse 请求数据返回类，可获取接口内容、过期时间等。
	//HttpCacheListener 请求回调接口，onPreGet方法会在请求前执行，
	//onPostGet方法会在请求结束后执行，两个方法都运行在UI线程
	
	private HttpCache httpCache; //定义网络缓存
	//网络请求的监听器
	private HttpCacheListener trineaListener = new HttpCacheListener() {
		/**
		 * 在网络请求发起之前执行的操作
		 * 
		 */
		@Override
		protected void onPreGet() {
			
			super.onPreGet();
			if(thishelper!=null) thishelper.doBefore();
		}
        
		/**
		 * 在网络请求结束之后执行的操作
		 * @param httpResponse 请求数据返回类的对象
		 * @param isInCache 数据是否在缓存中
		 */
		@Override
		protected void onPostGet(HttpResponse httpResponse,boolean isInCache) {
			
			super.onPostGet(httpResponse, isInCache);				
			JSONObject o;
			try {
				String result = httpResponse.getResponseBody();
				//System.out.println("okok"+httpResponse.getUrl());
				//System.out.println("dgsdgsgs"+result);
				o = new JSONObject(result);
				String d = MsgCrypt.decodeMsg(o.getString("timestamp"), o.getString("nonce"), 
						o.getString("signature"), o.getString("msg"));
			    //System.out.println(d);
				JSONObject obj = new JSONObject(d);
				if(thishelper!=null)thishelper.doAfter(obj,isInCache);
			} catch (Exception e) {
				Log.e(TAG, e.getMessage());
				if(thishelper!=null) thishelper.doAfter(null,isInCache);
			}	
		}
	};
	
	/**
	 * 构造函数
	 */
    public GetNetDataHelper(){
		
	}
	
	/**
	 *构造函数
	 *@param url 服务器网址
	 *@param pairs 服务器与客户端交互的信息的键值对
	 *@param helper 网络操作类的监听器
	 *@param context 上下文
	 */
    public GetNetDataHelper(String url,Map<String,String> pairs,NetHelperListener helper,Context context){
		this.url = url;
		this.pairs = pairs;	
		this.thishelper = helper;
		this.excuteContext = context;
		this.httpCache = CacheManager.getHttpCache(context);
	}
    
    /**
     * 设置服务器网址
     * @param url 服务器网址
     */
	public void setUrl(String url){
		this.url = url;
	}
	
	/**
	 * 设置网络请求发送的数据的键值对
	 * @param pairs 数据的键值对
	 */
	public void setPairs(Map<String,String> pairs){
		this.pairs = pairs;
	}
	
	/**
	 * 设置上下文
	 * @param context 上下文
	 */
	public void setContext(Context context){
		this.excuteContext = context;
	}
	
	/**
	 * 后台执行从服务器获取数据
	 * @deprecated
	 */
	private void setMyTask(){
		
		//AsyncTask只能执行一次，因而使用Java匿名内部类保证只执行一次
		mytask = new AsyncTask<Void, Void, String>(){
			
			@Override
			protected String doInBackground(Void... arg0) {
				
				PrintWriter out = null;
				BufferedReader in = null;
				StringBuffer data = new StringBuffer();
				try {
					URL realUrl = new URL(url);
					// 打开和URL之间的连接
					URLConnection conn = realUrl.openConnection();
					//设置网络连接的时间上限为20s
					conn.setConnectTimeout(20000);
					//设置读取数据的时间为20s
					conn.setReadTimeout(20000);
					// 发送POST请求必须设置如下两行
					conn.setDoOutput(true);
					conn.setDoInput(true);
					// 获取URLConnection对象对应的输出流
					out = new PrintWriter(conn.getOutputStream());
					// 发送请求参数
					out.print(postPairs);
					// flush输出流的缓冲
					out.flush();
					// 定义BufferedReader输入流来读取URL的响应
					in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
					String line;
					while ((line = in.readLine()) != null) {
						data.append( line );
					}
					errMsg = "0";
				}catch(SocketTimeoutException e){
					e.printStackTrace();
					errMsg = "网络连接超时";
				}catch (Exception e) {
					e.printStackTrace();
					errMsg = "网络连接异常";
				}
				// 使用finally块来关闭输出流、输入流
				finally {
					try {
						if (out != null)out.close();
						if (in != null)	in.close();
					} catch (IOException ex) {
						ex.printStackTrace();
					}					
				}
				//返回从服务器获取到的数据
				return data.toString();
			}

			@Override
			protected void onPostExecute(String result) {

				// 解析result,如果出现网络连接超时或者网络异常,显示错误信息
				if(!errMsg.equals("0")){
					Toast.makeText(excuteContext, errMsg, Toast.LENGTH_LONG).show();
				}
				JSONObject o;
				try {
					o = new JSONObject(result);
					String d = MsgCrypt.decodeMsg(o.getString("timestamp"), o.getString("nonce"), 
							o.getString("signature"), o.getString("msg"));
					JSONObject obj = new JSONObject(d);
					if(thishelper!=null)thishelper.doAfter(obj,false);
				} catch (JSONException e) {
					
					e.printStackTrace();
					if(thishelper!=null)thishelper.doAfter(null,false);
				}				
			}

			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
				if(thishelper!=null)thishelper.doBefore();
			}
			
		};
	}
	
	
	/**
	 * 获取数据的键值对
	 * @return 数据的键值对
	 */
	public Map<String,String> getPairs(){
		return this.pairs;
	}
	
	
	/** 
	 * 网络接口，执行相关操作，实现该接口必须要实现doBefore和doAfter方法
	 *
	 */
	public interface NetHelperListener{
		/**
		 * this method is triggered before getting data from the Internet
		 */
		public void doBefore();
		
		/**
		 * this method is triggered after finishing getting data from the Internet
		 * @param data
		 * @param isFromCache 
		 */
		public void doAfter(JSONObject data, boolean isFromCache);
	}

	
	/**
	 * * 发起网络请求
	 * 加密后获得 真正 发送的url
	 * 需要网络连接
	 * @deprecated
	 */
	public void sendNetData(){
		//if(!CommonFunc.checkNetCommon(excuteContext, true)) return;
		JSONObject o = new JSONObject(this.pairs);
		this.postPairs = MsgCrypt.encodeMsg(o.toString());
		setMyTask();
		this.mytask.execute();
	}
	
	
	/**
	 *发起网络请求 post 
	 * 优先从缓存获取
	 */
	public void sendNetData_trinea(){
		
		JSONObject o = new JSONObject(this.pairs);
		this.postPairs = MsgCrypt.encodeMsgCache(o.toString());
		//httpCache.httpGet(url + "?" + postPairs, trineaListener);
		HttpRequest request = new HttpRequest(url + "?" + postPairs);
		request.setRawUrl(this.url);
		httpCache.httpGet(request, trineaListener);
	}
	
	/**
	 *发起网络请求 post
	 * 不从缓存获取，但缓存新的数据
	 */
	public void sendNetData_trineaNoCache(){
		JSONObject o = new JSONObject(this.pairs);
		this.postPairs = MsgCrypt.encodeMsgCache(o.toString());
		HttpRequest request = new HttpRequest(url + "?" + postPairs);
		request.setRawUrl(this.url);
		request.setRequestProperty("cache-control", "no-cache");
		httpCache.httpGet(request, trineaListener);
	}
	
	/**
	 *发起网络请求 post
	 * 不从缓存获取，也不缓存新的数据，直接从服务器获取数据
	 */
	public void sendNetData_trineaNoStore(){
		JSONObject o = new JSONObject(this.pairs);
		this.postPairs = MsgCrypt.encodeMsg(o.toString());
		HttpRequest request = new HttpRequest(url + "?" + postPairs);
		//System.out.println("postPairs == " +postPairs);
		request.setRawUrl(this.url);
		request.setRequestProperty("cache-control", "no-store");
		httpCache.httpGet(request, trineaListener);
	}
}
