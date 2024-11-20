package ebook.library.data.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import ebook.library.data.AbstractEntity;

@Entity
@Table(name = "authors")
public class AuthorEntity extends AbstractEntity {

	@Column(name = "first_name", length = 50, nullable = false)
	@NotNull(message = "Field is required!")
	private String firstName;
	
	@Column(name = "last_name", length = 80, nullable = false)
	@NotNull(message = "Field is required!")
	private String lastName;
	
	@Column(name = "biography", length = 2000)
	private String biography = "";
	
	@OneToMany(mappedBy = "author", fetch = FetchType.EAGER)
	private List<BookEntity> books;

	public AuthorEntity() {
	
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

	public String getBiography() {
		return biography;
	}

	public void setBiography(String biography) {
		this.biography = biography.isBlank() ? "" : biography;
	}

	public List<BookEntity> getBooks() {
		return books;
	}

	public void setBooks(List<BookEntity> books) {
		this.books = books;
	}
	
	public String getAuthorName() {
		return String.format("%s %s",this.getFirstName(), this.getLastName());
	}
	
}
