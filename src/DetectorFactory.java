import java.util.HashMap;

public class DetectorFactory {

	HashMap<String, IDetector> detectors;
	
	public DetectorFactory(Graph g) {
		this.detectors = new HashMap<String, IDetector>();
//		this.detectors.put("-coi", new CoIDetector(g));
	}
	
	public void build() {
		
		ArrayList<IDetector> d = new ArrayList<>();
		
		for (String s: detectors) {
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
