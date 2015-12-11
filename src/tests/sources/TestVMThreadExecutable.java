package tests.sources;
public class TestVMThreadExecutable {

	private class VM {
		public int id;
		public VM(int i) {
			id = i;
			System.out.println("created VM " + id);
		}
	}

	public VM createVM() {
		return new VM((int) (Math.random()*100));
	}
	
	public void releaseVM(VM x) {
		System.out.println("released VM " + x.id);
	}

	private class T extends Thread {
		
		private VM alpha;

		public T(VM a) {
			alpha = a;
		}
		
		public void run() {
			VM b = createVM();
			try {
				sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			synchronized (alpha) {
				releaseVM(alpha);
				alpha = createVM();
				System.out.println("new alpha id is " + alpha.id);
			}
			releaseVM(b);
		}
		
	}
	
	public void main(String[] args) throws InterruptedException {
	
		VM a = createVM();
		Thread t = new T(a);
		t.run();
		try {
			int x = 1 / 0;
		} catch (Exception e) {
			
		}
		VM c = createVM();
		synchronized (a) {
			releaseVM(a);
		}
		t.join();
		System.out.println("alpha's id is " + a.id);
		releaseVM(c);
//		releaseVM(a);
	
	}

}