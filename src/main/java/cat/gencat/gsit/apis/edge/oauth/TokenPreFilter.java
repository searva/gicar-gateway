package cat.gencat.gsit.apis.edge.oauth;



import java.util.Date;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

public class TokenPreFilter extends ZuulFilter {

	private final Logger LOG = LoggerFactory.getLogger(TokenPreFilter.class);
	
	private ResourceServerTokenServices tokenService;
	private TokenExtractor tokenExtractor;
	private JwtAccessTokenConverter tokenEnhancer;
	
	
	public TokenPreFilter(TokenExtractor tokenExtractor, ResourceServerTokenServices tokenService, JwtAccessTokenConverter tokenEnhancer) {
		this.tokenService = tokenService;
		this.tokenExtractor = tokenExtractor;
		this.tokenEnhancer = tokenEnhancer;
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
		
		String accessToken = this.tokenExtractor.extractToken(request);

		try {
			OAuth2Authentication authentication = this.tokenService.loadAuthentication(accessToken);
			LOG.info("Authentication: " + authentication);
			
			DefaultOAuth2AccessToken token = new DefaultOAuth2AccessToken(UUID.randomUUID().toString());
			int validitySeconds = 300;
			if (validitySeconds > 0) {
				token.setExpiration(new Date(System.currentTimeMillis() + (validitySeconds * 1000L)));
			}
			token.setScope(authentication.getOAuth2Request().getScope());

			OAuth2AccessToken jwtToken = this.tokenEnhancer.enhance(token, authentication);
			
			ctx.addZuulRequestHeader("Authorization", "Bearer " + jwtToken.getValue());
			
		}catch(InvalidTokenException e) {
			this.setFailedRequest(e.getMessage(), 403);
		}
		
		return null;
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
