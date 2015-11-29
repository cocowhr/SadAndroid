package com.example.hospital;

import com.example.hospital.R;
import com.example.hospital.model.FgtPerson;
import com.example.hospital.model.FgtVisHosp;
import com.example.hospital.model.HospitalBarActivity;
import com.example.hospital.tool.CommonFunc;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class MainActivity extends HospitalBarActivity implements OnCheckedChangeListener {
	//private final String TAG = MainActivity.class.getSimpleName();
	public static MainActivity mainInstance = null;//表示主界面
	private long exitTime = 0;
	private FragmentManager fm;  //用fragment表示界面页(精简代码，便于代码管理及后期修改)
	private FragmentTransaction transaction;//fragment的事务管理，对fragment进行添加，移除，替换等操作。
	private RadioGroup rgBottomBar;   //屏幕下方的菜单栏，有就诊，个人中心两项
	private FgtVisHosp fgtVisHosp; //就诊的fragment
    private FgtPerson fgtPerson; //个人中心的fragment

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //隐藏标题栏左上角的返回图标ImageButton
        getLeftView().setVisibility(View.GONE);
        mainInstance = this;
        initKJ();
    }
    
    @Override
	protected void onResume() {
    	
		super.onResume();
	}
    
    @Override
	protected void onDestroy() {
		
    	mainInstance = null;
		super.onDestroy();
	}

    /**
     * 初始化主页面中的控件
     */
	private void initKJ(){
		rgBottomBar = (RadioGroup)findViewById(R.id.rgBottomBar);
    	rgBottomBar.setOnCheckedChangeListener(this);
    	fm = getSupportFragmentManager();
    	transaction = fm.beginTransaction();
    	//最先初始化构造待报价单fragment,使其显示在屏幕上
    	fgtVisHosp = new FgtVisHosp();
        setCenterTitle(getResources().getString(R.string.FgtVisHosp));
        setRightTxt("北京");
    	transaction.add(R.id.id_content, fgtVisHosp);
    	transaction.commit();
    }
    
    /**
     *  按两次返回键(Back键)退出,按返回键时系统自动调用该函数<br>
     *  当第二次点击与第一次点击的时间差小于2s时自动退出，大于2s不会退出
     */
    @Override
	public void onBackPressed() {

		if ((System.currentTimeMillis() - exitTime) > 2000) {  
            Toast.makeText(this, getResources().getString(R.string.exit_alert), Toast.LENGTH_SHORT).show();  
            exitTime = System.currentTimeMillis();  
            return;  
        }
		// exit
		finish();
	}
	
    /**
     * 该方法为实现onCheckedListener接口必须实现的onCheckedChanged方法
     * 当页面切换时如何显示页面
     * @param group 表示屏幕下方的菜单栏 
     * @param checkedId 表示菜单栏中每个子页面的R.id
     * @return 
     */
    @Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		
		if(group.equals(rgBottomBar)){ // 底部菜单栏
			transaction = fm.beginTransaction();
			CommonFunc.hideFragment(transaction,fgtVisHosp,fgtPerson);
			switch(checkedId){
				case R.id.rb_tab1:
					transaction.show(fgtVisHosp);
					setCenterTitle(getResources().getString(R.string.FgtVisHosp));
					setRightTxt("北京");
					break;
				case R.id.rb_tab2:
					if(fgtPerson == null){ 
						fgtPerson = new FgtPerson(); 
					    transaction.add(R.id.id_content, fgtPerson);
					}
					else {
						transaction.show(fgtPerson);
					}
					setCenterTitle(getResources().getString(R.string.FgtPerson));
					setRightTxt("");
					break;
			}
			//提交fragment事务
			transaction.commit();
		}
	}

}

