///*
// * Copyright (C) 2013-2015 北京爱百车信息技术服务有限公司
// * 
// * 		http://www.aibaiche.com
// *
// * Unless required by applicable law or agreed to in writing, you 
// * may not use or modify this file. 
// */
//package com.example.hospital;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import org.json.JSONObject;
//
//import com.example.hospital.model.HospitalBarActivity;
//
//import android.annotation.SuppressLint;
//import android.app.AlertDialog;
//import android.content.Intent;
//import android.os.Bundle;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.view.Gravity;
//import android.view.KeyEvent;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.inputmethod.EditorInfo;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.TextView;
//import android.widget.TextView.OnEditorActionListener;
//import android.widget.Toast;
//
//
///**
// * 该类表示爱百车车行端的登录界页，未登录账户不能使用车行客户端
// * @author tennkei
// *
// */
//public class LogIn extends HospitalBarActivity implements OnClickListener, OnEditorActionListener, NetHelperListener{
//	
//	private final String TAG = LogIn.class.getSimpleName();
//	private EditText etLoginAccount,etPwd;//分别表示输入账户名和密码的输入框
//	private Button bLogin;//表示登陆按钮
//	private AlertDialog dlgLoading = null;//登录过程中显示的对话框
//	private GetNetDataHelper nethelper;//表示网络操作类
//	private Map<String,String> pairs = new HashMap<String, String>();//表示传递给服务器的键值对
//	private String remindMsg;//表示登录过程中的各种提示信息
//	private Toast toast; //显示登录是否成功的提示信息
//		
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_login);
//		initKJ();//初始化界面控件
//		//初始化网络操作类
//		nethelper = new GetNetDataHelper(AibaicheConstants.API_MAINSITE, pairs, this, this);
//		
//	}
//	
//	@Override
//	protected void onDestroy(){
//		super.onDestroy();
//	}
//	
//	/**
//	 * 该函数为通用函数，用来初始化界面中的各个控件
//	 */
//	@SuppressLint("ShowToast") 
//	private void initKJ(){
//		etLoginAccount = (EditText)findViewById(R.id.etAccount);
//		etPwd = (EditText)findViewById(R.id.etPwd);
//		bLogin = (Button)findViewById(R.id.bLogin);
//		//为登录输入框，登录按钮等控件绑定监听器
//		bLogin.setOnClickListener(this);
//		etLoginAccount.addTextChangedListener(new TextWatch(etLoginAccount));
//		etPwd.addTextChangedListener(new TextWatch(etPwd));
//		etPwd.setOnEditorActionListener(this);
//		//初始化Toast的Text为空串
//		toast = Toast.makeText(this, "", Toast.LENGTH_LONG);
//		toast.setGravity(Gravity.CENTER, 0, 0); //toast居中显示在屏幕中间
//	}
//	
//	/**
//	 * 实现OnClickListener点击监听器(接口)的onClick点击事件<br>
//	 * 监听登录按钮的点击动作
//	 * @param v 点击的View,即调用setOnClickListener的那个控件(控件也属于View)
//	 */
//	@Override
//	public void onClick(View v) {
//		//如果登录按钮被按下
//		if(v.equals(bLogin)){
//			
//			remindMsg = "登录中";
//			//为pairs添加登录信息传递给服务器
//			pairs.clear();
//			pairs.put("a", "login");
//			pairs.put(AibaicheConstants.HTTP_REQUETID, "login");
//			pairs.put("appid", pfs.getPfs(pfs.APPID));
//			pairs.put("deviceId", CommonFunc.getDeviceId(this));
//			pairs.put("u", etLoginAccount.getText().toString());
//			pairs.put("p", etPwd.getText().toString());
//            //网络操作类向服务器发送网络请求，既不从缓存获取数据，也不缓存新数据
//			nethelper.sendNetData_trineaNoStore();
//		}
//	}
//	
//	/**
//	 * 实现OnEditorActionListener监听器(接口)的 事件<br>
//	 * 监听键盘输入是否完成，当按下软键盘的Enter键时触发事件
//	 */
//	@Override
//	public boolean onEditorAction(TextView v, int actionId, KeyEvent arg2) {
//		//软键盘按下Enter时，设置登录按钮执行了点击操作
//		if(actionId == EditorInfo.IME_ACTION_DONE){
//			if(v.equals(etPwd)) bLogin.performClick();
//		}
//		return false;
//	}
//	
//	
//	/**
//	 * 该函数为实现的NetHelperListener接口中必须实现的函数<br>
//	 * 在网络请求发起之前需要进行的一系列操作
//	 */
//	@Override
//	public void doBefore() {
//		
//		if(dlgLoading==null) dlgLoading = CommonFunc.getLoadingDLG(LogIn.this,remindMsg);
//		else dlgLoading.show();
//	}
//	
//	/**
//	 * 该函数为实现的NetHelperListener接口中必须实现的函数<br>
//	 * 在网络请求发起之后需要进行的一系列操作
//	 */
//	@Override
//	public void doAfter(JSONObject data,boolean isFromCache) {
//		
//		if(dlgLoading!=null) dlgLoading.dismiss();
//		if(data==null){
//			toast.setText("网络数据异常"); toast.show(); return;
//		}
//		try {
//			if("0".equals( data.optString("err") ) && "login".equals(data.optString(AibaicheConstants.HTTP_REQUETID))){ 
//				//登录成功
//				pfs.setPfs(pfs.USER, data.optString("u"));
//				pfs.setPfs(pfs.YGID, data.optString("ygid"));
//				pfs.setPfs(pfs.CHID, data.optString("chid"));
//				pfs.setPfs(pfs.CHUserID, data.optString("chuid"));
//				pfs.setPfs(pfs.USER_NAME, data.optString("name"));
//				pfs.setPfs(pfs.USER_TYPE, data.optString("type"));
//				pfs.setPfs(pfs.CHCityName, data.optString("cityname"));
//				pfs.setPfs(pfs.CHNAME, data.optString("chname"));
//				pfs.setPfs(pfs.RC_TOKEN, data.optString("rc_token"));
//				pfs.setPfs(pfs.APPID, data.optString("appid"));
//				pfs.setPfs(pfs.UserAvatar, data.optString("avatar"));
//				toast.setText("登录成功"); toast.show();
//				//友盟统计
//				HashMap<String, String> m = new HashMap<String, String>();
//				m.put("user", data.optString("u"));
//				m.put("ygid", data.optString("ygid"));
//				m.put("chid", data.optString("chid"));
//				m.put("chUserId", data.optString("chuid"));
//				m.put("name", data.optString("name"));
//				m.put("chname", data.optString("chname"));
//				m.put("time", DateUtil.getCurTime());
//				UMeng.addEvent_js(this, UMeng.CustomEventId_login, m);
//				
//				RongCloud.getInstance().connect();
//				//登录成功，跳转到MainActivity界面
//				Intent intent = new Intent(this,MainActivity.class);
//				startActivity(intent);
//				//关闭该Activity
//				finish();
//			} else { // 失败
//				toast.setText(data.optString("msg")); toast.show();
//			}
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			TTLog.e(TAG, "after net:" + e.getMessage());
//		} 
//	}
//	
//	/**
//	 * 文本监听类，实现了TextWatcher接口<br>
//	 * 监听登陆界面账户名输入框和密码输入框的状态
//	 */
//	class TextWatch implements TextWatcher{
//		private EditText et;
//		public TextWatch(EditText e){
//			this.et = e;
//		}
//		/**
//		 * 监听文本变化，改变按钮状态<br>
//		 * 当账号和密码均不为空的时候，设置登录按钮的状态为可用
//		 */
//		private void setLoginBtn(){
//			bLogin.setEnabled(!etLoginAccount.getText().toString().equals("") &&
//					!etPwd.getText().toString().equals(""));
//		}
//		
//		/**
//		 * 在账号输入框和密码输入框的内容改变时触发该事件
//		 */
//		@Override
//		public void afterTextChanged(Editable s) {
//			
//			if(et.equals(etLoginAccount) || et.equals(etPwd)) setLoginBtn();
//		}
//
//		@Override
//		public void beforeTextChanged(CharSequence s, int start, int count,
//				int after) {
//			
//			
//		}
//
//		@Override
//		public void onTextChanged(CharSequence s, int start, int before,
//				int count) {
//
//		}
//		
//	}
//	
//}
//
