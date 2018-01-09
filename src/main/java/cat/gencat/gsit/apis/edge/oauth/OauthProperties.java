package cat.gencat.gsit.apis.edge.oauth;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "oauth")
public class OauthProperties {
	private String clientId;
	private String clientSecret;
	private String checkTokenUrl;
	private String userInfoUrl;
	private String signingKey;
	
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public String getClientSecret() {
		return clientSecret;
	}
	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}
	public String getCheckTokenUrl() {
		return checkTokenUrl;
	}
	public void setCheckTokenUrl(String checkTokenUrl) {
		this.checkTokenUrl = checkTokenUrl;
	}
	public String getUserInfoUrl() {
		return userInfoUrl;
	}
	public void setUserInfoUrl(String userInfoUrl) {
		this.userInfoUrl = userInfoUrl;
	}
	public String getSigningKey() {
		return signingKey;
	}
	public void setSigningKey(String signinKey) {
		this.signingKey = signinKey;
	}
	
	

}
