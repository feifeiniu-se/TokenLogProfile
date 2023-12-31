package pipe.io;

/**
 * @author Matt and Edwin
 */
public class AbortDotFileGenerationException extends Exception {

	public AbortDotFileGenerationException() {
		super("Generate method could not carry out file io.");
	}

	public AbortDotFileGenerationException(String reason) {
		super(reason);
	}

}
