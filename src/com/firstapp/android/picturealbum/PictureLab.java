package com.firstapp.android.picturealbum;

import java.util.ArrayList;
import java.util.UUID;

import android.content.Context;
import android.util.Log;

public class PictureLab {
	private static final String TAG = "PictureLab";
	private static final String FILENAME = "picture.json";

	private ArrayList<Picture> mPictures;
	private PictureAlbumJSONSerializer mSerializer;

	private static PictureLab sPictureLab;
	private Context mAppContext;

	private PictureLab(Context appContext) {
		mAppContext = appContext;
		mSerializer = new PictureAlbumJSONSerializer(mAppContext, FILENAME);
		try {
			mPictures = mSerializer.loadPictures();
			} catch (Exception e) {
				mPictures = new ArrayList<Picture>();
			Log.e(TAG, "Error loading Pictures: ", e);
			}
		}

	public static PictureLab get(Context c) {
		if (sPictureLab == null) {
			sPictureLab = new PictureLab(c.getApplicationContext());
		}
		return sPictureLab;
	}

	public Picture getPicture(UUID id) {
		for (Picture p : mPictures) {

			if (p.getId().equals(id))
				return p;

		}
		return null;
	}

	public void addPicture(Picture p) {
		mPictures.add(p);
		// need to save picture
	}

	public ArrayList<Picture> getPictures() {
		return mPictures;
	}

	public boolean savePictures() {
		try {
			mSerializer.savePictures(mPictures);
			Log.d(TAG, "Pictures saved to file");
			return true;
		} catch (Exception e) {
			Log.e(TAG, "Error saving Pictures: ", e);
			return false;
		}
	}
	
	public void deletePicture(Picture p) {
		mPictures.remove(p);
		}

}
