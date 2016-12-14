import java.util.ArrayList;
import org.objectweb.asm.tree.ClassNode;

public class GraphViz {
	GVdisplayer displayer;
	
	public GraphViz() {
		this.displayer = null;
	}
	
	public GraphViz(GVdisplayer d) {
		this.displayer = d;
	}
	
	public void display(ArrayList<ClassInfo> classes) {
		System.out.println("in GraphViz display");
		for(ClassInfo c : classes) {
			System.out.println(c.getClassName());
			System.out.println(c.getFields());
			System.out.println(c.getMethods());
			System.out.println(c.getInterfaces());
			System.out.println(c.getExtends());
			System.out.println(c.getAccess() + "\n");
		}
	}
	
}
