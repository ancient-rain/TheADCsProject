import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

public class Graph {
	ArrayList<ClassInfo> classes;
	HashMap<String, ClassInfo> graph;
	
	public Graph(ArrayList<ClassInfo> clazz) {
		this.classes = clazz;
		
		this.graph = new HashMap<String, ClassInfo>();
		this.populateGraph(clazz);
		
		DesignParser p = new DesignParser();
		Settings settings = Settings.getInstance();
		int stop = this.classes.size();
		
		if (settings.getRecursive()) {
			for (int i = 0; i < stop; i++) {
				ClassInfo ci = this.classes.get(i);
				List<FieldNode> fields = ci.getFields();
				List<MethodNode> methods = ci.getMethods();
				
				for (FieldNode f: fields) {
					if (f.signature != null) {
						String fieldPath = f.signature;
						String[] path = fieldPath.split(";");
						fieldPath = path[0];
						path = fieldPath.split("/");
						int lastIndex = path.length - 1;
						String className = path[lastIndex];
						
						if (!settings.isPrimVal(className)) {
							if (!this.graph.containsKey(className)) {
								ClassInfo c = new ClassInfo(p.parse(fieldPath));
								this.graph.put(className, c);
								this.classes.add(c);
							}
						}
					} else if (f.desc.charAt(0) == '[') {
						String fieldPath = f.desc.substring(2);
						String[] path = fieldPath.split(";");
						fieldPath = path[0];
						path = fieldPath.split("/");
						int lastIndex = path.length - 1;
						String className = path[lastIndex];
						
						if (!settings.isPrimVal(className)) {
							if (!this.graph.containsKey(className)) {
								ClassInfo c = new ClassInfo(p.parse(fieldPath));
								this.graph.put(className, c);
								this.classes.add(c);
							}
						}
					} else {
						String fieldPath = f.desc;
						if (fieldPath.charAt(0) == 'L') {
							fieldPath = fieldPath.substring(1);
						}
						String[] path = fieldPath.split(";");
						fieldPath = path[0];
						path = fieldPath.split("/");
						int lastIndex = path.length - 1;
						String className = path[lastIndex];
						System.out.println(className + " " + fieldPath);
				
						if (!settings.isPrimVal(className)) {
							if (!this.graph.containsKey(className)) {
								ClassInfo c = new ClassInfo(p.parse(fieldPath));
								this.graph.put(className, c);
								this.classes.add(c);
							}
						}
					}
				}
				
				for (MethodNode m: methods) {
					
				}
			}
		}
		
		
//		for(ClassInfo ci : clazz) {
//			// what do you point too?
//			HashMap<String, Integer> fields = ci.getFieldAppear();
//			//fields
//			for (Map.Entry<String, Integer> entry : fields.entrySet()) {
//				String fieldType = entry.getKey();
//				if (this.graph.containsKey(fieldType)) {
////					ClassInfo inAssoc = this.graph.get(fieldType);
////					inAssoc.addInAssoc(fieldType);
//				}
//			}
//			
//			HashMap<String, Integer> methods = ci.getMethodAppear();
//			//methods
//			for (Map.Entry<String, Integer> entry : methods.entrySet()) {
//				String methodType = entry.getKey();
//				if (this.graph.containsKey(methodType)) {
////					ClassInfo inAssoc = this.graph.get(methodType);
////					inAssoc.addInAssoc(methodType);
//				}
//			}
//			
//		}
	}
	
	public void populateGraph(List<ClassInfo> clazz) {
		for (ClassInfo ci : clazz) {
			String name = ci.getClassName();
			this.graph.put(name, ci);
		}
	}
	
	public ArrayList<ClassInfo> getClasses() {
		return this.classes;
	}
	
	public HashMap<String, Integer> getOutwardDepends(String name) {
		ClassInfo clazz = this.graph.get(name);
		return clazz.getMethodAppear();
	}
	
	public HashMap<String, Integer> getOutwardAssoc(String name) {
		ClassInfo clazz = this.graph.get(name);
		return clazz.getFieldAppear();
	}
	
//	public List<String> getInwardDepends(String name) {
//		ClassInfo clazz = this.graph.get(name);
//		return clazz.getInDepend();	
//	}
	
//	public List<String> getInwardAssoc(String name) {
//		ClassInfo clazz = this.graph.get(name);
//		return clazz.getInAssoc();	
//	}

	public HashMap<String, ClassInfo> getGraph() {
		return this.graph;
	}
	
	public String getFieldPath() {
		return null;
	}
	
	public List<String> getMethodPath() {
		return null;
	}
}
