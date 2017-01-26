import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;


public class ClassInfo {
	ClassNode classNode;
	
	String className;
	String sterotype;
	String extendz;
	
	List<String> implementz;
//	List<String> inDependz;
//	List<String> inAssocz;
	
	HashMap<String, Integer> fieldAppear;
	HashMap<String, Integer> methodAppear;
	
	Settings settings;
	
	public ClassInfo(ClassNode classNode) {
		this.classNode = classNode;
		this.className = this.getClazzName();
		this.extendz = this.getExtendz(); 
		this.implementz = this.getInterfacez();
		this.fieldAppear = new HashMap<>(); 
		this.methodAppear = new HashMap<>();
//		this.inDependz = new ArrayList<>();
//		this.inAssocz = new ArrayList<>();
		this.settings = Settings.getInstance();
		this.populateFieldAppear();
		this.populateMethodAppear();
	}
	
	private String getClazzName() {
		// this gets the name of the class.
		String name = this.classNode.name;
		int length;
		
		String[] fields = name.split("/");
		length = fields.length - 1;
		
		return fields[length];
	}
	
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
			int length;
			
			String[] fields = name.split("/");
			length = fields.length - 1;
			
			split.add(fields[length]);
		}
		return split;
	}
	
	public List<String> getInterfaces() {
		return this.implementz;
	}
	
	private String getExtendz() {
		String superName = this.classNode.superName;
		int length;
		
		String[] path = superName.split("/");
		length = path.length - 1;
		
		return path[length];
	}
	
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
		return this.sterotype;
	}
	
	public void setSterotype(String s) {
		this.sterotype = s;
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
				String[] path = colEleType.split("/");
				int lastIndex = path.length - 1;
				colEleType = path[lastIndex];
				String[] path2 = colEleType.split(";");
				colEleType = path2[0];
				this.fieldAppear.put(colEleType, 2);
				
			} else if (f.desc.charAt(0) == '['){
				String colEleType = f.desc;
				String[] path = colEleType.split("/");
				int lastIndex = path.length - 1;
				colEleType = path[lastIndex];
				String[] path2 = colEleType.split(";");
				colEleType = path2[0];
				this.fieldAppear.put(colEleType, 2);
			} else { // if not a collection
				String type = f.desc;
				String[] path = type.split("/");
				int lastIndex = path.length - 1;
				
				int len = path[lastIndex].length();
				if (len == 1) {
					type = path[lastIndex];
				} else {
					type = path[lastIndex].substring(0, len - 1);
				}

				if(this.fieldAppear.containsKey(type)) {
					int appear = this.fieldAppear.get(type);
					this.fieldAppear.put(type, appear++);
				} else {
					this.fieldAppear.put(type, 1);
				}
			}
		}
	}
	
	private void populateMethodAppear() {
		List<MethodNode> methods = this.getMethods();
		// the return and parameter types of methods of a class
		ArrayList<String> methodTypes = new ArrayList<>();
		
		//getting the parameters of each of the methods
		for (MethodNode m: methods) {
			if (m.signature != null) {
				String type = m.signature;
				String[] path = type.split("\\)");
				String params = path[0];
				String [] pathParam = params.split("\\(");
				
				if (pathParam.length != 0) {
					int lastIndex = pathParam.length - 1;
					params = pathParam[lastIndex];
					pathParam = params.split("<");
					
					for (String s : pathParam) {
						if (s.contains("/")) {
							String [] p = s.split("/");
							int len = p.length - 1;
							String current = p[len];
							
							if (!this.settings.isPrimVal(current)) {
								methodTypes.add(current);
							}
						}	
					}
				}
				
				//checking for the return type
				String returnType = path[1];
				
				if (returnType.length() != 0 && !this.settings.isPrimVal(returnType)) {
					String [] returnPath = returnType.split("<");
					String returnVal = returnPath[0];
					
					if (returnVal.contains("/")) {
						returnPath = returnVal.split("/");
						int lastIndex = returnPath.length - 1;
						String current = returnPath[lastIndex];
						
						if (!this.settings.isPrimVal(current)) {
							//System.out.println(current);
							methodTypes.add(current);
						}
					}
				}
			} else {
				String type = m.desc;
				String[] path = type.split("\\)");
				String params = path[0];
				String [] pathParam = params.split("\\(");
				
				//if the parameter is not null
				if (pathParam.length != 0) {
					int lastIndex = pathParam.length - 1;
					params = pathParam[lastIndex];
					pathParam = params.split(";");

					for (String s : pathParam) {
						if (s.contains("/")) {
							String [] p = s.split("/");
							int len = p.length - 1;
							String current = p[len];
							
							if (!this.settings.isPrimVal(current)) {
								methodTypes.add(current);
							}
						}
					}
				}
				
				//checking for the return type
				String returnType = path[1];
				
				if (returnType.length() != 0 && !this.settings.isPrimVal(returnType)) {
					String [] returnPath = returnType.split("/");
					int lastIndex = returnPath.length - 1;
					String returnVal = returnPath[lastIndex];
					returnPath = returnVal.split(";");
					String current = returnPath[0];
					
					if (!this.settings.isPrimVal(current)) {
						methodTypes.add(current);
					}
				}
			}
		}
		
		for (String m : methodTypes) {
			if (this.methodAppear.containsKey(m)) {
				int appear = this.methodAppear.get(m);
				this.methodAppear.put(m, appear++);
			} else {
				this.methodAppear.put(m, 1);
			}
		}
	}	
}
