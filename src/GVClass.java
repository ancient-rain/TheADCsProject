import java.util.List;

import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

public class GVClass {

	public GVClass() {
		
	}
	
	public void printClass(ClassInfo c) {
		List<MethodNode> methods = c.getMethods();
		List<FieldNode> fields = c.getFields();
		String name = c.getClassName();
		
		System.out.println("\t<" + name + "> [");
		System.out.println("\t\tshape = \"record\",");
		
		String color = c.getColor();
		
		if (!color.equals("black")) {
			System.out.println("\t\tcolor = \"" + color + "\",");
		}

		System.out.print("\t\tlabel = <{");
		
		if (c.isInterface()) {
			System.out.print("interface<BR /><I>" + c.getClassName() + "</I>");
		} else {
			System.out.print(c.getClassName());
		}
				
		if (!fields.isEmpty() || !methods.isEmpty()) {
			System.out.print("|");
		}	
	}
}
