import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;


public class ClassInfo {
	ClassNode classNode;
	
	String className;
	String stereotype;
	String extendz;
	String color;
	
	List<String> implementz;
	
	HashMap<String, Integer> fieldAppear;
	HashMap<String, Integer> methodAppear;
	
	Settings settings;
	
	public ClassInfo(ClassNode classNode) {
		this.classNode = classNode;
		this.className = this.classNode.name;
		this.extendz = this.classNode.superName;
		this.implementz = this.getInterfacez();
		this.fieldAppear = new HashMap<>(); 
		this.methodAppear = new HashMap<>();
		this.settings = Settings.getInstance();
		this.color = "black";
		this.populateFieldAppear();
		this.populateMethodAppear();
	}
	
//	private String getClazzName() {
//		// this gets the name of the class.
//		String name = this.classNode.name;
//		System.out.println(name);
//		int length;
//		
//		String[] fields = name.split("/");
//		length = fields.length - 1;
//		
//		return fields[length];
//	}
	
	public String getClassName() {
		return this.className;
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
	private List<String> getInterfacez() {
		List<String> unSplit = this.classNode.interfaces;
        ArrayList<String> split = new ArrayList<>();
		for(String name : unSplit) {
			//int length;
//			System.out.println(name);
//			String[] fields = name.split("/");
//			length = fields.length - 1;
//			
			split.add(name);
		}
		return split;
	}
	
	public List<String> getInterfaces() {
		return this.implementz;
	}
	
//	private String getExtendz() {
//		String superName = this.classNode.superName;
//		int length;
//		
//		String[] path = superName.split("/");
//		length = path.length - 1;
//		
//		return path[length];
//	}
	
	public String getExtends() {
		return this.extendz;
	}
	
	public HashMap<String, Integer> getFieldAppear() {
		return this.fieldAppear;
	}
	
	public HashMap<String, Integer> getMethodAppear() {
		return this.methodAppear;
	}
	
//	public List<String> getInAssoc() {
//		return this.inAssocz;
//	}
	
//	public List<String> getInDepend() {
//		return this.inDependz;
//	}
	
	public String getSterotype() {
		return this.stereotype;
	}
	
	public String getColor() {
		return this.color;
	}
	
	public void setColor(String c) {
		this.color = c;
	}
	
	public void setSterotype(String s) {
		this.stereotype = s;
	}
	
//	public void addInAssoc(String c) {
//		this.inAssocz.add(c);
//	}
	
//	public void addInDepends(String c) {
//		this.inDependz.add(c);
//	}
	
	public boolean isPublic() {
		return (this.classNode.access & Opcodes.ACC_PUBLIC) > 0;
	}
	
	public boolean isInterface() {
		return (this.classNode.access & Opcodes.ACC_INTERFACE) > 0;
	}
	
	public boolean isAbsract() {
		return (this.classNode.access & Opcodes.ACC_ABSTRACT) > 0;
	}
	
	public boolean hasFields() {
		if (this.fieldAppear.isEmpty()) {
			return false;
		} else {
			return true;
		}
	}
	
	private void populateFieldAppear() {
		List<FieldNode> fs = this.getFields();
		
		for (FieldNode f : fs) {
			if (f.signature != null) {
				String colEleType = f.signature;
//				System.out.println(colEleType);

				// get rid of carrots
				String[] temp = colEleType.split("<");
				String temp1 = temp[1];
				
				//get rid of the L in front of the object
				String nameJunk = temp1.substring(1);
				
				//get rid of the junk at the end
				String[] nameSplit = nameJunk.split(";");
				
				//get the actual name
				String name = nameSplit[0];
//				System.out.println(name);
				this.fieldAppear.put(name, 2);
				
			} else if (f.desc.charAt(0) == '['){
				String colEleType = f.desc;
				
				String name = colEleType;
				if (!this.settings.isPrimVal(colEleType)) {
					name = colEleType.substring(2, colEleType.length() - 1);
				}
				
				this.fieldAppear.put(name, 2);
			} else { // if not a collection
				String type = f.desc;
//				System.out.println(type + " type");
				
				String name = type;
				if (!this.settings.isPrimVal(type)) {
					name = type.substring(1, type.length() - 1);
				}
//				System.out.println(name + " type");
				if(this.fieldAppear.containsKey(name)) {
					int appear = this.fieldAppear.get(name);
					this.fieldAppear.put(name, appear++);
				} else {
					this.fieldAppear.put(name, 1);
				}
			}
		}
		
//		for (Map.Entry<String, Integer> entry : this.fieldAppear.entrySet()) {
//			System.out.println("key: " + entry.getKey() + " val: " + entry.getValue());
//		}
		
	}
	
	private void populateMethodAppear() {
		List<MethodNode> methods = this.getMethods();
		// the return and parameter types of methods of a class
		ArrayList<String> methodTypes = new ArrayList<>();
		
		//getting the parameters of each of the methods
		for (MethodNode m: methods) {

			String type = m.desc;
			//System.out.println(type);
				
			Type[] methodType = Type.getArgumentTypes(type);
				
			for (Type t: methodType) {
				
				String temp = t.getDescriptor();
				String name = temp;
				if(!this.settings.isPrimVal(temp)) {
					//System.out.println(temp);
					if(temp.charAt(0) == '[') {
						name = temp.substring(2, temp.length() - 1);
					} else if(temp.charAt(0) == 'L') {
						name = temp.substring(1, temp.length() - 1);
					}
					methodTypes.add(name);
				}
			}
			
						
			for (String m1 : methodTypes) {
				if(!settings.isPrimVal(m1)) {
					if (this.methodAppear.containsKey(m1)) {
						int appear = this.methodAppear.get(m1);
						this.methodAppear.put(m1, appear++);
					} else {
						this.methodAppear.put(m1, 1);
					}
				}
			}
		}
	}
}
