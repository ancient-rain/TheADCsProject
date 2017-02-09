import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

public class DecoratorDetector implements IDetector {

	Graph graph;
	Settings settings;
	
	public DecoratorDetector() {
		this.settings = Settings.getInstance();
		this.graph = this.settings.getGraph();
	}
	
	@Override
	public void detect() {
		ArrayList<ClassInfo> classes = this.graph.getClasses();
		HashMap<String, ClassInfo> classNames = this.graph.getGraph();
		
		for (ClassInfo ci: classes) {
			//System.out.println(ci.className);
			if(!settings.isPrimVal(ci.getExtends()) || ci.getDoesImplement()) {
				if(!ci.getInterfaces().isEmpty()) {
					List<String> interfaces =  ci.getInterfaces();
					//System.out.println("interfaces is not empty");
					for(String inter: interfaces) {
						String superType = inter;
						if(!this.settings.isPrimVal(superType)) {
							//System.out.println("interface is not a prim val");
							
							List<FieldNode> fields = ci.getFields();
							//System.out.println(fields.size());
							for (FieldNode f: fields) {
								
								String field = f.desc;
								if(!settings.isPrimVal(field)) {
									//System.out.println("field is not a prim val");
									if (superType.equals(field)) {
										//System.out.println("the field and the interface are the same");
										List<MethodNode> methods = ci.getMethods();
										
										for(MethodNode method: methods) {
											
											InsnList iNode = method.instructions;
											for(int i = 0; i< iNode.size(); i++) {						
											
												if(iNode.get(i) instanceof MethodInsnNode) {
													MethodInsnNode methodCall = (MethodInsnNode) iNode.get(i);
													System.out.println(methodCall.name);
													if(method.name.equals(methodCall.name) && superType.equals(methodCall.owner)) {
														ci.setColor("green");
														ci.setSterotype("decorator");
														ci.setIsPattern();
														ci.addPatternArrow(superType);
														ci.setPatternArrow("decorates");
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
				//System.out.println("checking extends");
				String superType = ci.getExtends();
				if(!this.settings.isPrimVal(superType)) {
					//System.out.println("super is not Object");
					List<FieldNode> fields = ci.getFields();
					//System.out.println("there are " + fields.size() + " fields");
					for (FieldNode f: fields) {
						
						String field = f.desc;
						if(!settings.isPrimVal(field)) {
							//System.out.println("field is not a prim val");
							
							field = field.substring(1, field.length() - 1);
							
							if (superType.equals(field)) {
								//System.out.println("super and field match");
								List<MethodNode> methods = ci.getMethods();
								
								boolean hasAll = true;
								for(MethodNode method: methods) {
									
									boolean hasCall = false;
									InsnList iNode = method.instructions;
									for(int i = 0; i< iNode.size(); i++) {						
											
										if(!method.name.equals("<init>")){
											
											if(iNode.get(i) instanceof MethodInsnNode) {
												MethodInsnNode methodCall = (MethodInsnNode) iNode.get(i);
												//System.out.println(methodCall.name);
											
												if(method.name.equals(methodCall.name) && superType.equals(methodCall.owner)) {
	//												System.out.println("setting colors");
													hasCall = true;
														
												} else {
													hasAll = false;
												}
											}
										}
									}
									if(!hasCall) {
										break;
									}
								}
								
								if(hasAll) {
									if (!ci.isPattern()) {
										ci.setColor("green");
										ci.setSterotype("decorator");
										ci.setIsPattern();
										ci.addPatternArrow(superType);
										ci.setPatternArrow("decorates");
										
										ClassInfo clazz = classNames.get(superType);
										clazz.setColor("green");
										clazz.setSterotype("component");
										clazz.setIsPattern();
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
