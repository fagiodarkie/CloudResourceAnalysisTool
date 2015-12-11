package tool;

import tests.sources.TestVMThread;
import tests.sources.TestVMThreadExecutable;

public class ASPTool {

	public static void main(String[] args) {
		TestVMThreadExecutable t = new TestVMThreadExecutable();
		try {
			t.main(null);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
