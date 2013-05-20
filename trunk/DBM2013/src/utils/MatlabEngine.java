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
		factory = new MatlabProxyFactory();
		proxy = factory.getProxy();
	}	
	
	/**
	 * Fa calcolare da MATLAB la funzione il cui nome e' passato come parametro
	 * (E' possibile vedere quali funzioni sono invocabili analizzando il contenuto
	 * della cartella "ext-matlab")
	 * 
	 * @param functionName nome della funzione da invocare
	 * @throws MatlabConnectionException 
	 * @throws MatlabInvocationException 
	*/
	public void eval(String functionName) throws MatlabConnectionException, MatlabInvocationException {		
		proxy.feval(functionName);
	}
		
	/**
	 * disconnette il proxy da Matlab
	 */
	public void shutdown() 
	{
		proxy.disconnect();
	}
}