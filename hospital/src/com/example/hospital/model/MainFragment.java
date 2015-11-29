package com.example.hospital.model;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MainFragment extends Fragment {

	/**
	 * 构造函数
	 *
	 * @param resource
	 *      ID for an XML layout resource to load
	 */

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return super.onCreateView(inflater, container, savedInstanceState);
		// return inflater.inflate(resource, container, attachToRoot);
	}

}
