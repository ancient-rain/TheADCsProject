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
		this.detectors.put("-di", DIDetector.class);
		this.detectors.put("-a", AdapterDetector.class);
		this.detectors.put("-de", DecoratorDetector.class);
	}
	
	public ArrayList<IDetector> build(ArrayList<String> flags) {
		
		ArrayList<IDetector> d = new ArrayList<>();
		
		for (String s: flags) {
			
			if(this.detectors.containsKey(s)) {
				Class<?> c = this.detectors.get(s);
				try {
					d.add((IDetector) c.newInstance());
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
		return d;
	}
}