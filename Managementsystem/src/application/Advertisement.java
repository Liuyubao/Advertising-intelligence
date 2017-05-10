package application;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Advertisement {
	private final StringProperty AdID;
	private final StringProperty AdType;
	private final StringProperty Age;
	private final StringProperty Gender;
	private final StringProperty Chubby;
	private final StringProperty Eyeglasses;
	private final StringProperty Heavy_Makeup;
	private final StringProperty Smiling;

	public Advertisement(String AdID,String AdType,String Age,String Gender, 
			String Chubby, String Eyeglasses, String Heavy_Makeup, String Smiling)
	{
		this.AdID=new SimpleStringProperty(AdID);
		this.AdType=new SimpleStringProperty(AdType);
		this.Age=new SimpleStringProperty(Age);
		this.Gender=new SimpleStringProperty(Gender);
		this.Chubby=new SimpleStringProperty(Chubby);
		this.Eyeglasses=new SimpleStringProperty(Eyeglasses);
		this.Heavy_Makeup=new SimpleStringProperty(Heavy_Makeup);
		this.Smiling=new SimpleStringProperty(Smiling); 
	}

	public String getAdID(){
		return AdID.get();
	}
	
	public void setAdId(String AdID){
		this.AdID.set(AdID);
	}
	
	public StringProperty AdIDProperty(){
		return AdID;
	}
	
	public String getAdType(){
		return AdType.get();
	}
	
	public void setAdType(String AdType){
		this.AdType.set(AdType);
	}
	
	public StringProperty AdTypeProperty(){
		return AdType;
	}
	
	public String getAge(){
		return Age.get();
	}
	
	public void setAge(String Age){
		this.Age.set(Age);
	}
	
	public StringProperty AgeProperty(){
		return Age;
	}
	public String Gender(){
		return Gender.get();
	}
	
	public void setGender(String Gender){
		this.Gender.set(Gender);
	}
	
	public StringProperty GenderProperty(){
		return Gender;
	}
	
	public String getChubby(){
		return Chubby.get();
	}
	
	public void setChubby(String Chubby){
		this.Chubby.set(Chubby);
	}
	
	public StringProperty ChubbyProperty(){
		return Chubby;
	}
	
	public String getEyeglasses(){
		return Eyeglasses.get();
	}
	
	public void setEyeglasses(String Eyeglasses){
		this.Eyeglasses.set(Eyeglasses);
	}
	
	public StringProperty EyeglassesProperty(){
		return Eyeglasses;
	}
	
	public String getHeavy_Makeup(){
		return Heavy_Makeup.get();
	}
	
	public void setHeavy_Makeup(String Heavy_Makeup){
		this.Heavy_Makeup.set(Heavy_Makeup);
	}
	
	public StringProperty Heavy_MakeupProperty(){
		return Heavy_Makeup;
	}
	
	public String getSmiling(){
		return Smiling.get();
	}
	
	public void setSmiling(String Smiling){
		this.Smiling.set(Smiling);
	}
	
	public StringProperty SmilingProperty(){
		return Smiling;
	}
	
}
