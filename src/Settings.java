import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class Settings {
  ArrayList<String> detection;
  boolean recursive;
  private final String [] primVals = new String[] {"boolean", "int", "char", "byte" ,"short", "int",
			"long", "float", "double", "Object", "Ojbect[]", "Z", "B", "S", "I", "J", "F",
			"D", "C", "L", "[Z", "[B", "[S", "[I", "[J", "[F", "[D", "[C", "[L", "V", "String"};
  private final HashSet<String> prims = new HashSet<String>(Arrays.asList(primVals));

  private volatile static Settings settings;
  
  private Settings() {
	  this.detection = new ArrayList<>();
	  this.recursive = false;
  }
  
  public static Settings getInstance() {
	if (settings == null) {
		synchronized (Settings.class) {
			if (settings == null) {
				settings = new Settings();
			}
		}
	}
	return settings;
  }
  
  public boolean isPrimVal(String p) {	  
	  return this.prims.contains(p);
  }
  
  public boolean getRecursive() {
	  return this.recursive;
  }
  
  public ArrayList<String> getDetectors() {
	  return this.detection;
  }
  
  public void setRecursive() {
	  this.recursive = !this.recursive;
  }
  
  public void addDetectors(String s) {
	  this.detection.add(s);
  }
}
