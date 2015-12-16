package tests.sources;


public class TestVMThread2 {

	private class VM {
	}

	public VM createVM() {
		return new VM();
	}
	
	public void releaseVM(VM x) {	}

	private class T extends Thread {
		
		private VM field1;
		private VM field2;

		public T(VM a, VM b) {
			field1 = a;
			field2 = b;
		}
		
		public void run() {
			releaseVM(field2);
		}
		
	}
	
	public void main(String[] args) throws InterruptedException {
	
		VM a = createVM();
		VM b = createVM();
		VM c = createVM();
		Thread t = new T(a, b);
		t.run();
		VM d = createVM();
		releaseVM(a);
		t.join();
		releaseVM(d);
		releaseVM(a);
	
	}

}
