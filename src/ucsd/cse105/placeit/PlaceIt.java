package ucsd.cse105.placeit;


import java.util.Date;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.EditText;

import com.google.android.gms.maps.model.LatLng;

public class PlaceIt implements Parcelable {

	private LatLng location;
	private String title = "";
	private String description = "";
	private int id;
	private Date dueDate;
	
	public PlaceIt(LatLng location, int id){
		this.location = location;
		this.id = id;
	}
	
	public void setDueDate(Date dueDate){
		this.dueDate = dueDate;
	}
	
	public void setTitle(EditText text){
		title = getText(text);
	}
	public void setTitle(String title){
		this.title = title;
	}
	public void setDescription(EditText text){
		description = getText(text);
	}
	public void setDescription(String description){
		this.description = description;
	}
	private String getText(EditText text){
		return text.getText().toString();
	}
	
	public Date getDueDate(){
		return dueDate;
	}
	
	public String getTitle(){
		if(title.length() == 0){
			int max = Math.max(description.length(), 20);
			return description.substring(0, max);
		}
		return title;
	}
	public String getDescription(){
		return description;
	}
	public LatLng getLocation(){
		return location;
	}

//	public void setID(Long id){
//		this.id = id;
//	}

	public int describeContents() {

		return 0;
	}


	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(description);
		dest.writeString(title);
		dest.writeDouble(location.latitude);
		dest.writeDouble(location.longitude);
		dest.writeLong(id);
	}
	
	public PlaceIt(Parcel in){
		description = in.readString();
		title = in.readString();
		location = new LatLng(in.readDouble(), in.readDouble());
		id = in.readInt();
	}
	
	public int getID(){
		return id;
	}
	
	public static final Parcelable.Creator<PlaceIt> CREATOR = new Parcelable.Creator<PlaceIt>() {
		  public PlaceIt createFromParcel(Parcel in) {
			  return new PlaceIt(in); 
		  }
		   
		  public PlaceIt[] newArray(int size) {
		     return new PlaceIt[size];
		  	}
		  };
}
