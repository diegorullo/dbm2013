package utils;

import java.io.IOException;
import java.sql.SQLException;

import org.junit.Test;

import matlabcontrol.MatlabConnectionException;
import matlabcontrol.MatlabInvocationException;

public class GraphEngineTest {

	@Test
	public void testGraphEngine() throws SQLException, MatlabConnectionException,	MatlabInvocationException, IOException {
		
		GraphEngine.initialize();
	}
}
