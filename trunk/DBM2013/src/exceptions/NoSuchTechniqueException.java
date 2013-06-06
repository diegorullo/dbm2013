package exceptions;

public class NoSuchTechniqueException extends Exception {
	private static final long serialVersionUID = 100005L;

	public NoSuchTechniqueException(String message) {
		super(message);
	}
}
