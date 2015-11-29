package com.example.hospital;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class Welcome extends Activity implements Runnable{

	private Handler handler = new Handler();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		handler.postDelayed(this, 2000);
	}
	
	@Override
	protected void onDestroy(){
		super.onDestroy();
		handler.removeCallbacks(this);
		//System.out.println("Welcome finish");
		finish();
	}

	@Override
	public void run() {
		Intent intent = new Intent(Welcome.this,MainActivity.class);
		startActivity(intent);
		finish();
	}

}
