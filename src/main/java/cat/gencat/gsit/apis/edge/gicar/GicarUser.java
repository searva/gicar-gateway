package cat.gencat.gsit.apis.edge.gicar;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class GicarUser extends User {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8238122722840564437L;
	private final Long id;
	
	public GicarUser(String username, Long id, Collection<? extends GrantedAuthority> authorities) {
		super(username, "", authorities);
		this.id = id;
	}

	public Long getId() {
		return id;
	}


}
