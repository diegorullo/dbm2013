package utils;

import static org.junit.Assert.*;

import matlabcontrol.MatlabConnectionException;
import matlabcontrol.MatlabInvocationException;
import matlabcontrol.MatlabProxy;
import matlabcontrol.MatlabProxyFactory;

import org.junit.Test;

public class MatlabEngineTest {

	@Test
	public void initTest() throws MatlabConnectionException, MatlabInvocationException {
		MatlabEngine me = new MatlabEngine();
			me.init();
	}
}
