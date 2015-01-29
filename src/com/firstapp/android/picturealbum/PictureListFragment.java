package com.firstapp.android.picturealbum;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class PictureListFragment extends ListFragment {
	private static final String TAG = "PictureListFragment";
	private ArrayList<Picture> mPictures;
	private boolean mSubtitleVisible;
	private Button mPicBtn;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setRetainInstance(true);
		mSubtitleVisible = false;
		setHasOptionsMenu(true);
		getActivity().setTitle(R.string.image_title);
		mPictures = PictureLab.get(getActivity()).getPictures();
		PictureAdapter adapter = new PictureAdapter(mPictures);
		setListAdapter(adapter);

	}// end onCreate

	@TargetApi(11)
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.empty_or_view_list_item, container,
				false);
		ListView listView = (ListView) v.findViewById(android.R.id.list);
		listView.setEmptyView(v.findViewById(android.R.id.empty));

		mPicBtn = (Button) v.findViewById(R.id.btn_add_image);
		mPicBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Picture picture = new Picture();
				PictureLab.get(getActivity()).addPicture(picture);
				Intent i = new Intent(getActivity(), PicturePagerActivity.class);
				i.putExtra(PictureFragment.EXTRA_PICTURE_ID, picture.getId());
				startActivityForResult(i, 0);

			}
		});
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			if (mSubtitleVisible) {
				getActivity().getActionBar().setSubtitle(R.string.subtitle);
			}
		}
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
			// Use floating context menus on Froyo and Gingerbread
			registerForContextMenu(listView);
		} else {
			// Use contextual action bar on Honeycomb and higher
			listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
			listView.setMultiChoiceModeListener(new MultiChoiceModeListener() {

				@Override
				public void onItemCheckedStateChanged(
						android.view.ActionMode mode, int position, long id,
						boolean checked) {
					// Required, but not used in this implementation

				}

				@Override
				public boolean onCreateActionMode(android.view.ActionMode mode,
						Menu menu) {
					MenuInflater inflater = mode.getMenuInflater();
					inflater.inflate(R.menu.picture_list_item_context, menu);
					return true;
				}

				@Override
				public boolean onPrepareActionMode(
						android.view.ActionMode mode, Menu menu) {
					// Required, but not used in this implementation
					return false;
				}

				@Override
				public boolean onActionItemClicked(
						android.view.ActionMode mode, MenuItem item) {
					switch (item.getItemId()) {
					case R.id.menu_item_delete_picture:
						PictureAdapter adapter = (PictureAdapter) getListAdapter();
						PictureLab pictureLab = PictureLab.get(getActivity());
						for (int i = adapter.getCount() - 1; i >= 0; i--) {
							if (getListView().isItemChecked(i)) {
								pictureLab.deletePicture(adapter.getItem(i));
							}
						}
						mode.finish();
						adapter.notifyDataSetChanged();
						return true;
					default:
						return false;
					}
				}

				@Override
				public void onDestroyActionMode(android.view.ActionMode mode) {
					// Required, but not used in this implementation

				}

			});
		}

		return v;
	}

	// inflating an option menu
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.fragment_picture_list, menu);
		MenuItem showSubtitle = menu.findItem(R.id.menu_item_show_subtitle);
		if (mSubtitleVisible && showSubtitle != null) {
			showSubtitle.setTitle(R.string.hide_subtitle);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_item_new_image:
			Picture picture = new Picture();
			PictureLab.get(getActivity()).addPicture(picture);
			Intent i = new Intent(getActivity(), PicturePagerActivity.class);
			i.putExtra(PictureFragment.EXTRA_PICTURE_ID, picture.getId());
			startActivityForResult(i, 0);
			return true;
		case R.id.menu_item_show_subtitle:
			if (getActivity().getActionBar().getSubtitle() == null) {
				getActivity().getActionBar().setSubtitle(R.string.subtitle);
				mSubtitleVisible = true;
				item.setTitle(R.string.hide_subtitle);
			} else {
				getActivity().getActionBar().setSubtitle(null);
				mSubtitleVisible = false;
				item.setTitle(R.string.show_subtitle);
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Picture p = ((PictureAdapter) getListAdapter()).getItem(position);

		// start the PicturePagerActivity
		Intent i = new Intent(getActivity(), PicturePagerActivity.class);
		i.putExtra(PictureFragment.EXTRA_PICTURE_ID, p.getId());
		startActivityForResult(i, 0);
		Log.d(TAG, "putExtra() called" + p.getId().toString());
	}

	private class PictureAdapter extends ArrayAdapter<Picture> {
		public PictureAdapter(ArrayList<Picture> pictures) {
			super(getActivity(), android.R.layout.simple_list_item_1, pictures);
		}

		@SuppressLint("InflateParams")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// if we weren't given a view, inflate one
			if (convertView == null) {
				// inflate R.layout.list_item_crime,
				convertView = getActivity().getLayoutInflater().inflate(
						R.layout.list_item_picture, null);
			}
			// Configure the view for this crime
			// get the data in position
			Picture p = getItem(position);
			// convert from memory to text view
			TextView titleTextView = (TextView) convertView
					.findViewById(R.id.picture_list_titleTextView);
			titleTextView.setText(p.getTitle());

			TextView descriptionTextView = (TextView) convertView
					.findViewById(R.id.picture_list_descriptionTextView);
			descriptionTextView.setText(p.getDescription());

			TextView dateTextView = (TextView) convertView
					.findViewById(R.id.picture_list_dateTextView);
			dateTextView.setText(p.getDate().toString());
			return convertView;
		}

	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		getActivity().getMenuInflater().inflate(
				R.menu.picture_list_item_context, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		int position = info.position;
		PictureAdapter adapter = (PictureAdapter) getListAdapter();
		Picture picture = adapter.getItem(position);
		switch (item.getItemId()) {
		case R.id.menu_item_delete_picture:
			PictureLab.get(getActivity()).deletePicture(picture);
			adapter.notifyDataSetChanged();
			return true;
		}
		return super.onContextItemSelected(item);
	}

	@Override
	public void onPause() {
		super.onPause();
		PictureLab.get(getActivity()).savePictures();
	}

}// End class
