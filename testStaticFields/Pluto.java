public class Pluto {

	
	static Pippo p = new Pippo();


	public static void main(String[]args) throws InterruptedException{
		
		
		Thread t1 = new Thread(){

			@Override
			public void run() {
				try {
					Pluto.p.takelock();
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
					Pluto.p.takelock2();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		};
		
		t2.start();
	}
}
