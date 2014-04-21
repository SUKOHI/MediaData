package com.sukohi.lib;

/*  Dependencies: MultipleArray  */

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.ContentValues;
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
	public static final String COLUMN_TYPE_STRING = "string";
	public static final String COLUMN_TYPE_INTEGER = "integer";
	public static final String COLUMN_TYPE_BOOLEAN = "boolean";
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
	
	public List<ContentValues> get(List<MediaDataColumn> mediaDataColumnList) {

		List<ContentValues> mediaList = new ArrayList<ContentValues>();
		
		if(mediaDataColumnList.size() == 0) {
			
			return mediaList;
			
		}

		List<String> columnList = new ArrayList<String>();
		
		for (int i = 0; i < mediaDataColumnList.size(); i++) {
			
			columnList.add(mediaDataColumnList.get(i).column);
			
		}
		
		for (int i = 0; i < modes.length; i++) {
			
			int mode = modes[i];
			Uri uri = getUri(mode);
			
			if(contentResolver != null && uri != null) {
				
				String[] columns = columnList.toArray(new String[columnList.size()]);
				Cursor cursor = contentResolver.query(
						uri,
						columns,
						null,
						null,
						null
				);
				
				if(cursor != null && cursor.getCount() > 0) {

					while(cursor.moveToNext()) {
						
						ContentValues contentValues = new ContentValues();
						
						for (int j = 0; j < mediaDataColumnList.size(); j++) {
							
							MediaDataColumn mediaDataColumn =  mediaDataColumnList.get(j);
							String column = mediaDataColumn.column;
							String type = mediaDataColumn.type;
							String value = "";
							
							if(type.equals(COLUMN_TYPE_INTEGER) || type.equals(COLUMN_TYPE_BOOLEAN)) {
								
								int intValue = cursor.getInt(cursor.getColumnIndex(column));
								
								if(type.equals(COLUMN_TYPE_BOOLEAN)) {
									
									boolean booleanValue = (intValue == 0) ? false : true;
									value = String.valueOf(booleanValue);
									
								} else {
									
									value = String.valueOf(intValue);
									
								}
								
							} else {
								
								value = cursor.getString(cursor.getColumnIndex(column));
								
							}
							
							contentValues.put(column, value);
							
						}
						
						mediaList.add(contentValues);
						
					}
					
					cursor.close();
					
				}
				
			}
		
		}

		return mediaList;
		
	}

	public static MediaDataColumn columnString(String column) {
		
		return column(column, COLUMN_TYPE_STRING);
		
	}
	
	public static MediaDataColumn columnInteger(String column) {
		
		return column(column, COLUMN_TYPE_INTEGER);
		
	}
	
	public static MediaDataColumn columnBoolean(String column) {
		
		return column(column, COLUMN_TYPE_BOOLEAN);
		
	}
	
	private static MediaDataColumn column(String column, String type) {
		
		return new MediaDataColumn(column, type);
		
	}
	
	public static class MediaDataColumn {
		
		private String column, type;
		
		public MediaDataColumn(String column, String type) {
			
			this.column = column;
			this.type = type;
			
		}
		
	}
	
}
/*** Example

	MediaData mediaData = new MediaData(this);
	
	mediaData.setMode(MediaData.MODE_AUDIO_INTERNAL); // or like the below
//	mediaData.setModes(new int[]{
//			
//			MediaData.MODE_AUDIO_INTERNAL, 
//			MediaData.MODE_AUDIO_EXTERNAL, 
//			MediaData.MODE_IMAGES_INTERNAL, 
//			MediaData.MODE_IMAGES_EXTERNAL, 
//			MediaData.MODE_VIDEO_INTERNAL, 
//			MediaData.MODE_VIDEO_EXTERNAL;
//			
//	});

	List<MediaDataColumn> columnList = new ArrayList<MediaDataColumn>();
	columnList.add( MediaData.columnString(MediaStore.Audio.Media.TITLE) );
	columnList.add( MediaData.columnString(MediaStore.Audio.Media.ARTIST) );
	columnList.add( MediaData.columnInteger(MediaStore.Audio.Media.DURATION) );
	columnList.add( MediaData.columnBoolean(MediaStore.Audio.Media.IS_MUSIC) );
	List<ContentValues> mediaList = mediaData.get(columnList);
	
	for (int i = 0; i < mediaList.size(); i++) {

		ContentValues mediaValues = mediaList.get(i);
		String title = mediaValues.getAsString(MediaStore.Audio.Media.TITLE);
		String artist = mediaValues.getAsString(MediaStore.Audio.Media.ARTIST);
		long duration = mediaValues.getAsLong(MediaStore.Audio.Media.DURATION);
		boolean isMusic = mediaValues.getAsBoolean(MediaStore.Audio.Media.IS_MUSIC);
		
	}
	
***/
