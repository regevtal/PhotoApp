package com.firstapp.android.picturealbum;

import java.util.ArrayList;
import java.util.UUID;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

public class PicturePagerActivity extends FragmentActivity {

	private ViewPager mViewPager;
	private ArrayList<Picture> mPictures;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		mViewPager = new ViewPager(this);
		mViewPager.setId(R.id.ViewPager);
		setContentView(mViewPager);
		
		mPictures = PictureLab.get(this).getPictures();
		
		FragmentManager fm = getSupportFragmentManager();
		mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
			
			@Override
			public int getCount() {
			
				return mPictures.size();
			}
			
			@Override
			public Fragment getItem(int pos) {
				Picture picture = mPictures.get(pos);
				return PictureFragment.newInstance(picture.getId());
			}
		});
		
		UUID pictureId = (UUID)getIntent()
				.getSerializableExtra(PictureFragment.EXTRA_PICTURE_ID);
		for(int i = 0; i<mPictures.size(); i++)
		{
			if(mPictures.get(i).getId().equals(pictureId))
			{
				mViewPager.setCurrentItem(i);
				break;
			}
		}
		
		mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				//Empty
				
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// Empty
				
			}
			
			@Override
			public void onPageScrollStateChanged(int pos) {
				Picture picture = mPictures.get(pos);
				if(picture.getTitle() != null)
				{
					setTitle(picture.getTitle());
				}
				
			}
		});
	}//end onCreate

	
	
	
	
	
	
	
	
	
	
	
}//end class
