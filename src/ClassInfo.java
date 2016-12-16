
import java.util.List;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

public class ClassInfo {
	ClassNode classNode;
	
	public ClassInfo(ClassNode classNode) {
		this.classNode = classNode;
	}
	
	
	public String getClassName() {
		String name = this.classNode.name;
		int length;
		
		String[] fields = name.split("/");
		length = fields.length - 1;
		
		return fields[length];
	}
	
	@SuppressWarnings("unchecked")
	public List<MethodNode> getMethods() {
		return (List<MethodNode>) this.classNode.methods;
	}
	
	@SuppressWarnings("unchecked")
	public List<FieldNode> getFields() {
		return (List<FieldNode>) this.classNode.fields;
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getInterfaces() {
		
		return this.classNode.interfaces;
	}
	
	public String getExtends() {
		String superName = this.classNode.superName;
		int length;
		
		String[] path = superName.split("/");
		length = path.length - 1;
		
		return path[length];
	}
	
	public boolean isPublic() {
		return (this.classNode.access & Opcodes.ACC_PUBLIC) > 0;
	}
	
	public boolean isInterface() {
		return (this.classNode.access & Opcodes.ACC_INTERFACE) > 0;
	}
}
