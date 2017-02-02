import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.MethodNode;


public class SingletonDetector implements IDetector {

	Graph graph;
	
	public SingletonDetector() {
		Settings settings = Settings.getInstance();
		this.graph = settings.getGraph();
	}
	
	public SingletonDetector(Graph g) {
		this.graph = g;
	}
	
	@Override
	public void detect() {
		HashMap<String, ClassInfo> classes = this.graph.getGraph();
		Settings settings = Settings.getInstance();
		for (Map.Entry<String, ClassInfo> entry: classes.entrySet()) {
			boolean fieldSame = false;
			boolean privateInit = false;
			
			ClassInfo ci = entry.getValue();
			String name = entry.getKey();		
			
			HashMap<String, Integer> fields = ci.getFieldAppear();
			List<MethodNode> methods = ci.getMethods();
			
			for(Map.Entry<String, Integer> f: fields.entrySet()) {
				String fName = f.getKey();
				if (fName.equals(name)) {
					
					fieldSame = !fieldSame;
					break;
				}
			}
			
			if(fieldSame) {
				for(MethodNode m : methods) {
					int access = m.access;
					String mName = m.name;
					
					if ((mName.equals("<init>") || mName.equals("<clinit>")) && (Opcodes.ACC_PRIVATE & access) > 0) {
						privateInit = !privateInit;
						break;
					}
				}
				
				if(privateInit) {
					ci.setColor("blue");
					ci.setSterotype("Singleton");
				}
			}
			
			
			
			
			
		
		}
	}

}
