package utils;

import java.io.File;

import matlabcontrol.*;

public class MatlabEngine {

	private static MatlabProxyFactoryOptions options;
	private static MatlabProxyFactory factory;
	private static MatlabProxy proxy;

	private static MatlabEngine mle;

	/**
	 * Crea un proxy, che useremo per controllare MATLAB e ne imposta il path
	 * 
	 * @throws MatlabConnectionException
	 * @throws MatlabInvocationException
	 */
	public void init() throws MatlabConnectionException, MatlabInvocationException {
		if (options == null) {
			String user_dir = System.getProperty("user.dir");
			File matlabStartingDirectory = new File(user_dir);
			//System.out.println("user_dir: " + user_dir);
			options = new MatlabProxyFactoryOptions.Builder().
					setHidden(true).
					setMatlabStartingDirectory(matlabStartingDirectory).
					setUsePreviouslyControlledSession(true).
					build();
		}
		if (factory == null) {
//			factory = new MatlabProxyFactory(options);
			factory = new MatlabProxyFactory();
			if (proxy == null || !proxy.isConnected()) {
				proxy = factory.getProxy();
			}
		}
	}

	/**
	 * disconnette il proxy da Matlab
	 * 
	 * @throws MatlabInvocationException
	 */
	public void shutdown() throws MatlabInvocationException {
		proxy.exit();
		proxy.disconnect();
	}
	
	private MatlabEngine() throws MatlabConnectionException, MatlabInvocationException {	
		mle = this;
		mle.init();
	}
	
	protected void finalize() throws MatlabInvocationException {
		mle.shutdown();
	}
	
	public static MatlabEngine getMatlabEngine() throws MatlabConnectionException, MatlabInvocationException {
		if(mle == null) {
				mle = new MatlabEngine();
		}
		return mle;
	}

	/**
	 * Fa calcolare da MATLAB la funzione il cui nome e' passato come parametro
	 * (E' possibile vedere quali funzioni sono invocabili analizzando il
	 * contenuto della cartella "ext-matlab")
	 * 
	 * @param functionName
	 *            nome della funzione da invocare
	 * @param fileName
	 *            nome del file da elaborare (lettura/scrittura)
	 * @throws MatlabConnectionException
	 * @throws MatlabInvocationException
	 */
	public void eval(String functionName, String fileName)
			throws MatlabConnectionException, MatlabInvocationException {
		proxy.feval(functionName, fileName);
	}

}