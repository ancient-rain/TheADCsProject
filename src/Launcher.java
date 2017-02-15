import java.io.*;

public class Launcher {
	
	public static void main(String [] args) throws IOException, InterruptedException {
		CommandLine cl = new CommandLine();
		
		cl.addDetector("-gs", GetSetDetector.class);
		cl.run(args);
		
		return;
	}
}
