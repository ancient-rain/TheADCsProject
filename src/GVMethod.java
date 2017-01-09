import java.util.List;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.MethodNode;

public class GVMethod {
	
	public GVMethod() {
		
	}
	
	public void printMethods(List<MethodNode> methods) {
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
	}
}
