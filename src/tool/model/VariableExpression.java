package tool.model;

public class VariableExpression implements ExtendedNatural {

	private String expression;
	
	public VariableExpression(String exp) {
		expression = exp;
	}
	
	@Override
	public String getValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void add(int value) {
		expression = "(" + expression + ") + " + Integer.toString(value);
	}

	@Override
	public ExtendedNatural greatestLowerBound(ExtendedNatural n) {
		if (n.getClass().equals(Constant.class) || n.getClass().equals(this.getClass())) {
			return this;
		}
		return n;
	}

}
