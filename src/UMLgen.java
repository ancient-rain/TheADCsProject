import java.util.ArrayList;
import org.objectweb.asm.tree.ClassNode;

public class UMLgen implements ICommand {

	IParser parser;
	ArrayList<ClassInfo> classList;
	GraphViz gv;
	
	
	public UMLgen(IParser p) {
		this.parser = p;
		this.classList = new ArrayList<>();
		
		this.gv = new GraphViz();
	}
	
	public void update(String[] classes) {
		for (int i = 0; i < classes.length; i++ ) {
			ClassNode c = this.parser.parse(classes[i]);
			ClassInfo ci = new ClassInfo(c);
			this.classList.add(ci);
		}
		displayGraph();	
	}

	private void displayGraph() {
		gv.displayGVCode(this.classList);
	}

}
