package utils;

import matlabcontrol.*;

public class MatlabEngine 
{
	
	private MatlabProxyFactory factory;
	private MatlabProxy proxy;

	/**
	 * Crea un proxy, che useremo per controllare MATLAB e ne imposta il path
	 * @param args
	 * @throws MatlabConnectionException 
	 * @throws MatlabInvocationException 
	*/
	public void init() throws MatlabConnectionException, MatlabInvocationException
	{
		factory = new MatlabProxyFactory();
		proxy = factory.getProxy();
	}	
	
	public void eval(String path) throws MatlabConnectionException, MatlabInvocationException {
	//matlab path
		String complete_path = path;
		
		proxy.feval(complete_path);
	}
	
	/**
	 * Esegue la funzione svd_IR 
	 * @throws MatlabInvocationException 
	*/
	public void svd_IR() throws MatlabInvocationException 
	{
		proxy.eval("svd_IR");
	}
	
	/**
	 * Esegue la funzione pca_IR 
	 * @throws MatlabInvocationException 
	*/
	public void pca_IR() throws MatlabInvocationException 
	{
		proxy.eval("pca_IR");
	}
	
	/**
	 * disconnette il proxy da Matlab
	 */
	public void shutdown() 
	{
		proxy.disconnect();
	}
}