import java.util.ArrayList;
import java.util.HashMap;

public class DetectorFactory {
	Graph graph;
	HashMap<String, Class<?>> detectors;
	
	public DetectorFactory(Graph g) {
		this.graph = g;
		Settings settings = Settings.getInstance();
		this.detectors = settings.getDetectors();
		
		this.detectors.put("-coi", CoIDetector.class);
		this.detectors.put("-sg", SingletonDetector.class);
		this.detectors.put("-p", PrintDetector.class);
	
	}
	
	public ArrayList<IDetector> build(ArrayList<String> flags) {
		
		ArrayList<IDetector> d = new ArrayList<>();
		
		for (String s: flags) {
			
			if(this.detectors.containsKey(s)) {
				Class<?> c = this.detectors.get(s);
				try {
					d.add((IDetector) c.newInstance());
				} catch (InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			
			
//			if(s.equals("-coi")) {
//				//System.out.println("building coi detector");
//				d.add(new CoIDetector(this.graph));
//			} else if(s.equals("-sg")) {
//				d.add(new SingletonDetector(this.graph));
//			}
		}
		return d;
	}
}