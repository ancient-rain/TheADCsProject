import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public class GVManager {
	GVdisplayer displayer;
	
	public GVManager() {
		this.displayer = null;
	}
	
	public GVManager(GVdisplayer d) {
		this.displayer = d;
	}
	
	public void displayGVCode(ArrayList<ClassInfo> classes) {
		GVClass cl = new GVClass();
		GVField field = new GVField();
		GVMethod method = new GVMethod();
		GVDependencies depends = new GVDependencies();
		
		System.out.println("digraph uml_diagram {");
		System.out.println("\trankdir = BT;\n");
		
		for(ClassInfo c : classes) {
			List<MethodNode> methods = c.getMethods();
			List<FieldNode> fields = c.getFields();
			List<String> interfaces = c.getInterfaces();
			String superclass = c.getExtends();
			String name = c.getClassName();
			
			// print class name
			cl.printClass(c);
			
			// print the fields
			field.printFields(fields);
			
			// print the methods
			method.printMethods(methods);
			
			for (String i : interfaces) {
				depends.addImplements(i, name);
			}
			
			depends.addExtend(name, superclass);
			depends.addRels(c);
		}
		
		depends.printDependencies();
		depends.determineRel();
		
		System.out.println("\n}");
	}
}
