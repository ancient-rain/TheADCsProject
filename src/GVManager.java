import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public class GVManager {
	GVClass cl; 
	GVField field; 
	GVMethod method;
	GVDependencies depends;
	Graph graph;
	
	public GVManager(Graph graph) {
		this.cl = new GVClass();
		this.field = new GVField();
		this.method = new GVMethod();
		this.depends = new GVDependencies(graph);
		this.graph = graph;
	}
	
	public void displayGVCode(ArrayList<ClassInfo> classes) {
		DetectorFactory factory = new DetectorFactory(this.graph);
		Settings settings = Settings.getInstance();
		ArrayList<IDetector> detectors = factory.build(this.graph, settings.getDetectors());
		
		for (IDetector d: detectors) {
			d.detect();
		}
		
		System.out.println("digraph uml_diagram {");
		System.out.println("\trankdir = BT;\n");
				
		for(ClassInfo c : this.graph.getClasses()) {
			List<MethodNode> methods = c.getMethods();
			List<FieldNode> fields = c.getFields();
			
			// print class name
			this.cl.printClass(c);
			
			// print the fields
			this.field.printFields(fields);
			
			// print the methods
			this.method.printMethods(methods);
		}
		
		this.depends.printImplementsAndExtends();
		System.out.println("\n");
		this.depends.printAssociations();
		System.out.println("\n");
		this.depends.printDependencies();
		
		System.out.println("\n}");
	}
}
