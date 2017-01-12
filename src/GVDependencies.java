import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.objectweb.asm.Type;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

public class GVDependencies {
	
	// =============== IF YOU ADD A FIELD! INSTANTIATE IT!!! STUPID BRONZE SCRUB! ==============================================
	ArrayList<String> implement;
	ArrayList<String> extend;
	HashMap<String, ArrayList<HashSet<Type>>> rels;
	HashSet<String> prims;
	
	HashMap<String, List<FieldNode>> assoc;
	HashMap<String, List<MethodNode>> depend;
	String[] primVals;
	
	public GVDependencies() {
		this.implement = new ArrayList<>();
		this.extend = new ArrayList<>();
		this.rels = new HashMap<String, ArrayList<HashSet<Type>>>();
		
		this.assoc = new HashMap<>();
		this.depend = new HashMap<>();
		
		this.primVals = new String[] {"boolean", "int", "char", "byte" ,"short", "int",
				"long", "float", "double", "java.lang.Object", "java.lang.Ojbect[]"};
		this.prims = new HashSet<String>(Arrays.asList(this.primVals)); 
		
	}

	public void printDependencies() {
		System.out.println();
		
		for (String i : implement) {
			System.out.println(i);
		}
		
		System.out.println();
		
		for (String e : extend) {
			System.out.println(e);
		}
	}
	
	public void addImplements(String i, String name) {
		int length;
		
		String[] paths = i.split("/");
		length = paths.length - 1;
		
		this.implement.add("\t" + name + " -> " + paths[length] + " [arrowhead=\"onormal\", style=\"dashed\"];");
	}
	
	public void addExtend(String name, String superclass) {
		this.extend.add("\t" + name + " -> " + superclass + " [arrowhead=\"onormal\"];\n");
	}
	
	public void determineRel() {
		
		boolean b = false;
		
		for (Map.Entry<String, List<FieldNode>> entry: this.assoc.entrySet()) {
			
			List<FieldNode> a = entry.getValue();
			
			if (!a.isEmpty()) {
				b = true;
			}
		}

		if (b) { // ====================== IF THERE IS AT LEAST ONE FIELD =========================
			
			HashMap<String, HashMap<String,Integer>> numAppear = new HashMap<String, HashMap<String, Integer>>();
			
			for (Map.Entry<String, List<FieldNode>> entry: this.assoc.entrySet()) {
				
				List<FieldNode> fields = entry.getValue();
				String className = entry.getKey();
				HashMap<String, Integer> fieldAppear = new HashMap<String, Integer>();
				
				for (FieldNode f: fields) {
					if (f.signature != null) {
						String colEleType = f.signature;
						String[] path = colEleType.split("/");
						int lastIndex = path.length - 1;
						colEleType = path[lastIndex];
						String[] path2 = colEleType.split(";");
						colEleType = path2[0];
						fieldAppear.put(colEleType, 2);
						
					} else if (f.desc.charAt(0) == '['){
						String colEleType = f.desc;
						String[] path = colEleType.split("/");
						int lastIndex = path.length - 1;
						colEleType = path[lastIndex];
						String[] path2 = colEleType.split(";");
						colEleType = path2[0];
						fieldAppear.put(colEleType, 2);
					} else {
						String type = f.desc;
						String[] path = type.split("/");
						int lastIndex = path.length - 1;
						
						int len = path[lastIndex].length();
						if (len == 1) {
							type = path[lastIndex];
						} else {
							type = path[lastIndex].substring(0, len - 1);
						}

						if(fieldAppear.containsKey(type)) {
							int appear = fieldAppear.get(type);
							fieldAppear.put(type, appear++);
						} else {
							fieldAppear.put(type, 1);
						}
					}
				}
				numAppear.put(className, fieldAppear);
			}
			
			for (Map.Entry<String, HashMap<String, Integer>> entry: numAppear.entrySet()) {
				String className = entry.getKey();
				HashMap<String, Integer> field = entry.getValue();
				for(Map.Entry<String, Integer> val: field.entrySet()) {
					String pointTo = val.getKey();
					int num = val.getValue();
					
					if (num > 1) {
						System.out.println("\t" + className + " -> " + pointTo + " [arrowhead=\"vee\", headlabel=\"1..*\"];");
					} else {
						System.out.println("\t" + className + " -> " + pointTo + " [arrowhead=\"vee\"];");
					}
				}
			}
		} else { // ======================= IF THERE ARE NO ASSOCIATIONS!! ========================

			HashMap<String, HashMap<String,Integer>> numAppear = new HashMap<String, HashMap<String, Integer>>();
			
			for (Map.Entry<String, List<MethodNode>> mapEntry: this.depend.entrySet()) {
				
				List<MethodNode> methods = mapEntry.getValue();
				String className = mapEntry.getKey();
				HashMap<String, Integer> methodAppear = new HashMap<String, Integer>();
				
				for (MethodNode m: methods) {
					if (m.signature != null) {
						String colEleType = m.signature;
						String[] path = colEleType.split("/");
						int lastIndex = path.length - 1;
						colEleType = path[lastIndex];
						String[] path2 = colEleType.split(";");
						colEleType = path2[0];
						methodAppear.put(colEleType, 2);
					} else if (m.desc.charAt(0) == '['){
						String colEleType = m.desc;
						String[] path = colEleType.split("/");
						int lastIndex = path.length - 1;
						colEleType = path[lastIndex];
						String[] path2 = colEleType.split(";");
						colEleType = path2[0];
						methodAppear.put(colEleType, 2);
					} else {
						String type = m.desc;
						String[] path = type.split("/");
						int lastIndex = path.length - 1;
						
						int len = path[lastIndex].length();
						if (len == 1) {
							type = path[lastIndex];
						} else {
							type = path[lastIndex].substring(0, len - 1);
						}
						
						if(methodAppear.containsKey(path[lastIndex])) {
							int appear = methodAppear.get(path[lastIndex]);
							methodAppear.put(path[lastIndex], appear++);
						} else {
							methodAppear.put(path[lastIndex], 1);
						}
					}
				}
				numAppear.put(className, methodAppear);
			}
			
			for (Map.Entry<String, HashMap<String, Integer>> entry: numAppear.entrySet()) {
				String className = entry.getKey();
				HashMap<String, Integer> field = entry.getValue();
				for(Map.Entry<String, Integer> val: field.entrySet()) {
					String pointTo = val.getKey();
					int num = val.getValue();
					
					String[] trunk = pointTo.split(";");
					pointTo = trunk[0];
					
					if (num > 1) {
						System.out.println("\t" + className + " -> " + pointTo + " [arrowhead=\"vee\", style=\"dashed\", headlabel=\"1..*\"];");
					} else {
						System.out.println("\t" + className + " -> " + pointTo + " [arrowhead=\"vee\", style=\"dashed\"];");
					}
				}
			}
	}
}
	public void addRels(ClassInfo c) {
		String name = c.getClassName();
		
		this.assoc.put(name, c.getFields());
		this.depend.put(name, c.getMethods());
	}
	
	public void checkCollection(String typeName) {
		if(!this.prims.contains(typeName)) {
			DesignParser parse = new DesignParser();
			ClassInfo info = new ClassInfo(parse.parse(typeName));
		}
						
	}
}
