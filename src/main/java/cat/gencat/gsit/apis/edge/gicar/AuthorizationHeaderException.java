package cat.gencat.gsit.apis.edge.gicar;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class AuthorizationHeaderException extends AuthenticationException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 2634777682045063649L;

	public AuthorizationHeaderException(String msg) {
        super(msg);
    }

    public AuthorizationHeaderException(String msg, Throwable t) {
        super(msg, t);
    }
}
