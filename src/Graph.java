import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
				HashMap<String, Integer> fields = ci.getFieldAppear();
				HashMap<String, Integer> methods = ci.getMethodAppear();
				List<String> interfaces = ci.getInterfaces();
				String extend = ci.getExtends();
				
				for (Map.Entry<String, Integer> entry : fields.entrySet()) {
					String name = entry.getKey();
					if(!this.graph.containsKey(name) && !settings.isPrimVal(name)) {
						ClassInfo c = new ClassInfo(p.parse(name));
						this.graph.put(name, c);
						this.classes.add(c);
					}
				}
				
				for (Map.Entry<String, Integer> entry : methods.entrySet()) {
					String name = entry.getKey();
					if(!this.graph.containsKey(name) && !settings.isPrimVal(name)) {
						System.out.println(name);
						ClassInfo c = new ClassInfo(p.parse(name));
						this.graph.put(name, c);
						this.classes.add(c);
					}
				}
				
				for (String name: interfaces) {
					if (!this.graph.containsKey(name)) {
						ClassInfo c = new ClassInfo(p.parse(name));
						this.graph.put(name, c);
						this.classes.add(c);
					}
				}
				
				if (!this.graph.containsKey(extend)) {
					ClassInfo c = new ClassInfo(p.parse(extend));
					this.graph.put(extend, c);
					this.classes.add(c);
				}
				
				
				
				
				
				
//				for (FieldNode f: fields) {
//					if (f.signature != null) {
//						//System.out.println(f.signature + " signature");
//						String className = f.signature;
//						String[] path = className.split(";");
//						className = path[0];
////						path = fieldPath.split("/");
////						int lastIndex = path.length - 1;
////						String className = path[lastIndex];
////						System.out.println(className + " " + fieldPath);
//
//						if (!settings.isPrimVal(className)) {
//							System.out.println("primVal Check 1 " + className);
//							if (!this.graph.containsKey(className)) {
//								ClassInfo c = new ClassInfo(p.parse(className));
//								this.graph.put(className, c);
//								this.classes.add(c);
//							}
//						}
//					} else if (f.desc.charAt(0) == '[') {
//						
//						String className = f.desc;
//						String[] path = className.split("L");
//						className = path[1];
//						path = className.split(";");
//						className = path[0];
//						//System.out.println(className + " bracket");
////						String[] path = fieldPath.split(";");
////						fieldPath = path[0];
////						path = fieldPath.split("/");
////						int lastIndex = path.length - 1;
////						String className = path[lastIndex];
////						System.out.println(className + " " + fieldPath);
//
//						if (!settings.isPrimVal(className)) {
//							System.out.println("primVal Check 2" + className);
//
//							if (!this.graph.containsKey(className)) {
//								ClassInfo c = new ClassInfo(p.parse(className));
//								this.graph.put(className, c);
//								this.classes.add(c);
//							}
//						}
//					} else {
//						//System.out.println(f.desc + " normal");
//						String className = f.desc;
//						if (className.charAt(0) == 'L') {
//							className = className.substring(1);
//						}
//						String[] path = className.split(";");
//						className = path[0];
////						path = fieldPath.split("/");
////						int lastIndex = path.length - 1;
////						String className = path[lastIndex];
////						System.out.println(className + " " + fieldPath);
//				
//						if (!settings.isPrimVal(className)) {
//							System.out.println("primVal Check 3" + className);
//
//							System.out.println();
//							if (!this.graph.containsKey(className)) {
//								ClassInfo c = new ClassInfo(p.parse(className));
//								this.graph.put(className, c);
//								this.classes.add(c);
//							}
//						}
//					}
//				}
				
//				for (MethodNode m: methods) {
//					
//				}
			}
			
			for (ClassInfo ci : this.classes){
				System.out.println(ci.getClassName());
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
