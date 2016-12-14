

import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

public class ClassInfo {
	ClassNode classNode;
	
	public ClassInfo(ClassNode classNode) {
		this.classNode = classNode;
	}
	
	
	public String getClassName() {
		return this.classNode.name;
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<String> getMethods() {
		List<MethodNode> methods = (List<MethodNode>) this.classNode.methods;
		ArrayList<String> arr = new ArrayList<>();
		for (MethodNode method : methods){
			arr.add(method.name);
		}
		return arr;
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<String> getFields() {
		List<FieldNode> fields = (List<FieldNode>) this.classNode.fields;
		ArrayList<String> arr = new ArrayList<>();
		for (FieldNode field : fields){
			arr.add(field.name);
		}
		return arr;
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getInterfaces() {
		return this.classNode.interfaces;
	}
	
	public String getExtends() {
		return this.classNode.superName;
	}
	
	public boolean getAccess() {
		return (this.classNode.access & Opcodes.ACC_PUBLIC) > 0;
	}
}
