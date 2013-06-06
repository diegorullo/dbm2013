package exceptions;

public class NoAuthorsWithSuchIDException extends Exception {
	private static final long serialVersionUID = 100002L;

	public NoAuthorsWithSuchIDException(String message) {
		super(message);
	}
}