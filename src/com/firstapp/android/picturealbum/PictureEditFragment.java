package com.firstapp.android.picturealbum;

import java.util.UUID;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class PictureEditFragment extends Fragment {

	public static final String EXTRA_DESCRIPTION_ID = "com.firstapp.android.picturealbum.description_id";
	public static final String EXTRA_EDIT = "com.bignerdranch.android.criminalintent.edit";
	public static final int RESULT_OK = 1;
	private Button mSaveBUtton;
	private EditText mDescriptionField;
	private EditText mTitleField;
	private Picture mPicture;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);

		getActivity().setTitle(R.string.edit_image_title);

		UUID pictureId = (UUID) getArguments().getSerializable(
				EXTRA_DESCRIPTION_ID);
		mPicture = PictureLab.get(getActivity()).getPicture(pictureId);

	}

	@TargetApi(11)
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_description, parent, false);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			if (NavUtils.getParentActivityName(getActivity()) != null) {
				getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
			}
		}
		mDescriptionField = (EditText) v
				.findViewById(R.id.description_id_editText);
		mDescriptionField.setText(mPicture.getDescription());
		mDescriptionField.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				mPicture.setDescription(s.toString());

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// This space intentionally left blank
			}

			@Override
			public void afterTextChanged(Editable s) {
				// This one too

			}
		});

		mTitleField = (EditText) v.findViewById(R.id.image_title);
		mTitleField.setText(mPicture.getTitle());

		mTitleField.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				mPicture.setTitle(s.toString());

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// This space intentionally left blank
			}

			@Override
			public void afterTextChanged(Editable s) {
				// This one too

			}
		});

		mSaveBUtton = (Button) v.findViewById(R.id.save_button);
		mSaveBUtton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
			Intent i = new Intent(getActivity(),PicturePagerActivity.class);
			i.putExtra(PictureFragment.EXTRA_PICTURE_ID, mPicture.getId());
			startActivity(i);
			}
		});

		return v;
	}// end view

	public static PictureEditFragment newInstance(UUID pictureId) {
		Bundle args = new Bundle();
		args.putSerializable(EXTRA_DESCRIPTION_ID, pictureId);

		PictureEditFragment fragment = new PictureEditFragment();
		fragment.setArguments(args);

		return fragment;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:

			// check if there is parent activity if true go there
			if (NavUtils.getParentActivityName(getActivity()) != null) {

				NavUtils.navigateUpFromSameTask(getActivity());

			}
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}
}// end class
