package ebook.library.data.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;

import ebook.library.data.entity.RatingEntity;
import ebook.library.data.repositories.RatingRepository;

@Service
public class RatingService extends CrudService<RatingEntity, Integer> {

	private RatingRepository ratingRepository;

	public RatingService(@Autowired RatingRepository repository) {
		this.ratingRepository = repository;
	}

	@Override
	protected RatingRepository getRepository() {
		return ratingRepository;
	}
}
