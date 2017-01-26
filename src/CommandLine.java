import java.util.ArrayList;
import java.util.HashMap;

public class CommandLine implements ISubject {

	HashMap<String, ICommand> commands;
	
	public CommandLine() {
		this.commands = new HashMap<>();
	}
	
	public CommandLine(String name, ICommand c) {
		this.commands = new HashMap<>();
		this.commands.put(name, c);
	}
	
	public CommandLine(HashMap<String, ICommand> c) {
		this.commands = c;
	}
	
	public void notify(String[] classes, ICommand c) {
		c.update(classes);
	}

	public void addCommand(String name, ICommand c) {
		this.commands.put(name, c);
	}

	public void removeCommand(String name) {
		this.commands.remove(name);
	}
	
	public void run(String[] args) {
		IParser p = new DesignParser();
		ICommand genDiagram = new UMLgen(p);
		Settings settings = Settings.getInstance();
		ArrayList<String> tempArgs = new ArrayList<>();
		
		for (int i = 0; i < args.length; i++) { //flags must be initialized before java files
			String s = args[i];
			
			if (s.charAt(0) != '-') {
				tempArgs.add(args[i]);
			}
			
			String firstTwo = s.substring(0, 2);
			
			if (firstTwo.equals("-r")) {
				settings.setRecursive();
			} else {
				settings.addDetectors(s);
			}			
		}
		
		String[] classes = new String[tempArgs.size()];
		
		classes = tempArgs.toArray(classes);

		this.notify(classes, genDiagram);		
	}
}
