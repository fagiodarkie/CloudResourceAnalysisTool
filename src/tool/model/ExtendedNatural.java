package tool.model;

public interface ExtendedNatural extends Datatype {

	public String getValue();
	
	public void add(int value);
	
	public ExtendedNatural greatestLowerBound(ExtendedNatural n);
	
}
