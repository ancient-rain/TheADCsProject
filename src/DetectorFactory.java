import java.util.ArrayList;
import java.util.HashMap;

public class DetectorFactory {
	Graph graph;
	
	public DetectorFactory(Graph g) {
		this.graph = g;
//		this.detectors.put("-coi", newIDetector(g));
	}
	
	public ArrayList<IDetector> build(ArrayList<String> flags) {
		
		ArrayList<IDetector> d = new ArrayList<>();
		
		for (String s: flags) {
			if(s.equals("-coi")) {
				//System.out.println("building coi detector");
				d.add(new CoIDetector(this.graph));
			} else if(s.equals("-sg")) {
				d.add(new SingletonDetector(this.graph));
			}
		}
		return d;
	}
}