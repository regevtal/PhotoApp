package com.firstapp.android.picturealbum;

import android.support.v4.app.Fragment;



public class PictureListActivity extends SingleFragmentActivity {

	@Override
	protected Fragment createFragment() {

		return new PictureListFragment();
	}

}
