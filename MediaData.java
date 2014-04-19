package com.sukohi.lib;

/*  Dependencies: MultipleArray  */

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

public class MediaData {

	public static final int MODE_AUDIO_INTERNAL = 1;
	public static final int MODE_AUDIO_EXTERNAL = 2;
	public static final int MODE_IMAGES_INTERNAL = 3;
	public static final int MODE_IMAGES_EXTERNAL = 4;
	public static final int MODE_VIDEO_INTERNAL = 5;
	public static final int MODE_VIDEO_EXTERNAL = 6;
	private ContentResolver contentResolver;
	private int[] modes;
	
	public MediaData(Context context) {
		
		contentResolver = context.getApplicationContext().getContentResolver();
		
	}
	
	public void setMode(int mode) {
		
		modes = new int[]{mode};
		
	}
	
	public void setModes(int[] modes) {
		
		this.modes = modes;
		
	}
	
	private Uri getUri(int mode) {
		
		Uri uri = null;
		
		switch (mode) {

		case MODE_AUDIO_INTERNAL:
			uri = MediaStore.Audio.Media.INTERNAL_CONTENT_URI;
			break;

		case MODE_AUDIO_EXTERNAL:
			uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
			break;

		case MODE_IMAGES_INTERNAL:
			uri = MediaStore.Images.Media.INTERNAL_CONTENT_URI;
			break;

		case MODE_IMAGES_EXTERNAL:
			uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
			break;

		case MODE_VIDEO_INTERNAL:
			uri = MediaStore.Video.Media.INTERNAL_CONTENT_URI;
			break;

		case MODE_VIDEO_EXTERNAL:
			uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
			break;
		
		}
		
		return uri;
		
	}
	
	public MultipleArray get(String[] columns) {
		
		MultipleArray multipleArray = new MultipleArray();
		int arrayIndex = 0;
		
		for (int i = 0; i < modes.length; i++) {
			
			int mode = modes[i];
			Uri uri = getUri(mode);
			
			if(contentResolver != null && uri != null) {
				
				Cursor cursor = contentResolver.query(
						uri,
						columns,
						null,
						null,
						null
				);
				
				if(cursor != null && cursor.getCount() > 0) {

					while(cursor.moveToNext()) {
						
						multipleArray.setValue("[MediaData]["+ arrayIndex +"][mediaDataMode]", String.valueOf(mode));
						
						for (int j = 0; j < columns.length; j++) {
							
							String column = columns[j];
							String value = cursor.getString(cursor.getColumnIndex(column));
							multipleArray.setValue("[MediaData]["+ arrayIndex +"]["+ column +"]", value);
							
						}
						
						arrayIndex++;
						
					}
					
					cursor.close();
					
				}
				
			}
		
		}

		return multipleArray;
		
	}
	
}
/*** Example

	MediaData mediaData = new MediaData(context);
	
	mediaData.setMode(MediaData.MODE_AUDIO_INTERNAL); // or the below
	mediaData.setModes(new int[]{
			
			MediaData.MODE_AUDIO_INTERNAL, 
			MediaData.MODE_AUDIO_EXTERNAL
			
	});
	MultipleArray multipleArray = mediaData.get(new String[]{
			
			MediaStore.Audio.Media.TITLE, 
			MediaStore.Audio.Media.ARTIST, 
			MediaStore.Audio.Media.DURATION
			
	});
	
	int multipleArrayCount = multipleArray.getCount("[MediaData]");
		
	for (int i = 0; i < multipleArrayCount; i++) {

		String title = multipleArray.getString("[MediaData]["+ i +"][title]");
		String artist = multipleArray.getString("[MediaData]["+ i +"][artist]");
		long duration = multipleArray.getLong("[MediaData]["+ i +"][duration]");
		int isMusic = multipleArray.getInt("[MediaData]["+ i +"][is_music]");
		
	}
	
***/
