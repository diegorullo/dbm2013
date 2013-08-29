package exceptions;

public class WrongFileTypeException extends Exception {
	private static final long serialVersionUID = 100008L;

	public WrongFileTypeException(String message) {
		super(message);
	}
}
