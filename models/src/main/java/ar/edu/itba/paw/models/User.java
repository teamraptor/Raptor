package ar.edu.itba.paw.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "users")
public class User {

	@Id
//  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tweets_tweetid_seq") //TODO what to do with randomID 
//  @SequenceGenerator(sequenceName = "tweets_tweetid_seq", name = "tweets_tweetid_seq", allocationSize = 1) //idem 
	@GenericGenerator(name = "random_id", strategy = "idgenerators.RandomIdGenerator")
    @GeneratedValue(generator = "random_id")
    @Column(name = "userID", length = 12, nullable = false)
    private String id;
	
	@Column(name = "username", nullable = false, length = 100)
    private String username;
	
	@Column(name = "email", nullable = false, length = 100)
    private String email;
	
	@Column(name = "firstName", nullable = false, length = 100)
    private String firstName;
	
	@Column(name = "lastName", nullable = false, length = 100)
    private String lastName;
	
	@Column(name = "verified", nullable = false)
    private Boolean verified;

	@Column(name = "password", nullable = false, length = 100)
	private String password;
	
//    @SuppressWarnings("unused")
//    private final String miniBio = null; //TODO use!

    public User(String username, String email,
                String firstName, String lastName, String password, Boolean verified) {
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.verified = verified;
        this.password = password;
    }

    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	/*
	 *
	 * Getters and Setters
	 * 
	 */
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public Boolean getVerified() {
		return verified;
	}
	
	public void setVerified(Boolean verified) {
		this.verified = verified;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
}
