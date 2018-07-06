package cat.gencat.gsit.apis.edge.oauth;



import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;

import com.google.common.io.BaseEncoding;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class TokenPreFilter extends ZuulFilter {
	
	final String SECRET = BaseEncoding.base64().encode("0fK9jWwWcHdi".getBytes());

	private final Logger LOG = LoggerFactory.getLogger(TokenPreFilter.class);
	
	
	public TokenPreFilter() {
	}
	
	@Override
	public boolean shouldFilter() {
	    return true;
	}

	@Override
	public int filterOrder() {
	    return 1;
	}

	@Override
	public String filterType() {
	    return "pre";
	}
	
    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
		HttpServletRequest request = ctx.getRequest();
		
		Set<String> roles = new HashSet<>();
		String gicar_id = "00000000T";
		roles.add("USER");
		User user = new User(gicar_id);
		user.setRoles(roles);
		

		try {
			LOG.info("Authentication: " + gicar_id);
			
			String jwtToken = this.encode(user);
			
			LOG.info("jwt: " + jwtToken);
			
			ctx.addZuulRequestHeader("Authorization", "Bearer " + jwtToken);
			
		}catch(InvalidTokenException | TokenCreationException e) {
			this.setFailedRequest(e.getMessage(), 403);
		}
		
		return null;
    }
    
    public String encode(User user) throws TokenCreationException {
        
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        
        long nowMillis = System.currentTimeMillis();
        long expMillis = nowMillis + 20000L;
        
        Date now = new Date(nowMillis);
        Date exp = new Date(expMillis);
        
        JwtBuilder builder = Jwts.builder()
                .setIssuer("gicar")
                .setSubject(user.getId())
                .setIssuedAt(now)
                .setExpiration(exp)
                .claim("role", "ROLE_USER")
                .claim("agentId", 103)
                .signWith(signatureAlgorithm, SECRET);
        
       return builder.compact(); 
     
    }
    
    private void setFailedRequest(String body, int code) {
        LOG.debug("Reporting error ({}): {}", code, body);
        RequestContext ctx = RequestContext.getCurrentContext();
        ctx.setResponseStatusCode(code);
        if (ctx.getResponseBody() == null) {
            ctx.setResponseBody(body);
            ctx.setSendZuulResponse(false);
        }
    }
    

}
