package exceptions;

public class AuthorWithoutPapersException extends Exception {
	private static final long serialVersionUID = 100001L;

	public AuthorWithoutPapersException(String message) {
		super(message);
	}
}
