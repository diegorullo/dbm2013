package utils;

import matlabcontrol.*;

public class MatlabEngine 
{
	
	private MatlabProxyFactory factory;
	private MatlabProxy proxy;

	/**
	 * Crea un proxy, che useremo per controllare MATLAB e ne imposta il path
	 * 
	 * @throws MatlabConnectionException 
	 * @throws MatlabInvocationException 
	*/
	public void init() throws MatlabConnectionException, MatlabInvocationException
	{
		if (factory == null) {
			factory = new MatlabProxyFactory();
			if (proxy == null || !proxy.isConnected()) {
				proxy = factory.getProxy();
			}
		}
	}	
	
	/**
	 * Fa calcolare da MATLAB la funzione il cui nome e' passato come parametro
	 * (E' possibile vedere quali funzioni sono invocabili analizzando il contenuto
	 * della cartella "ext-matlab")
	 * 
	 * @param functionName nome della funzione da invocare
	 * @param fileName nome del file da elaborare (lettura/scrittura)
	 * @throws MatlabConnectionException 
	 * @throws MatlabInvocationException 
	*/
	public void eval(String functionName,String fileName) throws MatlabConnectionException, MatlabInvocationException {		
		proxy.feval(functionName,fileName);
	}
		
	/**
	 * disconnette il proxy da Matlab
	 * @throws MatlabInvocationException 
	 */
	public void shutdown() throws MatlabInvocationException 
	{
		proxy.exit();
		proxy.disconnect();
	}
}