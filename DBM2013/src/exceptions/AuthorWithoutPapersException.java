package exceptions;

public class AuthorWithoutPapersException extends Exception {
	private static final long serialVersionUID = 1L;

	public AuthorWithoutPapersException(String message) {
		super(message);
	}
}
