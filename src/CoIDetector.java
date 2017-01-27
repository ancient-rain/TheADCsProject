import java.util.HashMap;
import java.util.Map;

public class CoIDetector implements IDetector {
	
	IDetector detector;
	Graph graph;
	
	public CoIDetector(IDetector d, Graph g) {
		this.detector = d;
		this.graph = g;
	}

	@Override
	public void detect() {
		HashMap<String, ClassInfo> classes = this.graph.getGraph();
		Settings settings = Settings.getInstance();
		
		for (Map.Entry<String, ClassInfo> entry: classes.entrySet()) {
			ClassInfo ci = entry.getValue();
			String name = entry.getKey();
			
			if (!(ci.isAbsract() && ci.isInterface())) {
				String extendz = ci.getExtends();
				
				if (!settings.isPrimVal(extendz)) {
					
					if (classes.containsKey(extendz)) {
						ClassInfo superC = classes.get(extendz);
						
						if (!(superC.isAbsract())) {
							HashMap<String, Integer> fields = ci.getFieldAppear();
							boolean allPrims = true;
							
							for (Map.Entry<String, Integer> f: fields.entrySet()) {
								String field = f.getKey();
								if (!settings.isPrimVal(field)) {
									allPrims = false;
									break;
								}
							}
							
							if (allPrims) {
								ci.setColor("orange");
							}
						}
					}
				}
			}			
		}
	}

}
