import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class GVDependencies {
	
	Graph graph; 
	ArrayList<String> classNames;
	Settings settings;
	
	public GVDependencies (Graph g) {
		this.graph = g;
		this.settings = Settings.getInstance();
		this.classNames = new ArrayList<>();
		
		for (ClassInfo ci : this.graph.getClasses()) {
			String className = ci.getClassName();
			this.classNames.add(className);
		}
	}
	
	public void printImplementsAndExtends() {
		
		HashMap<String, ClassInfo> g = this.graph.getGraph();
		
		for(String className : this.classNames) {
			
			//printing interfaces that are implemented for each class
			ClassInfo ci = g.get(className);
			for(String interfacez : ci.getInterfaces()) {
				if (!settings.isBlacklisted(interfacez)) {
					System.out.println("\t<" + className + "> -> <" + interfacez + "> [arrowhead=\"onormal\", style=\"dashed\", color=\"" + ci.getColor() + "\"];");
				}
			}
			
			//printing classes that this class extends
			String extendz = ci.getExtends();
			if(!this.settings.isPrimVal(extendz) && !settings.isBlacklisted(extendz)) {
				System.out.println("\t<" + className + "> -> <" + extendz + "> [arrowhead=\"onormal\", color=\"" + ci.getColor() + "\"];\n");
			}
		}
	}
	
	public void printAssociations() {
		HashMap<String,ClassInfo> g = this.graph.getGraph();
		HashSet<String> doubleArrow = new HashSet<>();
		//go through all of the classes determining associations and dependencies
		for(String className : this.classNames) {
			
			ClassInfo ci = g.get(className);
			HashMap<String, Integer> fields = ci.getFieldAppear();
			
			//loop through all of the fields for the class
			for(Map.Entry<String, Integer> entry : fields.entrySet()) {
				String pointsTo = entry.getKey();
				
				//check to see if the field is not a primitive type
				if (!this.settings.isPrimVal(pointsTo)) {
				
					//check to see if one of the associations is in graph 
					//of classes being analyzed 
					if(g.containsKey(pointsTo) && !pointsTo.equals(className)) {
						ClassInfo ptClass = g.get(pointsTo);
						HashMap<String, Integer> ptFields = ptClass.getFieldAppear();
						
						//check to see if the class that is an association 
						//also associates with the class being analyzed
						if(ptFields.containsKey(className)) {
							String appended1 = className.concat(pointsTo);
							String appended2 = pointsTo.concat(className);
							//check to see if double arrow already exists 
							//between the two classes
							if(!(doubleArrow.contains(appended1) || doubleArrow.contains(appended2))) {
								doubleArrow.add(appended1);
								doubleArrow.add(appended2);
								System.out.println("\t<" + className + "> -> <" + entry.getKey() + "> [arrowhead=\"vee\", style=\"dashed\", arrowtail=\"vee\", dir=\"both\", color=\"" + ci.getColor() + "\"];");
							}
						} else {
							if (entry.getValue() > 1) {
								System.out.println("\t<" + className + "> -> <" + entry.getKey() + "> [arrowhead=\"vee\", style=\"dashed\", headlabel=\"1..*\", color=\"" + ci.getColor() + "\"];");
							} else {
								System.out.println("\t<" + className + "> -> <" + entry.getKey() + "> [arrowhead=\"vee\", style=\"dashed\", color=\"" + ci.getColor() + "\"];");
							}
						}
					} else {
					
						if (entry.getValue() > 1) {
							System.out.println("\t<" + className + "> -> <" + entry.getKey() + "> [arrowhead=\"vee\", style=\"dashed\", headlabel=\"1..*\", color=\"" + ci.getColor() + "\"];");
						} else {
							System.out.println("\t<" + className + "> -> <" + entry.getKey() + "> [arrowhead=\"vee\", style=\"dashed\", color=\"" + ci.getColor() + "\"];");
						}
					}
				}
			}	
		}
	
	}
	
	public void printDependencies() {
		HashMap<String,ClassInfo> g = this.graph.getGraph();
		HashSet<String> doubleArrow = new HashSet<>();
		//go through all of the classes determining associations and dependencies
		for(String className : this.classNames) {
			ClassInfo ci = g.get(className);
			HashMap<String, Integer> fields = ci.getFieldAppear();
			
			if (fields.isEmpty()) {
				HashMap<String, Integer> methods = ci.getMethodAppear();
		
				//loop through all of the methods for the class
				for(Map.Entry<String, Integer> entry : methods.entrySet()) {
					String pointsTo = entry.getKey();
			
					if (!this.settings.isPrimVal(pointsTo) && !this.settings.isBlacklisted(pointsTo)) {
						//check to see if one of the associations is in graph 
						//of classes being analyzed 
						if(g.containsKey(pointsTo)) {
							ClassInfo ptClass = g.get(pointsTo);
							HashMap<String, Integer> ptMethods = ptClass.getMethodAppear();
					
							//check to see if the class that is an association 
							//also associates with the class being analyzed
							if(ptMethods.containsKey(className)) {
								String appended1 = className.concat(pointsTo);
								String appended2 = pointsTo.concat(className);
								//check to see if double arrow already exists 
								//between the two classes
								if(!(doubleArrow.contains(appended1) || doubleArrow.contains(appended2))) {
									doubleArrow.add(appended1);
									doubleArrow.add(appended2);
									System.out.println("\t<" + className + "> -> <" + entry.getKey() + "> [arrowhead=\"vee\", arrowtail=\"vee\", dir=\"both\", color=\"" + ci.getColor() + "\"];");
								}	
							} else {
								if (entry.getValue() > 1) {
									System.out.println("\t<" + className + "> -> <" + entry.getKey() + "> [arrowhead=\"vee\", headlabel=\"1..*\", color=\"" + ci.getColor() + "\"];");
								} else {
									System.out.println("\t<" + className + "> -> <" + entry.getKey() + "> [arrowhead=\"vee\", color=\"" + ci.getColor() + "\"];");
								}
							}
						} else {
				
							if (entry.getValue() > 1) {
								System.out.println("\t<" + className + "> -> <" + entry.getKey() + "> [arrowhead=\"vee\", headlabel=\"1..*\", color=\"" + ci.getColor() + "\"];");
							} else {
								System.out.println("\t<" + className + "> -> <" + entry.getKey() + "> [arrowhead=\"vee\", color=\"" + ci.getColor() + "\"];");
							}
						}
					}	
				}
			}
		}
	}
}
