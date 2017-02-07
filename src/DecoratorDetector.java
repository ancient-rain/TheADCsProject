import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DecoratorDetector implements IDetector {

	Graph graph;
	Settings settings;
	
	public DecoratorDetector() {
		this.settings = Settings.getInstance();
		this.graph = this.settings.getGraph();
	}
	
	@Override
	public void detect() {
		ArrayList<ClassInfo> classes = this.graph.getClasses();
		HashMap<String, ClassInfo> classNames = this.graph.getGraph();
		
		for (ClassInfo ci: classes) {
			String superType = ci.getExtends();
			HashMap<String, Integer> fields = ci.getFieldAppear();
			
			for (Map.Entry<String, Integer> entry: fields.entrySet()) {
				String field = entry.getKey();
				
				if (superType.equals(field)) {
					ci.setColor("green");
					ci.setSterotype("Decorator");
				}
			}
		}
	}

}
