package tool.model;

public class NonPresburgerExpression implements ExtendedNatural {

	@Override
	public String getValue() {
		return "-";
	}

	@Override
	public void add(int value) {
	}

	@Override
	public ExtendedNatural greatestLowerBound(ExtendedNatural n) {
		return this;
	}

}
