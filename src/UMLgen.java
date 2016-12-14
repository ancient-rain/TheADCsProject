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
		// TODO Auto-generated method stub
		System.out.println("In UMLgen");
			//non recursive UML diagram.
			for (int i = 0; i < classes.length; i++ ) {
				ClassNode c = this.parser.parse(classes[i]);
				ClassInfo ci = new ClassInfo(c);
				this.classList.add(ci);
			}
			System.out.println("after loop before DisplayGraph()");
			displayGraph();
		
		System.out.println("exiting UMLgen");
	}

	private void displayGraph() {
		// TODO Auto-generated method stub
		System.out.println("in DisplayGraph()");
		gv.display(this.classList);
	}

}
