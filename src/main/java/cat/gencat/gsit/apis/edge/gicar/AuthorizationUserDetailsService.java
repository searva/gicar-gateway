package cat.gencat.gsit.apis.edge.gicar;
import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import cat.gencat.gsit.apis.edge.config.CacheConfiguration;

/* Not marked @Component to simplify WebSecurityConfiguration. */
public class AuthorizationUserDetailsService implements AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> {
    private static final Logger logger = LoggerFactory.getLogger(AuthorizationUserDetailsService.class);

    @Override
    @Cacheable(value = CacheConfiguration.GICAR_USERS, key = "#token.principal")
    public UserDetails loadUserDetails(PreAuthenticatedAuthenticationToken token) throws UsernameNotFoundException {
        String gicarHeader = token.getPrincipal().toString();
        logger.info("Loading user for Gicar_id: " + gicarHeader);

       
        return new GicarUser(gicarHeader, 103L, Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
    }

  
}
