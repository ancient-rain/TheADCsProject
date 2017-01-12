import java.util.ArrayList;
import java.util.HashMap;


public class Graph {
	HashMap<String, ClassInfo> graph;
	
	public Graph() {
		this.graph = new HashMap<>();
	}
	
	public Graph(String name, ClassInfo v) {
		this.graph = new HashMap<>();
		this.graph.put(name, v);
	}
	
	public void addClassInfo(String name, ClassInfo v) {
		this.graph.put(name, v);
	}
	
	public ArrayList<ClassInfo> getOutward(String name, ClassInfo v) {
		ClassInfo node = graph.get(name);
		
		return node.getOutward();
	}
	
	public ArrayList<ClassInfo> getInward(String name, ClassInfo v) {
		ClassInfo node = graph.get(name);
		
		return node.getInward();
	}
	
	public void addInward(String name, ClassInfo v) {
		ClassInfo node = this.graph.get(name);
		node.addInward(v);
	}
	
	public void addOutward(String name, ClassInfo v) {
		ClassInfo node = this.graph.get(name);
		node.addOutward(v);
	}
}
