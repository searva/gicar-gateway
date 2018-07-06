package cat.gencat.gsit.apis.edge.oauth;

import java.util.Set;

public class User {
	
	String id;
	Set<String> roles;
	
	public User(String id) {
		this.id = id;
	}

	public String getId() {
		return this.id;
	}
	
	public String[] getRoles() {
		return (String[]) roles.toArray(new String[roles.size()]);
	}

	public void setRoles(Set<String> roles) {
		this.roles = roles;
		
	}
	

}
