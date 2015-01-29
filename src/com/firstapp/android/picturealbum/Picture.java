package com.firstapp.android.picturealbum;

import java.util.Date;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

public class Picture {

	private static final String JSON_ID = "id";
	private static final String JSON_TITLE = "title";
	private static final String JSON_DESCRIPTION = "description";
	private static final String JSON_DATE = "date";

	private UUID mId;
	private String mTitle;
	private Date mDate;
	private String mDescription;

	public Picture() {
		mId = UUID.randomUUID();
		mDate = new Date();
	}

	public Picture(JSONObject json) throws JSONException {
		mId = UUID.fromString(json.getString(JSON_ID));
		if (json.has(JSON_TITLE)) {
			mTitle = json.getString(JSON_TITLE);
		}
		mDescription = json.getString(JSON_DESCRIPTION);
		mDate = new Date(json.getLong(JSON_DATE));
	}

	public JSONObject toJSON() throws JSONException {
		JSONObject json = new JSONObject();
		json.put(JSON_ID, mId.toString());
		json.put(JSON_TITLE, mTitle);
		json.put(JSON_DESCRIPTION, mDescription);
		json.put(JSON_DATE, mDate.getTime());
		return json;
	}

	@Override
	public String toString() {
		return mTitle;
	}

	public String getTitle() {
		return mTitle;
	}

	public void setTitle(String title) {
		mTitle = title;
	}

	public Date getDate() {
		return mDate;
	}

	public void setDate(Date date) {
		mDate = date;
	}

	public String getDescription() {
		return mDescription;
	}

	public void setDescription(String description) {
		mDescription = description;
	}

	public UUID getId() {
		return mId;
	}

}
