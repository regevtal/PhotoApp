package com.firstapp.android.picturealbum;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

public abstract class SingleFragmentActivity extends FragmentActivity { 
	
	protected abstract Fragment createFragment();
	
	@Override
	public void onCreate (Bundle savedInstanceSatae)
	{
		super.onCreate(savedInstanceSatae);
		setContentView(R.layout.activity_fragment);
		FragmentManager fm = getSupportFragmentManager();
		Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);
		
		
		if(fragment ==null)
		{
			fragment = createFragment();
			fm.beginTransaction()
			.add(R.id.fragmentContainer, fragment)
			.commit();
		}
	}

}
