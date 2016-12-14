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
		// TODO Auto-generated method stub
		System.out.println("leaving CommandLine");
		c.update(classes);
	}

	public void addCommand(String name, ICommand c) {
		this.commands.put(name, c);
	}

	public void removeCommand(String name) {
		this.commands.remove(name);
	}
	
	public void run(String[] args) {
		System.out.println("Starting commandLine");
		
//		for(int i = 0; i < args.length; i++) {
//			if(this.commands.containsKey(args[0])) {
//				System.out.println("command exists!");
//				ICommand c = this.commands.get(fields[0]);
//				Event e = new Event(fields);
//				this.notify(e, c);
//			} else {
//				System.out.println("Command not recognized!");
//			}
//		}
		
		IParser p = new DesignParser();
		ICommand genDiagram = new UMLgen(p);
		
		this.notify(args, genDiagram);
		
	}
}
