import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

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
		
		this.notify(args, genDiagram);
		
	}
}
