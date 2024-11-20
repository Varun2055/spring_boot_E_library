package ebook.library.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import ebook.library.data.entity.RatingEntity;

public interface RatingRepository extends JpaRepository<RatingEntity, Integer> {

}
