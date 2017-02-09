import java.util.HashMap;
import java.util.Map;
public class CoIDetector implements IDetector {
	
	Graph graph;
	
	public CoIDetector() {
		Settings settings = Settings.getInstance();
		this.graph = settings.getGraph();
	}
	
	public CoIDetector(Graph g) {
		this.graph = g;
	}
	
	@Override
	public void detect() {
		//System.out.println("in Detect");
		HashMap<String, ClassInfo> classes = this.graph.getGraph();
		Settings settings = Settings.getInstance();
		
		for (Map.Entry<String, ClassInfo> entry: classes.entrySet()) {
			ClassInfo ci = entry.getValue();
			String name = entry.getKey();
			if (!(ci.isAbsract() && ci.isInterface())) {
				String extendz = ci.getExtends();
				if (!settings.isPrimVal(extendz)) {
					
					DesignParser p = new DesignParser();
					ClassInfo clazz = new ClassInfo(p.parse(extendz));
					
					if (clazz.isAbsract()) {
						ci.setColor("orange");
					}
				}
			}
		}
	}
}