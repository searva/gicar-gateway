package cat.gencat.gsit.apis.edge.jwt;

public class TokenCreationException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TokenCreationException(String cause, Exception ex) {
		super(cause, ex);
	}

}
