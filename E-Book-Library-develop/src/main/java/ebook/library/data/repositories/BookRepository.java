package ebook.library.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import ebook.library.data.entity.BookEntity;

public interface BookRepository extends JpaRepository<BookEntity, Integer> {

}
