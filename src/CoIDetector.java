import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.MethodNode;

public class CoIDetector implements IDetector {
	
	Graph graph;
	
	public CoIDetector(Graph g) {
		this.graph = g;
	}

	@Override
	public void detect() {
		HashMap<String, ClassInfo> classes = this.graph.getGraph();
		Settings settings = Settings.getInstance();
		
		for (Map.Entry<String, ClassInfo> entry: classes.entrySet()) {
			ClassInfo ci = entry.getValue();
			
			if (!(ci.isAbsract() && ci.isInterface())) {
				List<MethodNode> methods = ci.getMethods();
				
				for (MethodNode m : methods) {
					if (m.visibleAnnotations != null) {
						@SuppressWarnings("unchecked")
						List<AnnotationNode> aNodes = m.visibleAnnotations;
						for (AnnotationNode a : aNodes) {
							System.out.println(a);
						}
					}
				}
				
			}			
		}
	}

}
