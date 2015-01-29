package com.firstapp.android.picturealbum;

import java.util.Date;
import java.util.UUID;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class PictureFragment extends Fragment {
	private static final String TAG = "PictureFragment";
	public static final String EXTRA_PICTURE_ID = "com.firstapp.android.picturealbum.picture_id";
	public static final String EXTRA_ID_EDIT = "com.firstapp.android.picturealbum.edit";
	public static final String DIALOG_DATE = "date";
	public static final String DIALOG_DESCRIPTION = "description";
	public static final int REQUEST_DATE = 0;

	private Picture mPicture;
	private Button mDateButton;
	private Button mEditButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		UUID pictureId = (UUID) getArguments()
				.getSerializable(EXTRA_PICTURE_ID);
		Log.d(TAG, "putExtra() called" + pictureId);
		mPicture = PictureLab.get(getActivity()).getPicture(pictureId);

	}// end onCreate

	@TargetApi(11)
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_picture, parent, false);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			// checking if there is name in meta data
			if (NavUtils.getParentActivityName(getActivity()) != null) {
				getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
			}
		}

		TextView descriptionTextView = (TextView) v
				.findViewById(R.id.description_view);
		descriptionTextView.setText(mPicture.getDescription());

		TextView titleTextView = (TextView) v.findViewById(R.id.title_view);
		titleTextView.setText(mPicture.getTitle());

		mEditButton = (Button) v.findViewById(R.id.image_edit_button);
		mEditButton.setText(R.string.description_and_title);
		mEditButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				// start the DescriptionActivity
				Intent i = new Intent(getActivity(), PictureEditActivity.class);
				i.putExtra(PictureEditFragment.EXTRA_DESCRIPTION_ID,
						mPicture.getId());
				startActivity(i);

			}
		});

		mDateButton = (Button) v.findViewById(R.id.image_date);
		updateDate();
		mDateButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				FragmentManager fm = getActivity().getSupportFragmentManager();
				DatePickerFragment dialog = DatePickerFragment
						.newInstance(mPicture.getDate());
				dialog.setTargetFragment(PictureFragment.this, REQUEST_DATE);
				dialog.show(fm, DIALOG_DATE);
			}
		});

		return v;
	}// end View

	public static PictureFragment newInstance(UUID pictureId) {
		Bundle args = new Bundle();
		args.putSerializable(EXTRA_PICTURE_ID, pictureId);

		PictureFragment fragment = new PictureFragment();
		fragment.setArguments(args);

		return fragment;
	}

	// get the result from DatePickerFragment
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != Activity.RESULT_OK)
			return;
		if (requestCode == REQUEST_DATE) {
			Date date = (Date) data
					.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
			mPicture.setDate(date);
			updateDate();

		}
	}

	private void updateDate() {
		mDateButton.setText(mPicture.getDate().toString());
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			if (NavUtils.getParentActivityName(getActivity()) != null) {
				NavUtils.navigateUpFromSameTask(getActivity());
			}
			// to implement text
			return true;
		case R.id.menu_item_delete_picture:
			Log.i(TAG, "Picture been deleted and return to Picture list");
			PictureLab.get(getActivity()).deletePicture(mPicture);
			if (NavUtils.getParentActivityName(getActivity()) != null) {
				NavUtils.navigateUpFromSameTask(getActivity());
			}
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.picture_fragment_delete_option, menu);
	}


	@Override
	public void onPause() {
		super.onPause();
		PictureLab.get(getActivity()).savePictures();
	}

}// end class
