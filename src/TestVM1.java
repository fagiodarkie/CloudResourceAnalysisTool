public class TestVM1 {

	private class VM {
	}

	public VM createVM() {
		return new VM();
	}
	
	public void releaseVM(VM x) {
	
	}

	public void main(String[] args) {
	
		int n = 3;
		VM a = createVM();
		while (n != 0) {
		
			releaseVM(a);
			VM b = createVM();
			a = createVM();
			releaseVM(b);
						
			n--;
		
		}
	
	}

}