package exceptions;

public class NoPaperWithSuchIDException extends Exception {
	private static final long serialVersionUID = 100004L;

	public NoPaperWithSuchIDException(String message) {
		super(message);
	}
}