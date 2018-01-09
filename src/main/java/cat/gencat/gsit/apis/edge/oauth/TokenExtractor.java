package cat.gencat.gsit.apis.edge.oauth;

import javax.servlet.http.HttpServletRequest;

public interface TokenExtractor {

	String extractToken(HttpServletRequest request);

}