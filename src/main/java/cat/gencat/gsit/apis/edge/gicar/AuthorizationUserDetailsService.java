package cat.gencat.gsit.apis.edge.gicar;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import cat.gencat.gsit.apis.edge.config.CacheConfiguration;

public class AuthorizationUserDetailsService implements AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	
    private static final Logger logger = LoggerFactory.getLogger(AuthorizationUserDetailsService.class);

    @Override
    @Cacheable(value = CacheConfiguration.GICAR_USERS, key = "#token.principal")
    public UserDetails loadUserDetails(PreAuthenticatedAuthenticationToken token) throws UsernameNotFoundException {
        String gicarHeader = token.getPrincipal().toString();
        
        if (logger.isInfoEnabled()) {
        	logger.info(String.format("Loading user for Gicar_id: %s", gicarHeader));
        }
       
        return getAndCheckUsuariEnabled(gicarHeader);
    }
    
    private GicarUser getAndCheckUsuariEnabled(final String nif) {
    	GicarUser gicarUser = null;
		
		StringBuilder stb = new StringBuilder("SELECT ac.* FROM GC_ACTOR ac ");
		stb.append("INNER JOIN GC_AGENT ag ON ac.ID = ag.ID AND ac.LOGIN_ID = ? AND ac.ACTOR_TYPE = 2 ");
		stb.append("INNER JOIN GC_AGENT_APPLICATIONS agapp ON ac.ID = agapp.ACTOR_ID ");
		
//		Map<String, Object> results;
//		try {
//			results = jdbcTemplate.queryForMap(stb.toString(), nif);
//		} catch (Exception exception) {
//			logger.error("Error buscando al usuario {}", nif);
//			results = new LinkedHashMap<>();
//		}
		
		//TODO: añadir los GrantedAuthorithy en base a las aplicaciones a las que tenga acceso el usuario ahora devolvemos algo dummy
		
		return new GicarUser(nif, 103L, "pruebas", Arrays.asList(new GrantedAuthority[] {new SimpleGrantedAuthority("ROLE_USER")}));
		
	}


  
}
