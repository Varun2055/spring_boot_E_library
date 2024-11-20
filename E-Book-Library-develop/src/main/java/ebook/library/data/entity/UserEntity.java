package ebook.library.data.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import ebook.library.data.AbstractEntity;

@Entity
@Table(name = "users")
public class UserEntity extends AbstractEntity {
	
	@Column(name = "first_name", length = 50, nullable = false)
	@NotNull(message = "Field is required!")
	private String firstName;
	
	@Column(name = "last_name", length = 80, nullable = false)
	@NotNull(message = "Field is required!")
	private String lastName;
	
	@Column(name = "username", length = 255, nullable = false, unique = true)
	@NotNull(message = "Field is required!")
	private String username;
	
	@Column(name = "password", length = 255, nullable = false)
	@NotNull(message = "Field is required!")
	private String password;
	
	@Column(name = "email", length = 255, nullable = false, unique = true)
	@Email(message = "Email not valid!")
	@NotNull(message = "Field is required!")
	private String email;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
      @JoinTable(name = "users_books",
            joinColumns = {
                    @JoinColumn(name = "user_id", referencedColumnName = "id",
                            nullable = false, updatable = false)},
            inverseJoinColumns = {
                    @JoinColumn(name = "book_id", referencedColumnName = "id",
                            nullable = false, updatable = false)})
    private Set<BookEntity> favouriteBooks = new HashSet<>();
	
    @ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "account_role", joinColumns = @JoinColumn(name = "account_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<RoleEntity> roles;
    
    
	public UserEntity() {
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


	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}


	public Set<BookEntity> getFavouriteBooks() {
		return favouriteBooks;
	}


	public void setFavouriteBooks(Set<BookEntity> favouriteBooks) {
		this.favouriteBooks = favouriteBooks;
	}


	public Set<RoleEntity> getRoles() {
		return roles;
	}


	public void setRoles(Set<RoleEntity> roles) {
		this.roles = roles;
	}	
}
