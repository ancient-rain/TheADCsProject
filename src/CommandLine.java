import java.util.ArrayList;
import java.util.HashMap;

public class CommandLine {
		
	public CommandLine() {
		
	}
	
	public void addDetector(String flag,Class<?> detector) {
		Settings settings = Settings.getInstance();
		settings.addDetector(detector, flag);
	}
	
	public void notify(String[] classes, ICommand c) {
		c.update(classes);
	}

	public void run(String[] args) {
		IParser p = new DesignParser();
		ICommand genDiagram = new UMLgen(p);
		Settings settings = Settings.getInstance();
		ArrayList<String> tempArgs = new ArrayList<>();
		
		if (args.length == 0) {
			ArrayList<String> classes = settings.getClasses();
			args = new String[classes.size()];
			for (int i = 0; i < classes.size(); i++) {
				args[i] = classes.get(i);
			}
		}
		boolean flags = true;
		
		for (int i = 0; i < args.length; i++) { //flags must be initialized before java files
			String s = args[i];
			
			if (s.charAt(0) != '-') {
				if(i == 0) {
					flags = false;
				}
				tempArgs.add(args[i]);
			} else if(flags){
				String firstTwo = s.substring(0, 2);
				
				if (firstTwo.equals("-r")) {
					settings.setRecursiveTrue();
				} else if (firstTwo.equals("-s")) {
					settings.setSyntheticTrue();
				} else if (firstTwo.equals("-u")){
					i++;
					settings.setConfig(args[i]);
				} else {
					settings.setCommandFlags();
					settings.addDetectors(s);
				}
			}
		}
		String[] classes = null;
		if (!tempArgs.isEmpty()) {
			classes = new String[tempArgs.size()];
			
			classes = tempArgs.toArray(classes);
		} else {
			ArrayList<String> temp = settings.getClasses();
			classes = new String[temp.size()];
			classes = temp.toArray(classes);
			//System.out.println(temp.get(0) + " " + classes[0]);
		}
		//System.out.println(settings.getRecursive());
		this.notify(classes, genDiagram);		
	}
}
