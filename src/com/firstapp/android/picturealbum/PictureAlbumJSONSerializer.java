package com.firstapp.android.picturealbum;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import android.content.Context;

public class PictureAlbumJSONSerializer {
	private Context mContext;
	private String mFilename;

	public PictureAlbumJSONSerializer(Context c, String f) {
		mContext = c;
		mFilename = f;
	}
	
	public ArrayList<Picture> loadPictures() throws IOException, JSONException {
		ArrayList<Picture> Pictures = new ArrayList<Picture>();
		BufferedReader reader = null;
		try {
		// Open and read the file into a StringBuilder
		InputStream in = mContext.openFileInput(mFilename);
		reader = new BufferedReader(new InputStreamReader(in));
		StringBuilder jsonString = new StringBuilder();
		String line = null;
		while ((line = reader.readLine()) != null) {
		// Line breaks are omitted and irrelevant
		jsonString.append(line);
		}
		// Parse the JSON using JSONTokener
		JSONArray array = (JSONArray) new JSONTokener(jsonString.toString())
		.nextValue();
		// Build the array of crimes from JSONObjects
		for (int i = 0; i < array.length(); i++) {
			Pictures.add(new Picture(array.getJSONObject(i)));
		}
		} catch (FileNotFoundException e) {
		// Ignore this one; it happens when starting fresh
		} finally {
		if (reader != null)
		reader.close();
		}
		return Pictures;
		}

	public void savePictures(ArrayList<Picture> picture) throws JSONException,
			IOException {
		// Build an array in JSON
		JSONArray array = new JSONArray();
		for (Picture c : picture)
			array.put(c.toJSON());
		// Write the file to disk
		Writer writer = null;
		try {
			OutputStream out = mContext.openFileOutput(mFilename,
					Context.MODE_PRIVATE);
			writer = new OutputStreamWriter(out);
			writer.write(array.toString());
		} finally {
			if (writer != null)
				writer.close();
		}
	}
}