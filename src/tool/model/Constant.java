package tool.model;

public class Constant implements ExtendedNatural {

	private int value;
	
	public Constant(int val) {
		value = val;
	}
	
	public Constant(String val) {
		value = Integer.parseInt(val);
	}
	
	@Override
	public String getValue() {
		return Integer.toString(value);
	}

	@Override
	public void add(int value) {
		this.value += value;
	}

	@Override
	public ExtendedNatural greatestLowerBound(ExtendedNatural n) {
		if (n.getClass().equals(this.getClass())) {
			return this;
		}
		return n;
	}
	
	

}
