package hu.hordosikrisztian.lrs.exception;

public class LogCreationException extends RuntimeException {
	
	private static final long serialVersionUID = -8658503918272677309L;

	public LogCreationException(String message, Throwable cause) {
		super(message, cause);
	}

}
