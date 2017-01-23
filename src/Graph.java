import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Graph {
	ArrayList<ClassInfo> classes;
	HashMap<String, ClassInfo> graph;
	
	public Graph(ArrayList<ClassInfo> clazz) {
		this.classes = clazz;
		
		this.graph = new HashMap<String, ClassInfo>();
		this.populateGraph(clazz);
		
		for(ClassInfo ci : clazz) {
			// what do you point too?
			HashMap<String, Integer> fields = ci.getFieldAppear();
			//fields
			for (Map.Entry<String, Integer> entry : fields.entrySet()) {
				String fieldType = entry.getKey();
				if (this.graph.containsKey(fieldType)) {
					ClassInfo inAssoc = this.graph.get(fieldType);
					inAssoc.addInAssoc(fieldType);
				}
			}
			
			HashMap<String, Integer> methods = ci.getMethodAppear();
			//methods
			for (Map.Entry<String, Integer> entry : methods.entrySet()) {
				String methodType = entry.getKey();
				if (this.graph.containsKey(methodType)) {
					ClassInfo inAssoc = this.graph.get(methodType);
					inAssoc.addInAssoc(methodType);
				}
			}
			
		}
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
	
	public List<String> getInwardDepends(String name) {
		ClassInfo clazz = this.graph.get(name);
		return clazz.getInDepend();	
	}
	
	public List<String> getInwardAssoc(String name) {
		ClassInfo clazz = this.graph.get(name);
		return clazz.getInAssoc();	
	}
	
	public HashMap<String, ClassInfo> getGraph() {
		return this.graph;
	}
}
