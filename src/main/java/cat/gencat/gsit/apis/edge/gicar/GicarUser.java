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
	private final String firstName;
	
	public GicarUser(String username, Long id, String firstName, Collection<? extends GrantedAuthority> authorities) {
		super(username, "", authorities);
		this.id = id;
		this.firstName = firstName;
	}

	public Long getId() {
		return id;
	}

	public String getFirstName() {
		return this.firstName;
	}
	

}
