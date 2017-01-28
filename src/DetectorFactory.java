import java.util.HashMap;

public class DetectorFactory {

	HashMap<String, IDetector> detectors;
	
	public DetectorFactory(Graph g) {
		this.detectors = new HashMap<String, IDetector>();
//		this.detectors.put("-coi", new CoIDetector(g));
	}
	
	public void build() {
		
	}
}
