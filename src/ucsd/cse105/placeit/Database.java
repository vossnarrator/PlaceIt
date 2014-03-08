package ucsd.cse105.placeit;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

public class Database {
	private static final String DATE_FORMAT = "MM/dd/yyyy HH:mm:ss";

	private static final String FILE_ZOOM = "zoom_file";
	public static float getLastZoom(Activity a){
		Log.d("Database.getLastZoom", "entering getLastZoom");
		try {
			FileInputStream fis = a.openFileInput(FILE_ZOOM);
			String[] content = reader(fis);
			Log.d("Database.getLastZoom", "returning for zoom level " + content[0]);
			return Float.parseFloat(content[0]);
		} catch (FileNotFoundException e ) {
			Log.d("Database.getLastZoom", "File not found");
			e.printStackTrace();
		} catch (ArrayIndexOutOfBoundsException f){
			Log.d("Database.getLastZoom", "No file found");
			
		}
		//default return value if file isn't found
		return 20;
	}
	public static void saveZoomLevel(float level, MapActivity a) {
		String data = Float.toString(level);
		Log.d("Database.saveZoomLevel", data + " is the saved zoom level");
		try {
			FileOutputStream fos = a.openFileOutput(FILE_ZOOM, Context.MODE_PRIVATE);
			writer(fos, new String[]{data});
		} catch (FileNotFoundException e) {
			Log.d("Database", "something went wrong?");
			e.printStackTrace();
		}
			
		
	}

	
	
	
	private static final String FILE_USERNAME = "username file";
	public static void saveUsername(String username, Activity a){
		
		try {
			FileOutputStream fos = a.openFileOutput(FILE_USERNAME, Context.MODE_PRIVATE);
			writer(fos, new String[]{username});
		} catch (FileNotFoundException e) {
			Log.d("Database", "something went wrong?");
			e.printStackTrace();
		}
		
	}
	public static String getUsername(Activity a){
		try {
			FileInputStream fis = a.openFileInput(FILE_USERNAME);
			return reader(fis)[0];
		} catch (FileNotFoundException e) {
			Log.d("Database", "something went wrong?");
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	//checks to see if the user has already logged in
	private static final String FILE_CREDENTIALS = "logincredentials file";
	public static boolean checkLoginCredentials(Activity a) {
		try{
			FileInputStream fis = a.openFileInput(FILE_CREDENTIALS);
			String content = reader(fis)[0];
			
			return Boolean.parseBoolean(content);
		}catch(Exception e){
			return false;
		}
	}
	public static void clearCredentials(Activity a) {
		Log.d("Database.clearCredentials", "clearing credentials");
		try {
			FileOutputStream fos = a.openFileOutput(FILE_CREDENTIALS, Context.MODE_PRIVATE);
			//write the one line to the file
			writer(fos, new String[]{Boolean.FALSE.toString()});
		} catch (FileNotFoundException e) {
			Log.d("Database", "something went wrong?");
			e.printStackTrace();
		}
	}
	//checks to see if the login was correct
	public static boolean checkLogin(String username, String password, Activity a){
		if(!password.equals("password"))
			return false;
		
		if(username.equals("account1") || username.equals("acount2")){
			saveUsername(username, a);
			return true;
		}
		
		return false;
		
	}
	
	
	
	
	private static final String FILE_POSITION = "last_known_location file";
	public static void savePosition(LatLng pos, Activity a) {
		String[] data = new String[]{Double.toString(pos.latitude), Double.toString(pos.longitude)};
		
		try {
			FileOutputStream fos = a.openFileOutput(FILE_POSITION, Context.MODE_PRIVATE);
			writer(fos, data);
		} catch (FileNotFoundException e) {
			Log.d("Database", "something went wrong?");
			e.printStackTrace();
		}
	}
	public static LatLng getLastPosition(Activity a){
		Log.d("Database.getLastZoom", "entering getLastZoom");
		try {
			FileInputStream fis = a.openFileInput(FILE_ZOOM);
			String[] content = reader(fis);
			double lat = Double.parseDouble(content[0]);
			double lng = Double.parseDouble(content[1]);
			return new LatLng(lat, lng);
		} catch (FileNotFoundException e ) {
			Log.d("Database.getLastZoom", "File not found");
			e.printStackTrace();
		} catch (ArrayIndexOutOfBoundsException f){
			Log.d("Database.getLastZoom", "No file found");
		
	}
	//default return value if file isn't found
		return new LatLng(0,0);
	}

	
	
	
	
	
	
	private static final String FILE_PLACEITS = "place_it_file";
	//returns an active list of all the placeits
	public static ArrayList<LocationPlaceIt> getAllPlaceIts(Context a) {

		try {
			FileInputStream fis = a.openFileInput(FILE_PLACEITS);
			String[] content = reader(fis);
			
			int position = 0; //what part of the content you're in
			int count = Integer.parseInt(content[position++]);

			ArrayList<LocationPlaceIt> list = new ArrayList<LocationPlaceIt>(count);
			for(int i = 0; i < count; i++){
				Log.d("Database.getAllPlaceIts", "# " + Integer.toString(i) + " placeit being loaded");
				String title = content[position++];
				Log.d("Database.getAllPlaceIts", "title of placeit being created: " + title);
				String description = content[position++];
				Log.d("Database.getAllPlaceIts", "description of placeit being created: " + description);
				String id_int = content[position++];
				Log.d("Database.getAllPlaceIts", "id of placeit being created: " + id_int);
				String lat_double = content[position++];
				Log.d("Database.getAllPlaceIts", "latitude of placeit being created: " + lat_double);
				String long_double = content[position++];
				Log.d("Database.getAllPlaceIts", "longitude of placeit being created: " + long_double);
				String dueDate_Date = content[position++];
				Log.d("Database.getAllPlaceIts", "dueDate of placeit being created: " + dueDate_Date);
				String schedule_String = content[position++];
				
				LatLng location = new LatLng(Double.parseDouble(lat_double), Double.parseDouble(long_double));
				
				LocationPlaceIt p = new LocationPlaceIt(Integer.parseInt(id_int));
				p.setLocation(location);
				p.setTitle(title);
				p.setDescription(description);
				try {
					SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT);
					p.setDueDate(df.parse(dueDate_Date));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				p.setSchedule(Integer.parseInt(schedule_String));
				list.add(p);
			}
			
			return list;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return new ArrayList<LocationPlaceIt>();
	}

	public static LocationPlaceIt getPlaceIt(int id, Context a){
		Log.d("Database.getPlaceIt", "trying to find placeIt with id #: " + Integer.toString(id));
		for(LocationPlaceIt p: getAllPlaceIts(a))
			if(p.getID() == id)
				return p;

		return null;
	}
	
	public static IPlaceIt getPlaceIt(LatLng pos, Context a){
		for(IPlaceIt p: getAllPlaceIts(a))
			if(p instanceof LocationPlaceIt)
				if(((LocationPlaceIt) p).getLocation().equals(pos))
					return p;

		return null;
	}	
	
	public static void removePlaceIt(int placeItID, Context a) {
		Log.d("Database.removePlaceIt", "called here");
		ArrayList<LocationPlaceIt> list = getAllPlaceIts(a);
		for(IPlaceIt p: list)
			if(p.getID() == (placeItID)){
				Log.d("Database.remove", "found the placeit to remove");
				list.remove(p);
				writePlaceIts(list, a);
				return;
			}
		Log.d("Database.remove", "Unable to find the placeit to remove");
	}

	public static void save(LocationPlaceIt p, Context a) {
		if(getPlaceIt(p.getID(), a) != null){
			Log.d("Database.save", "going to call removePlaceit");
			removePlaceIt(p.getID(), a);
		}
		ArrayList<LocationPlaceIt> list = getAllPlaceIts(a);
		
		list.add(p);
		
		writePlaceIts(list, a);
	}
	private static void writePlaceIts(ArrayList<LocationPlaceIt> list, Context a){
		ArrayList<String> stringList = new ArrayList<String>();
		stringList.add(Integer.toString(list.size()));
		
		SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT);
		
		for(LocationPlaceIt p: list){
			stringList.add(p.getTitle());
			stringList.add(p.getDescription());
			stringList.add(Integer.toString(p.getID()));
			stringList.add(Double.toString(p.getLocation().latitude));
			stringList.add(Double.toString(p.getLocation().longitude));
			stringList.add(df.format(p.getDueDate()));
			stringList.add(Integer.toString(p.getSchedule()));
		}
		
		String[] array = toStringArray(stringList.toArray());
		
		Log.d("Database.writing", "This is the contents of the write");
		for(String s: array)
			Log.d("Database.writing", s);
		
		try {
			writer(a.openFileOutput(FILE_PLACEITS, Context.MODE_PRIVATE), array);
		} catch (FileNotFoundException e) {
			Log.d("Database", "Something went wrong in saving PlaceIt");
		}
	}
	private static String[] toStringArray(Object[] input){
		String[] array = new String[input.length];
		for(int i = 0; i < array.length; i++)
			array[i] = (String) input[i];
		return array;
	}
	
	
	
	
	private static void writer(FileOutputStream fos, String[] data){
		try{
			OutputStreamWriter out = new OutputStreamWriter(fos);
			BufferedWriter writer = new BufferedWriter(out);
			
			for(String s: data)
				writer.write(s+"\n");
			writer.close();
			out.flush();
			out.close();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private static String[] reader(FileInputStream fis){
		try {
			InputStreamReader input = new InputStreamReader(fis);
			BufferedReader reader = new BufferedReader(input);
			
			ArrayList<String> list = new ArrayList<String>();
			while(true){
				String s = reader.readLine();
				Log.d("Database", " Read line: " + s);
				if(s == null)
					break;
				list.add(s);
			}
			
			if(list.size() == 0)
				Log.d("Database", "Came up with empty file");
			
			reader.close();
			input.close();
			fis.close();
			
			
			return toStringArray(list.toArray());
			
		} catch (FileNotFoundException e) {
			Log.d("Database", "unable to get last zoom level");
			e.printStackTrace();
		} catch (IOException e) {
			Log.d("Database", "unable to get last zoom level");
			e.printStackTrace();
		}
		return null;
	}
	public static ArrayList<LocationPlaceIt> getCompletedPlaceIts(
			PullDownListActivity pullDownListActivity) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
