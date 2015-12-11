package tests.sources;
public class TestVMThread {

	private class VM {
	}

	public VM createVM() {
		return new VM();
	}
	
	public void releaseVM(VM x) {	}

	private class T extends Thread {
		
		private VM alpha;

		public T(VM a) {
			alpha = a;
		}
		
		public void run() {
			VM b = createVM();
			synchronized (alpha) {
				releaseVM(alpha);
				alpha = createVM();
			}
			releaseVM(b);
		}
		
	}
	
	public void main(String[] args) throws InterruptedException {
	
		VM a = createVM();
		Thread t = new T(a);
		t.run();
		VM c = createVM();
		synchronized (a) {
			releaseVM(a);
		}
		t.join();
		releaseVM(c);
		releaseVM(a);
	
	}

}
