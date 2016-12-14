
public interface ISubject {
	void notify(String[] classes, ICommand c);
	void addCommand(String name,ICommand c);
	void removeCommand(String name);
}
