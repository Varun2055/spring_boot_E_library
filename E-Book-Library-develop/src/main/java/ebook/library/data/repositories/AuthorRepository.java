package ebook.library.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import ebook.library.data.entity.AuthorEntity;

public interface AuthorRepository extends JpaRepository<AuthorEntity, Integer> {

}
