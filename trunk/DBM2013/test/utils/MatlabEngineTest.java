package utils;

import matlabcontrol.MatlabConnectionException;
import matlabcontrol.MatlabInvocationException;

import org.junit.Test;

public class MatlabEngineTest {

	@Test
	public void initTest() throws MatlabConnectionException, MatlabInvocationException {
		MatlabEngine me = new MatlabEngine();
			me.init();
			
			me.eval("svd_IR");
			me.eval("pca_IR");
	}
}
