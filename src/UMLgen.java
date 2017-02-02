import java.util.ArrayList;
import org.objectweb.asm.tree.ClassNode;

public class UMLgen implements ICommand {

	IParser parser;
	ArrayList<ClassInfo> classList;
	GVManager gv;
	
	public UMLgen(IParser p) {
		this.parser = p;
		this.classList = new ArrayList<>();
	}
	
	public void update(String[] classes) {
		for (int i = 0; i < classes.length; i++ ) {
			ClassNode c = this.parser.parse(classes[i]);
			ClassInfo ci = new ClassInfo(c);
			this.classList.add(ci);
		}
		Graph graph = new Graph(this.classList);
		Settings settings = Settings.getInstance();
		settings.addGraph(graph);
		this.gv = new GVManager(graph);
		displayGraph();	
	}

	private void displayGraph() {
		gv.displayGVCode(this.classList);
	}

}
