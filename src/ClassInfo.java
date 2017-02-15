import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Stack;

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
	String patternArrow;
	
	List<String> implementz;
	
	HashSet<String> patternArrows;
	
	HashMap<String, Integer> fieldAppear;
	HashMap<String, Integer> methodAppear;
	
	Settings settings;

	private boolean doesImplement = false;
	private boolean isInterface = false;
	private boolean isPattern = false;
	
	public ClassInfo(ClassNode classNode) {
		this.classNode = classNode;
		this.className = this.classNode.name;
		this.extendz = this.classNode.superName;
		this.implementz = this.getInterfacez();
		this.patternArrows = new HashSet<>();
		this.fieldAppear = new HashMap<>(); 
		this.methodAppear = new HashMap<>();
		this.settings = Settings.getInstance();
		this.color = "black";
		
		this.isInterface = (this.classNode.access & Opcodes.ACC_INTERFACE) > 0;
		this.populateFieldAppear();
		this.populateMethodAppear();
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
			split.add(name);
		}
		if(split.size() > 0) {
			this.doesImplement = true;
		}
		return split;
	}
	
	public List<String> getInterfaces() {
		return this.implementz;
	}
	
	public boolean getDoesImplement() {
		return this.doesImplement;
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
	
	public String getSterotype() {
		return this.stereotype;
	}
	
	public String getColor() {
		return this.color;
	}
	
	public HashSet<String> getPatternArrows() {
		return this.patternArrows;
	}
	
	public String getPatternArrow() {
		return this.patternArrow;
	}
	
	public String setPatternArrow(String pattern) {
		return this.patternArrow = pattern;
	}
	
	public void setColor(String c) {
		this.color = c;
	}
	
	public void setSterotype(String s) {
		this.stereotype = s;
	}
	
	public void setIsPattern() {
		this.isPattern = true;
	}
	
	public boolean isPublic() {
		return (this.classNode.access & Opcodes.ACC_PUBLIC) > 0;
	}
	
	public boolean isInterface() {
		return this.isInterface;
	}
	
	public boolean isAbsract() {
		return (this.classNode.access & Opcodes.ACC_ABSTRACT) > 0;
	}
	
	public boolean isPattern() {
		return this.isPattern;
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
				
				Stack<String> type = new Stack<>();
				char[] chars = colEleType.toCharArray();
				int lastIndex = 0;
				for (int i = 0; i < chars.length; i++) {
					if(chars[i] == '<') {
						type.push(colEleType.substring(lastIndex, i));
						lastIndex = i;
					} else if (chars[i] == '>'){
						type.push(colEleType.substring(lastIndex, i));
						break;	
					}
				}
				
				if(!type.isEmpty()) {
					String actualType = type.pop();
					if(actualType.charAt(0) == 'L') {
						actualType = actualType.substring(1);
						String[] types = actualType.split(";");
						for(int i = 0; i < types.length; i++) {
							if(!settings.isPrimVal(types[i])) {
								System.out.println(types[i]);
								this.fieldAppear.put(types[i], 2);
							}
						}
					}
				}
			} else if (f.desc.charAt(0) == '['){
				String colEleType = f.desc;
				
				String name = colEleType;
				if (!this.settings.isPrimVal(colEleType)) {
					name = colEleType.substring(2, colEleType.length() - 1);
				}
				
				this.fieldAppear.put(name, 2);
			} else { // if not a collection
				String type = f.desc;
				
				String name = type;
				if (!this.settings.isPrimVal(type)) {
					name = type.substring(1, type.length() - 1);
				}
				if(this.fieldAppear.containsKey(name)) {
					int appear = this.fieldAppear.get(name);
					this.fieldAppear.put(name, appear++);
				} else {
					this.fieldAppear.put(name, 1);
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

	public void addPatternArrow(String name) {
		this.patternArrows.add(name);
	}
}
