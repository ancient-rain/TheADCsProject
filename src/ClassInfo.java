
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.Type;

public class ClassInfo {
	ClassNode classNode;
	
	public ClassInfo(ClassNode classNode) {
		this.classNode = classNode;
	}
	
	
	public String getClassName() {
		// this gets the name of the class.
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
	
	public HashSet<Type> getAssocTypes() {
		List<FieldNode> fields = this.getFields();
		List<Type> types = new ArrayList<>();
		
		for (FieldNode f : fields) {
			types.add(Type.getType(f.desc));
		}
		
		return new HashSet<Type>(types);
	}
	
	public HashSet<Type> getDependencyTypes() {
		List<MethodNode> methods = this.getMethods();
		List<Type> types = new ArrayList<>();
		
		for (MethodNode m : methods) {
			Type.getReturnType(m.desc);
		}
		
		return new HashSet<Type>(types);
	}
}
