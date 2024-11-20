package ebook.library.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import ebook.library.data.AbstractEntity;

@Entity
@Table(name = "ratings")
public class RatingEntity extends AbstractEntity {

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "book_id")
	private BookEntity book;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id")
	private UserEntity user;
	
	@Column(name = "rating", nullable = false)
	@NotNull(message = "Rating is required!")
	@Min(1)
    @Max(6)
	private int rating;
}
