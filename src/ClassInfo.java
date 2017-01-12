
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
	ArrayList<ClassInfo> outward;
	ArrayList<ClassInfo> inward;
	
	
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
	
	public String getFullClassName() {
		String name = this.classNode.name;
		int length;
		
		String[] fields = name.split("/");
		
		String fullName = "";
		for(int i = 0; i < fields.length; i++) {
			if (i + 1 == fields.length) {
				fullName.concat(fields[i]);
				break;
			}
			fullName.concat(fields[i] + ".");
		}
		return fullName;
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


	public void addOutward(ClassInfo v) {
		this.outward.add(v);
	}


	public void addInward(ClassInfo v) {
		this.inward.add(v); 
	}


	public ArrayList<ClassInfo> getInward() {
		return this.inward;
	}


	public ArrayList<ClassInfo> getOutward() {
		return this.outward;
	}
	
	
	
}
