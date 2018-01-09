package cat.gencat.gsit.apis.edge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import cat.gencat.gsit.apis.edge.oauth.AccessTokenValidator;
import cat.gencat.gsit.apis.edge.oauth.GoogleAccessTokenValidator;
import cat.gencat.gsit.apis.edge.oauth.GoogleTokenServices;
import cat.gencat.gsit.apis.edge.oauth.OauthAccessTokenExtractor;
import cat.gencat.gsit.apis.edge.oauth.OauthProperties;
import cat.gencat.gsit.apis.edge.oauth.TokenExtractor;
import cat.gencat.gsit.apis.edge.oauth.TokenPreFilter;

@SpringBootApplication
@EnableZuulProxy
public class PublicGatewayApplication {

	@Bean
	public TokenPreFilter tokenFilter(TokenExtractor tokenExtractor, ResourceServerTokenServices tokenService, JwtAccessTokenConverter tokenEnhancer) {
		return new TokenPreFilter(tokenExtractor, tokenService, tokenEnhancer);
	}
	
	@Bean
	public TokenExtractor tokenExtractor() {
		return new OauthAccessTokenExtractor();
	}
	
	@Bean
	public AccessTokenValidator accessTokenValidator(OauthProperties oauthProperties) {
		GoogleAccessTokenValidator validator = new GoogleAccessTokenValidator();
		validator.setClientId(oauthProperties.getClientId());
		validator.setCheckTokenUrl(oauthProperties.getCheckTokenUrl());
		return validator;
	}
	
	@Bean
	public ResourceServerTokenServices resourceServerTokenServices(OauthProperties oauthProperties,AccessTokenValidator tokenValidator) {
		GoogleTokenServices service = new GoogleTokenServices(tokenValidator);
		service.setUserInfoUrl(oauthProperties.getUserInfoUrl());
		return service;
	}

	@Bean
	public OauthProperties oauthProperties() {
		return new OauthProperties();
	}
	
	@Bean
	public JwtAccessTokenConverter accessTokenConverter(OauthProperties oauthProperties) {
		JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
		converter.setSigningKey(oauthProperties.getSigningKey());
		return converter;
	}
	
	public static void main(String[] args) {
		SpringApplication.run(PublicGatewayApplication.class, args);
	}
}
