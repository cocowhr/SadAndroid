package com.example.hospital.model;

import com.example.hospital.R;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class HospitalBarActivity extends ActionBarActivity{
	
	private View centerTitle = null,barBtn_left = null,barBtn_right = null;
	protected final int BARID_leftView= 0,BARID_rightView=1;
    protected OnClickListener listener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
		 
			if(v.equals(barBtn_left)) onHospitalActionBarClick(v,BARID_leftView);
			else if(v.equals(barBtn_right)) onHospitalActionBarClick(v,BARID_rightView);
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSupportActionBar().setDisplayOptions(0);
		getSupportActionBar().setDisplayShowCustomEnabled(true);
		setBarView();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	/**
	 * set bar view layout and initial
	 * 
	 */
	protected void setBarView() {
		
		getSupportActionBar().setCustomView(R.layout.hospital_actionbar);
		initBarView();	 
	}
	
	protected void initBarView() {
		centerTitle = getSupportActionBar().getCustomView().findViewById(R.id.tvBarTitle);
		barBtn_left = getSupportActionBar().getCustomView().findViewById(R.id.vBarBtn_left);
		barBtn_right = getSupportActionBar().getCustomView().findViewById(R.id.vBarBtn_right);
		
		centerTitle.setOnClickListener(listener);
		barBtn_left.setOnClickListener(listener);
		barBtn_right.setOnClickListener(listener);
		setCenterTitle(getTitle().toString());
	}
	
	protected void setCenterTitle(String title){
		((TextView)centerTitle).setText(title);
	}
	protected View getCenterTitle(){
		return centerTitle;
	}

	
	protected boolean onHospitalActionBarClick(View v,int barid){
		if(barid == BARID_leftView){
			finish();
		}
		return true;
	}
	
	protected View getLeftView(){
		return barBtn_left;
	}
	protected void setRightTxt(String txt){
		((TextView)barBtn_right).setText(txt);
	}
	protected View getRightView(){
		return barBtn_right;
	}

}
