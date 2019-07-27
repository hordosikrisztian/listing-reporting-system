package hu.hordosikrisztian.lrs.exception;

public class HibernatePropertiesLoadingException extends RuntimeException {

	private static final long serialVersionUID = -6703801556453313138L;

	public HibernatePropertiesLoadingException(String message, Throwable cause) {
		super(message, cause);
	}

}
