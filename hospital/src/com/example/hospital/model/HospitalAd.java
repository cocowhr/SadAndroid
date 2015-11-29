package com.example.hospital.model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.hospital.R;

public class HospitalAd extends LinearLayout implements Runnable{

	private Context context;
	private View thisView;
	private RelativeLayout hospitalAd;
	private ViewGroup hospitalAdPoints;

	private ViewPager adViewPager;
	private List<View> hospitalAds ;
	private ImageView[] imageViews;
	private ImageView imageView;
	private AdPageAdapter adapter;

	private AtomicInteger atomicInteger = new AtomicInteger(0);
	private int ids[]={R.drawable.view_add_1,R.drawable.view_add_2,
			R.drawable.view_add_3,R.drawable.view_add_4,
			R.drawable.view_add_5,R.drawable.view_add_6};
	
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			adViewPager.setCurrentItem(msg.what);
			super.handleMessage(msg);
		}

	};
	

	public HospitalAd(Context context) {
		super(context,null);
		this.context = context;
		initKJ();
	}

	public HospitalAd(Context context, AttributeSet attrs) {
		super(context,attrs);
		this.context = context;
		initKJ();
	}

	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
	}

	private void initKJ() {
		System.out.println("111111111111111111");
		thisView = LayoutInflater.from(context).inflate(R.layout.hospital_ad_layout,this,true);
		System.out.println("thisView "+thisView==null);
		hospitalAd = (RelativeLayout) thisView.findViewById(R.id.rlHospitalAd);
		hospitalAdPoints = (ViewGroup) thisView.findViewById(R.id.rlHospitalAdPoint);
		System.out.println("22222222222222222222");
		hospitalAds = new ArrayList<View>();
		addViewPager();
	}

	private void addViewPager(){
		adViewPager = new ViewPager(context);
		LinearLayout.LayoutParams layoutParams = new LayoutParams(
				LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		adViewPager.setLayoutParams(layoutParams);
		addImg();
	}

	private void addImg() {
		for(int id: ids){
			ImageView img = new ImageView(context);
			img.setBackgroundResource(id);
			hospitalAds.add(img);
		}
		adapter = new AdPageAdapter(hospitalAds);
		addPoints();
		adViewPager.setAdapter(adapter);
		hospitalAd.addView(adViewPager);
		adViewPager.setOnPageChangeListener(new AdPageChangeListener());
	}
	
	public ViewPager getAdViewPager(){
		return adViewPager;
	}
	
	public void addPoints(){
		int size = hospitalAds.size();
		imageViews = new ImageView[size];
		// 广告栏的小圆点图标
		for (int i = 0; i < size; i++) {
			imageView = new ImageView(context);
			LayoutParams lp = new LayoutParams(20, 20);
			lp.setMargins(20, 0, 0, 0);
			imageView.setLayoutParams(lp);
			
			imageViews[i] = imageView;
			if (i == 0) {
				imageViews[i].setBackgroundResource(R.drawable.point_focused);
			} else {
				imageViews[i].setBackgroundResource(R.drawable.point_unfocused);
			}
			// 将小圆点放入到布局中
			hospitalAdPoints.addView(imageViews[i]);
		}
	}
	
	public void atomicOption() {
		atomicInteger.incrementAndGet();
		if (atomicInteger.get() > imageViews.length - 1) {
			atomicInteger.getAndAdd(-5);
		}
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {

		}
	}
	
	@Override
	public void run() {
		System.out.println("runnn");
		while (true) {
			System.out.println("1111111");
			handler.sendEmptyMessage(atomicInteger.get());
			atomicOption();
		}
	}


	private final class AdPageChangeListener implements OnPageChangeListener {

		/**
		 * 页面滚动状态发生改变的时候触发
		 */
		@Override
		public void onPageScrollStateChanged(int arg0) {
		
		}

		/**
		 * 页面滚动的时候触发
		 */
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		/**
		 * 页面选中的时候触发
		 */
		@Override
		public void onPageSelected(int arg0) {
			// 获取当前显示的页面是哪个页面
			atomicInteger.getAndSet(arg0);
			// 重新设置原点布局集合
			for (int i = 0; i < imageViews.length; i++) {
				imageViews[arg0].setBackgroundResource(R.drawable.point_focused);
				if (arg0 != i) {
					imageViews[i].setBackgroundResource(R.drawable.point_unfocused);
				}
			}
		}
	}

	private final class AdPageAdapter extends PagerAdapter {
		private List<View> views = null;

		/**
		 * 初始化数据源, 即View数组
		 */
		public AdPageAdapter(List<View> views) {
			this.views = views;
		}

		/**
		 * 从ViewPager中删除集合中对应索引的View对象
		 */
		@Override
		public void destroyItem(View container, int position, Object object) {
			((ViewPager) container).removeView(views.get(position));
		}

		/**
		 * 获取ViewPager的个数
		 */
		@Override
		public int getCount() {
			return views.size();
		}

		/**
		 * 从View集合中获取对应索引的元素, 并添加到ViewPager中
		 */
		@Override
		public Object instantiateItem(View container, int position) {
			((ViewPager) container).addView(views.get(position), 0);
			return views.get(position);
		}

		/**
		 * 是否将显示的ViewPager页面与instantiateItem返回的对象进行关联 这个方法是必须实现的
		 */
		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}
	}

}
