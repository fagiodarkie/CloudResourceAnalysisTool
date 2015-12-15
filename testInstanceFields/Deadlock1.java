// pc1 = monitorenter , pc2 = monitorenter

public class Deadlock1 {

	private Object o = new Object();
	private Object o_primo = new Object();


	public void takelock() throws InterruptedException{
		synchronized (o) {
			synchronized (o_primo) {
			}
		}
	}
	
	public void takelock2() throws InterruptedException{
		synchronized (o_primo) {
			synchronized (o) {
			}
		}
	}

	public static void main(String[]args) throws InterruptedException{
	
		final Deadlock1 d = new Deadlock1();
		
		
		Thread t1 = new Thread(){

			@Override
			public void run() {
				try {
					d.takelock();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		};
		
		t1.start();

		Thread t2 = new Thread() {

			@Override
			public void run() {
				try {
					d.takelock2();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		};
		
		t2.start();
	}
}
