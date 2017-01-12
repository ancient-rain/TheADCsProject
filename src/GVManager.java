import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public class GVManager {
	GVdisplayer displayer;
	GVClass cl = new GVClass();
	GVField field = new GVField();
	GVMethod method = new GVMethod();
	GVDependencies depends = new GVDependencies();
	
	public GVManager() {
		this.displayer = null;
		this.cl = new GVClass();
		this.field = new GVField();
		this.method = new GVMethod();
		this.depends = new GVDependencies();
	}
	
	public GVManager(GVdisplayer d) {
		this.displayer = d;
	}
	
	public void displayGVCode(ArrayList<ClassInfo> classes) {
		
		
		System.out.println("digraph uml_diagram {");
		System.out.println("\trankdir = BT;\n");
		
		for(ClassInfo c : classes) {
			List<MethodNode> methods = c.getMethods();
			List<FieldNode> fields = c.getFields();
			List<String> interfaces = c.getInterfaces();
			String superclass = c.getExtends();
			String name = c.getClassName();
			
			// print class name
			this.cl.printClass(c);
			
			// print the fields
			this.field.printFields(fields);
			
			// print the methods
			this.method.printMethods(methods);
			
			for (String i : interfaces) {
				this.depends.addImplements(i, name);
			}
			
			this.depends.addExtend(name, superclass);
			this.depends.addRels(c);
		}
		
		this.depends.printDependencies();
		this.depends.determineRel();
		
		System.out.println("\n}");
	}
}
