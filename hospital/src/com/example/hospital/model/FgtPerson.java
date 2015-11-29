package com.example.hospital.model;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.hospital.LoginActivity;
import com.example.hospital.R;
import com.example.hospital.Setting;

public class FgtPerson extends MainFragment implements OnClickListener {

	private boolean attachToRoot;
	private View thisView;// 表示该fragment对应的view
	private TextView tvChangeUserInfo, tvHospitalBidding, tvHospitalRegistration, tvHospitalSetting;
	// 分别表示个人中心上面修改资料，预约，挂号和设置的TextView

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
			@Nullable Bundle savedInstanceState) {
		thisView = inflater.inflate(R.layout.fragment_person, container, attachToRoot);
		initKJ();
		return thisView;
	}

	public FgtPerson() {
		this.attachToRoot = false;
	}

	private void initKJ() {
		tvChangeUserInfo = (TextView) thisView.findViewById(R.id.tvChangeUserInfo);
		tvHospitalBidding = (TextView) thisView.findViewById(R.id.tvHospitalBidding);
		tvHospitalRegistration = (TextView) thisView.findViewById(R.id.tvHospitalRegistration);
		tvHospitalSetting = (TextView) thisView.findViewById(R.id.tvHospitalSetting);

		tvChangeUserInfo.setOnClickListener(this);
		tvHospitalBidding.setOnClickListener(this);
		tvHospitalRegistration.setOnClickListener(this);
		tvHospitalSetting.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.equals(tvChangeUserInfo)) {
			Intent intent = new Intent(getActivity(), LoginActivity.class);
			startActivity(intent);
		} else if (v.equals(tvHospitalBidding)) {

		} else if (v.equals(tvHospitalRegistration)) {

		} else if (v.equals(tvHospitalSetting)) {
			Intent intent = new Intent(getActivity(), Setting.class);
			startActivity(intent);
		}
	}

}
