package ebook.library.data.service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;

import ebook.library.data.entity.AuthorEntity;
import ebook.library.data.repositories.AuthorRepository;

@Service
public class AuthorService extends CrudService<AuthorEntity, Integer> {

	private AuthorRepository authorRepository;

	public AuthorService(@Autowired AuthorRepository repository) {
		this.authorRepository = repository;
	}

	@Override
	protected AuthorRepository getRepository() {
		return authorRepository;
	}
	
	public Collection<AuthorEntity> findAll() {
		return authorRepository.findAll();
	}

	public void saveAll(List<AuthorEntity> authors) {
		authorRepository.saveAll(authors);
	}
}
