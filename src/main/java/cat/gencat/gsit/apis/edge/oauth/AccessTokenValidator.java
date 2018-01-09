package cat.gencat.gsit.apis.edge.oauth;

public interface AccessTokenValidator {
    AccessTokenValidationResult validate(String accessToken);
}
