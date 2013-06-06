package exceptions;

public class NoAuthorsWithSuchNameException extends Exception {
	private static final long serialVersionUID = 100003L;

	public NoAuthorsWithSuchNameException(String message) {
		super(message);
	}
}
