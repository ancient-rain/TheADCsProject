import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;

public class Settings {
  ArrayList<String> detection;
  ArrayList<String> classes;
  ArrayList<String> blacklist;
  boolean recursive;
  boolean synthetic;
  boolean commandFlags;
  String config = "resources/config.properties";
  
  HashSet<String> blackListClasses;
  
  private final String [] primVals = new String[] {"boolean", "int", "char", "byte" ,"short", "int",
			"long", "float", "double", "Object", "Ojbect[]", "Z", "B", "S", "I", "J", "F",
			"D", "C", "L", "[Z", "[B", "[S", "[I", "[J", "[F", "[D", "[C", "[L", "V", "java/lang/String", "java/lang/Object"};
  private final HashSet<String> prims = new HashSet<String>(Arrays.asList(primVals));  
  private volatile static Settings settings;
  
  private Settings() throws IOException {
	  this.detection = new ArrayList<>();
	  this.classes = new ArrayList<>();
	  this.blacklist = new ArrayList<>();
	  this.blackListClasses = new HashSet<>();
	  
	  this.recursive = false;
	  this.synthetic = false;
	  this.commandFlags = false;
	  this.readSettings();
  }
  
  public static Settings getInstance()  {
	if (settings == null) {
		synchronized (Settings.class) {
			if (settings == null) {
				try {
					settings = new Settings();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	return settings;
  }
  
  public boolean isBlacklisted(String s) {
	  for(String black: this.blackListClasses) {
		  if(s.contains(black)) {
			  return true;
		  }
	  }
	  return false;
	  
  }
  
  public boolean isPrimVal(String p) {	  
	  return this.prims.contains(p);
  }
  
  public boolean getRecursive() {
	  return this.recursive;
  }
  
  public boolean getSynthetic() {
	  return this.synthetic;
  }
  
  public ArrayList<String> getDetectors() {
	  return this.detection;
  }
  
  public ArrayList<String> getClasses() {
	  return this.classes;
  }
  
  public ArrayList<String> getBlackList() {
	  return this.blacklist;
  }
  
  public void setRecursive() {
	  this.recursive = !this.recursive;
  }
  
  public void setSynthetic() {
	  this.synthetic = !this.synthetic;
  }
  
  private void setRecursiveTrue() {
	  this.recursive = true;
  }
  
  private void setSytheticTrue() {
	  this.synthetic = true;
  }
  
  private void setRecursiveFalse() {
	  this.recursive = false;
  }
  
  private void setSyntheticFalse() {
	  this.synthetic = false;
  }
  
  public void setConfig(String config) {
	  this.config = config;
	  try {
		this.readSettings();
	} catch (IOException e) {
		e.printStackTrace();
	}
  }
  
  public void addDetectors(String s) {
	  this.detection.add(s);
  }
  
  private void addClasses(String s) {
	  this.classes.add(s);
  }
  
  private void readSettings() throws IOException {
	  FileInputStream inputStream=null;
	  Properties prop = new Properties();
	  String propName = this.config;
	  try {

		 inputStream = new FileInputStream(propName);
	  } catch (Exception e) {
		  System.out.println("Exception: " + e);
		  e.printStackTrace();
	  }
	  
	  if(inputStream != null) {
		  prop.load(inputStream);
	  } else {
		 throw new FileNotFoundException("Settings file: '" + propName + "' not found in class path");
	  }
	  
	  String flags = prop.getProperty("flags");
	  String recursive = prop.getProperty("recursive");
	  String classes = prop.getProperty("test");
	  String blacklist = prop.getProperty("blacklist");
	  String synthetic = prop.getProperty("synthetic");

	  if (recursive.equals("true")) {
		  this.setRecursiveTrue();
	  } else {
		  this.setRecursiveFalse();
	  }
	  
	  if (synthetic.equals("true")) {
		  this.setSytheticTrue();
	  } else {
		  this.setSyntheticFalse();
	  }
	  
	  String[] flagsList = flags.split("\\s+");
	  
	  for (String s: flagsList) {
		  this.addDetectors(s);
	  }
	  
	  String[] classList = classes.split("\\s+");
	  
	  for(String s: classList) {
		  this.addClasses(s);
//			  System.out.println(s);
	  }
	  
	  String[] blackList = blacklist.split("\\s+");
	  
	  for(String s : blackList) {
		  this.blacklist.add(s); // array
		  this.blackListClasses.add(s); // hashset
	  }
	  	  
	  
  }

public void setCommandFlags() {
	if(!this.commandFlags) {
		this.detection = new ArrayList<String>();
	}
	this.commandFlags = true;
}
}
