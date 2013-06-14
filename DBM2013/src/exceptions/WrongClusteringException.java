package exceptions;

public class WrongClusteringException extends Exception {
	private static final long serialVersionUID = 100007L;

	public WrongClusteringException(String message) {
		super(message);
	}
}
