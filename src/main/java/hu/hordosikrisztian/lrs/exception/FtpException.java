package hu.hordosikrisztian.lrs.exception;

public class FtpException extends RuntimeException {

	private static final long serialVersionUID = 6091996678386343174L;

	public FtpException(String message, Throwable cause) {
		super(message, cause);
	}

}
