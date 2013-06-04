package exceptions;

public class NoAuthorsWithSuchIDException extends Exception {
	private static final long serialVersionUID = 1L;

	public NoAuthorsWithSuchIDException(String message) {
		super(message);
	}
}