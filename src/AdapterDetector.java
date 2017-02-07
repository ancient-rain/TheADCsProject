
public class AdapterDetector implements IDetector {
	
	Graph graph;
	Settings settings;
	
	public AdapterDetector() {
		this.settings = Settings.getInstance();
		this.graph = this.settings.getGraph();
	}

	@Override
	public void detect() {
		
	}

}
