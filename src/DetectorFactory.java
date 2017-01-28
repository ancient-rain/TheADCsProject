import java.util.ArrayList;

public class DetectorFactory {
	
	Graph graph;
	
	public DetectorFactory(Graph g) {
		this.graph = g;
	}
	
	public ArrayList<IDetector> build(Graph g, ArrayList<String> detectors) {
		ArrayList<IDetector> detects = new ArrayList<>();
		
		for (String s: detectors) {
			if (s.equals("-coi")) {
				IDetector d = new CoIDetector(this.graph);
				detects.add(d);
			}
		}
		
		return detects;
	}
}
