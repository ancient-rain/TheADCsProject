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
			
			System.out.println("Interfaces");
			//printing interfaces that are implemented for each class
			ClassInfo ci = g.get(className);
			for(String interfacez : ci.getInterfaces()) {
				System.out.println("\t" + className + " -> " + interfacez + " [arrowhead=\"onormal\", style=\"dashed\"];");
			}
			
			System.out.println("Extends");
			//printing classes that this class extends
			String extendz = ci.getExtends();
			System.out.println("\t" + className + " -> " + extendz + " [arrowhead=\"onormal\"];\n");
		}
	}
	
	public void printAssociations() {
		HashMap<String,ClassInfo> g = this.graph.getGraph();
		HashSet<String> doubleArrow = new HashSet<>();
		System.out.println("associations");
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
					if(g.containsKey(pointsTo)) {
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
								System.out.println("\t" + className + " -> " + entry.getKey() + " [arrowhead=\"vee\", arrowtail=\"vee\", dir=\"both\"];");
							}
						}
					} else {
					
						if (entry.getValue() > 1) {
							System.out.println("\t" + className + " -> " + entry.getKey() + " [arrowhead=\"vee\", headlabel=\"1..*\"];");
						} else {
							System.out.println("\t" + className + " -> " + entry.getKey() + " [arrowhead=\"vee\"];");
						}
					}
				}
			}	
		}
	}
	
	public void printDependencies() {
		HashMap<String,ClassInfo> g = this.graph.getGraph();
		HashSet<String> doubleArrow = new HashSet<>();
		System.out.println("dependencies");
		//go through all of the classes determining associations and dependencies
		for(String className : this.classNames) {
			ClassInfo ci = g.get(className);
			HashMap<String, Integer> fields = ci.getFieldAppear();
			
			if (fields.isEmpty()) {
				HashMap<String, Integer> methods = ci.getMethodAppear();
		
				//loop through all of the methods for the class
				for(Map.Entry<String, Integer> entry : methods.entrySet()) {
					String pointsTo = entry.getKey();
			
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
								System.out.println("\t" + className + " -> " + entry.getKey() + " [arrowhead=\"vee\", arrowtail=\"vee\", dir=\"both\"];");
							}	
						}
					} else {
			
						if (entry.getValue() > 1) {
							System.out.println("\t" + className + " -> " + entry.getKey() + " [arrowhead=\"vee\", headlabel=\"1..*\"];");
						} else {
							System.out.println("\t" + className + " -> " + entry.getKey() + " [arrowhead=\"vee\"];");
						}
					}
				}	
			}
		}
	}

//		} else { // ======================= IF THERE ARE NO ASSOCIATIONS!! ========================
//
//			HashMap<String, HashMap<String,Integer>> numAppear = new HashMap<String, HashMap<String, Integer>>();
//			
//			for (Map.Entry<String, List<MethodNode>> mapEntry: this.depend.entrySet()) {
//				
//				List<MethodNode> methods = mapEntry.getValue();
//				String className = mapEntry.getKey();
//				HashMap<String, Integer> methodAppear = new HashMap<String, Integer>();
//				
//
//				numAppear.put(className, methodAppear);
//			}
//			
//			for (Map.Entry<String, HashMap<String, Integer>> entry: numAppear.entrySet()) {
//				String className = entry.getKey();
//				HashMap<String, Integer> field = entry.getValue();
//				for(Map.Entry<String, Integer> val: field.entrySet()) {
//					String pointTo = val.getKey();
//					int num = val.getValue();
//					
//					String[] trunk = pointTo.split(";");
//					pointTo = trunk[0];
//					
//					if (num > 1) {
//						System.out.println("\t" + className + " -> " + pointTo + " [arrowhead=\"vee\", style=\"dashed\", headlabel=\"1..*\"];");
//					} else {
//						System.out.println("\t" + className + " -> " + pointTo + " [arrowhead=\"vee\", style=\"dashed\"];");
//					}
//				}
//			}
//	}
//}
//	public void addRels(ClassInfo c) {
//		String name = c.getClassName();
//		
//		this.assoc.put(name, c.getFields());
//		this.depend.put(name, c.getMethods());
//	}
//	
//	public void checkCollection(String typeName) {
//		if(!this.prims.contains(typeName)) {
//			DesignParser parse = new DesignParser();
//			ClassInfo info = new ClassInfo(parse.parse(typeName));
//		}
//						
//	}
}
