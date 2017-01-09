import java.util.List;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.FieldNode;

public class GVField {
	
	public GVField() {
		
	}
	
	public void printFields(List<FieldNode> fields) {
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
	}
}
