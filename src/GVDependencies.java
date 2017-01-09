import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.objectweb.asm.Type;

public class GVDependencies {
	ArrayList<String> implement;
	ArrayList<String> extend;
	HashMap<String, ArrayList<HashSet<Type>>> rels;
	
	public GVDependencies() {
		this.implement = new ArrayList<>();
		this.extend = new ArrayList<>();
		this.rels = new HashMap<String, ArrayList<HashSet<Type>>>();
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
		for (Map.Entry<String, ArrayList<HashSet<Type>>> entry: this.rels.entrySet()) {
			ArrayList<HashSet<Type>> r = entry.getValue();
			HashSet<Type> assoc = r.get(0);
			
			if (assoc.isEmpty()) {
				HashSet<Type> depend = r.get(1);
				
				for (Type t : depend) {
					String className = t.getClassName();
					String[] path = className.split("\\.");
					int lastIndex = path.length - 1;
					
					System.out.println("\t" + entry.getKey() + " -> " + path[lastIndex] + " [arrowhead=\"vee\", style=\"dashed\"];");
				}
			} else {
				for (Type t : assoc) {
					String className = t.getClassName();
					String[] path = className.split("\\.");
					int lastIndex = path.length - 1;
					
					System.out.println("\t" + entry.getKey() + " -> " + path[lastIndex] + " [arrowhead=\"vee\"];");
				}
			}
		}
	}
	
	public void addRels(ClassInfo c) {
		String name = c.getClassName();
		HashSet<Type> assoc = c.getAssocTypes();
		HashSet<Type> depend = c.getDependencyTypes();
		ArrayList<HashSet<Type>> a = new ArrayList<HashSet<Type>>();
		
		a.add(assoc);
		a.add(depend);
		
		this.rels.put(name, a);
	}
}
