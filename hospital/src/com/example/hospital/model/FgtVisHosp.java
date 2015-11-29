package com.example.hospital.model;

import java.util.concurrent.atomic.AtomicInteger;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.hospital.R;
import com.example.hospital.Setting;


public class FgtVisHosp extends MainFragment implements OnClickListener{
	
	private boolean attachToRoot;
	private View thisView;//表示该fragment对应的view
	private HospitalAd hospitalAd;
	private TextView tvBidding,tvRegistration,tvChooseHospital,tvFindHospital;
	
	
	private Thread thread;
	
	
	
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		thisView = inflater.inflate(R.layout.fragment_jiuzhen, container, attachToRoot);
		initKJ();
		return thisView;
	}
	
	public FgtVisHosp(){
		this.attachToRoot = false;
	}
	
	private void initKJ(){
		hospitalAd = (HospitalAd) thisView.findViewById(R.id.model_HospitalAd);
		tvBidding = (TextView) thisView.findViewById(R.id.tvBidding);
		tvRegistration = (TextView) thisView.findViewById(R.id.tvRegistration);
		tvChooseHospital = (TextView) thisView.findViewById(R.id.tvChooseHospital);
	    tvFindHospital = (TextView) thisView.findViewById(R.id.tvFindHospital);
        System.out.println("YESSSSSSSSSSSS");
        System.out.println(hospitalAd==null);
	    new Thread(hospitalAd).start();
        System.out.println("NPPPPPPPPPPPP");
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
//		if(v.equals(tvChangeUseInfo)){
//			
//		}else if(v.equals(tvHospitalBidding)){
//			
//		}else if(v.equals(tvHospitalRegistration)){
//			
//		}else if(v.equals(tvHospitalSetting)){
//			Intent intent = new Intent(getActivity(),Setting.class);
//			startActivity(intent);
//		}
	}
	
	


	
	
	
}
