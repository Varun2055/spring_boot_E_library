package ebook.library.data.service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;

import ebook.library.data.entity.BookEntity;
import ebook.library.data.entity.UserEntity;
import ebook.library.data.repositories.BookRepository;
import ebook.library.data.repositories.UserRepository;

@Service
public class BookService extends CrudService<BookEntity, Integer> {

	private BookRepository bookRepository;
	private UserRepository userRepository;

	public BookService(@Autowired BookRepository repository, UserRepository userRepository) {
		this.bookRepository = repository;
		this.userRepository = userRepository;
	}
	
	@Override
	protected BookRepository getRepository() {
		return bookRepository;
	}

	public Collection<BookEntity> findAll() {
		return bookRepository.findAll();
	}
	
	public void saveAll(List<BookEntity> books) {
		bookRepository.saveAll(books);
	}	
	
	public Collection<BookEntity> getUserFavouriteBooks(int userId) {
		UserEntity user = userRepository.getById(userId);
		return user.getFavouriteBooks();
	}
}
