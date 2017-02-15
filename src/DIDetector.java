import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DIDetector implements IDetector {

	Graph graph;
	Settings settings;
	
	public DIDetector() {
		this.settings = Settings.getInstance();
		this.graph = this.settings.getGraph();
	}
	
	
	@Override
	public void detect() {
		ArrayList<ClassInfo> classes = this.graph.getClasses();
		HashMap<String, ClassInfo> classNames = this.graph.getGraph();
		
		for (ClassInfo ci: classes) {
			HashMap<String, Integer> depends = ci.getMethodAppear();
			int violations = 0;
			int nonViolations = 0;
			
			for (Map.Entry<String, Integer> entry: depends.entrySet()) {
				String dependName = entry.getKey();
				
				if (classNames.containsKey(dependName)) {
					ClassInfo clazz = classNames.get(dependName);
					
					if (!clazz.isAbsract() || !clazz.isInterface()) {
						violations++;
					} else {
						nonViolations++;
					}
				}
			}
			
			if (violations > nonViolations) {
				ci.setColor("purple");
			}
		}
	}

}
