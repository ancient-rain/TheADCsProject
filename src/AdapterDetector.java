import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;


public class AdapterDetector implements IDetector {
	
	Graph graph;
	Settings settings;
	
	public AdapterDetector() {
		this.settings = Settings.getInstance();
		this.graph = this.settings.getGraph();
	}

	@Override
	public void detect() {
		ArrayList<ClassInfo> classes = this.graph.getClasses();
		HashMap<String, ClassInfo> classNames = this.graph.getGraph();
		
		// look through all the classes in the graph
		for (ClassInfo ci: classes) {
			
			List<MethodNode> methods = ci.getMethods();
			List<FieldNode> fields = ci.getFields();
			
			if ((ci.getDoesImplement() && (ci.getInterfaces().size() < 2)) || !this.settings.isPrimVal(ci.getExtends())) {
			// for each class in the graph look at each of its non-primitive fields
				for(FieldNode f: fields) {
					
					String fieldType = f.desc;
					if(!settings.isPrimVal(fieldType)) {
						fieldType = fieldType.substring(1, fieldType.length() - 1);
						// Only continue if the field is a class in the graph
						if(classNames.containsKey(fieldType)) {
							ClassInfo clazz = classNames.get(fieldType);
							List<MethodNode> methodz = clazz.getMethods();
							HashSet<String>	methodName = new HashSet<>();				
							
							//get the names of all of the methods that the field type has
							for(MethodNode m: methodz) {
								methodName.add(m.name);
							}
							
							// for each of the methods in the class look at the instructions of the methods
							// and determine if those instructions are method calls to methods from the 
							// field contained inside of the class. If all 
							boolean hasAllCalls = true;
							for(MethodNode m: methods) {
								
								boolean hasCall = false;
								InsnList iNode = m.instructions;
								for(int i = 0; i< iNode.size(); i++) {
									
									if(!m.name.equals("<init>")){
										if(iNode.get(i) instanceof MethodInsnNode) {
											
											MethodInsnNode methodCall = (MethodInsnNode) iNode.get(i);
											if(methodName.contains(methodCall.name) && fieldType.equals(methodCall.owner)) {
												hasCall = true;
//												System.out.println(fieldType + ": " + methodCall.owner);
											} else {
//												System.out.println(m.name + ": " + methodCall.name);
												hasAllCalls = false;
											}
										}
									}
								}
								
								if(!hasCall) {
									break;
								}
							}
							if(hasAllCalls) {
								if(!ci.isPattern()) {
									ci.setColor("pink");
									ci.setSterotype("Adapter");
									ci.setIsPattern();
									ci.addPatternArrow(clazz.getClassName());
									ci.setPatternArrow("adapts");
									clazz.setColor("pink");
									clazz.setSterotype("adaptee");
									clazz.setIsPattern();
									List<String> impl = ci.getInterfaces();
									String clazzimpl;
									if(impl.isEmpty()) {
										clazzimpl = ci.getExtends();
									} else {
										clazzimpl = impl.get(0);
									}
									
									if(classNames.containsKey(clazzimpl)) {
										ClassInfo ciI = classNames.get(clazzimpl);
										ciI.setColor("pink");
										ciI.setSterotype("target");
										ciI.setIsPattern();
									}
								}
							}
						}
					}
				}
			}
		}
	}

}
