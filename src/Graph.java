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
					if(!this.graph.containsKey(name) && !settings.isPrimVal(name) && !settings.isBlacklisted(name)) {
						//System.out.println(name);
						ClassInfo c = new ClassInfo(p.parse(name));
						this.graph.put(name, c);
						this.classes.add(c);
					}
				}
				
				for (Map.Entry<String, Integer> entry : methods.entrySet()) {
					String name = entry.getKey();
					if(!this.graph.containsKey(name) && !settings.isPrimVal(name) && !settings.isBlacklisted(name)) {
						//System.out.println(name);
						ClassInfo c = new ClassInfo(p.parse(name));
						this.graph.put(name, c);
						this.classes.add(c);
					}
				}
				
				for (String name: interfaces) {
					if (!this.graph.containsKey(name) && !settings.isBlacklisted(name)) {
						ClassInfo c = new ClassInfo(p.parse(name));
						this.graph.put(name, c);
						this.classes.add(c);
					}
				}
				
				if (!this.graph.containsKey(extend) && !settings.isPrimVal(extend) &&!settings.isBlacklisted(extend)) {
					ClassInfo c = new ClassInfo(p.parse(extend));
					this.graph.put(extend, c);
					this.classes.add(c);
				}
				
			}
			
//			for (ClassInfo ci : this.classes){
//				System.out.println(ci.getClassName());
//			}
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
