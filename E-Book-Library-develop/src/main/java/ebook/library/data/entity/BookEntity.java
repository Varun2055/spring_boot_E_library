package ebook.library.data.entity;

import java.time.Year;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ebook.library.data.AbstractEntity;

@Entity
@Table(name = "books")
public class BookEntity extends AbstractEntity {

	@Column(name = "title", length = 255, nullable = false)
	private String title;
	
	@Column(name = "isbn", length = 255, nullable = false)
	private String isbn;
	
	@Column(name = "description", length = 2000)
	private String description;
	
	@Column(name = "year_published")
	private int yearPublished;
	
	@Column(name = "cover_location", length = 255, nullable = false)
	private String coverLocation;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "author_id")
	private AuthorEntity author;

    @ManyToMany(mappedBy = "favouriteBooks", fetch = FetchType.LAZY)
    private Set<UserEntity> users = new HashSet<>();
	
	public BookEntity() {
		
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getYearPublished() {
		return yearPublished;
	}

	public void setYearPublished(int yearPublished) {
		this.yearPublished = yearPublished;
	}

	public String getCoverLocation() {
		return coverLocation;
	}

	public void setCoverLocation(String coverLocation) {
		this.coverLocation = coverLocation;
	}

	public AuthorEntity getAuthor() {
		return author;
	}

	public void setAuthor(AuthorEntity author) {
		this.author = author;
	}
	
	public String getAuthorName() {
		return this.author.getAuthorName();
	}
	
}
