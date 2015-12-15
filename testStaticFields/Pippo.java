public class Pippo {
		
	private Object a = new Object();
	private Object b = new Object();
	
	public void takelock() throws InterruptedException{
		synchronized (a) {
			synchronized (b) {
			}
		}
	}
	
	public void takelock2() throws InterruptedException{
		synchronized (b) {
			synchronized (a) {
			}
		}
	}
}
