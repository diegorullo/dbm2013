package exceptions;

public class AuthorWithoutCoAuthorsException extends Exception {
	private static final long serialVersionUID = 100000L;

	public AuthorWithoutCoAuthorsException(String message) {
		super(message);
	}
}
