package com.firstapp.android.picturealbum;

import java.util.ArrayList;
import java.util.UUID;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

public class PictureEditActivity extends FragmentActivity {
	
	private ViewPager mViewPager;
	private ArrayList<Picture> mPictures;
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		mViewPager = new ViewPager(this);
		mViewPager.setId(R.id.ViewPager1);
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
				return PictureEditFragment.newInstance(picture.getId());
			}
		});


		UUID pictureId = (UUID)getIntent()
				.getSerializableExtra(PictureEditFragment.EXTRA_DESCRIPTION_ID);
		for(int i = 0; i<mPictures.size(); i++)
		{
			if(mPictures.get(i).getId().equals(pictureId))
			{
				mViewPager.setCurrentItem(i);
				break;
			}
		}

	}
}//end class
