import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public class GraphViz {
	GVdisplayer displayer;
	
	public GraphViz() {
		this.displayer = null;
	}
	
	public GraphViz(GVdisplayer d) {
		this.displayer = d;
	}
	
	public void displayGVCode(ArrayList<ClassInfo> classes) {
		ArrayList<String> implement = new ArrayList<>();
		ArrayList<String> extend = new ArrayList<>();
		
		System.out.println("digraph uml_diagram {");
		System.out.println("\trankdir = BT;\n");
		
		for(ClassInfo c : classes) {
			List<MethodNode> methods = c.getMethods();
			List<FieldNode> fields = c.getFields();
			List<String> interfaces = c.getInterfaces();
			String superclass = c.getExtends();
			String name = c.getClassName();
				
			System.out.println("\t" + name + " [");
			System.out.println("\t\tshape = \"record\",");

			System.out.print("\t\tlabel = <{");
			
			if (c.isInterface()) {
				System.out.print("interface<BR /><I>" + c.getClassName() + "</I>");
			} else {
				System.out.print(c.getClassName());
			}
					
			if (!fields.isEmpty() || !methods.isEmpty()) {
				System.out.print("|");
			}
			
			// print the fields
			for (FieldNode f : fields) {
				int access = f.access;
				String fName = f.name;
				Type returnValue = Type.getType(f.desc);
				String returnString = returnValue.getClassName();
				String[] path = returnString.split("\\.");
				
				if ((access & Opcodes.ACC_PUBLIC) > 0) {
					System.out.print("+ ");
				} else if ((access & Opcodes.ACC_PROTECTED) > 0) {
					System.out.print("# ");
				} else {
					System.out.print("- ");
				}

				System.out.print(fName + ": " + path[path.length - 1] + "<BR ALIGN=\"LEFT\"/>");				
			}
			
			if (!fields.isEmpty()) {
				System.out.print("|");
			}
			
			// print the methods
			for (MethodNode m : methods) {
				int access = m.access;
				String mName = m.name;
				Type returnValue = Type.getReturnType(m.desc);
				String returnString = returnValue.getClassName();
				String[] path = returnString.split("\\.");
				
				if ((access & Opcodes.ACC_PUBLIC) > 0) {
					System.out.print("+ ");
				} else if ((access & Opcodes.ACC_PROTECTED) > 0) {
					System.out.print("# ");
				} else {
					System.out.print("- ");
				}
				
				if (mName.equals("<init>")) {
					mName = "Constructor";
				}
				
				if (mName.equals("<clinit>")) {
					mName = "Constructor";
				}
				
				System.out.print(mName + ": " + path[path.length - 1] + "<BR ALIGN=\"LEFT\"/>");
			}
			
			System.out.print("}>\n\t];\n\n");
			
			for (String i : interfaces) {
				int length;
				
				String[] paths = i.split("/");
				length = paths.length - 1;
								
				implement.add("\t" + name + " -> " + paths[length] + " [arrohead=\"onormal\", style=\"dashed\"];");
			}
			
			extend.add("\t" + name + " -> " + superclass + " [arrohead=\"onormal\"];");
		}
		
		System.out.println();
		
		for (String i : implement) {
			System.out.println(i);
		}
		
		System.out.println();
		
		for (String e : extend) {
			System.out.println(e);
		}
		
		System.out.println("\n}");
		
	}
}
