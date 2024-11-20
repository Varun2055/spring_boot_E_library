package ebook.library.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import ebook.library.data.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {

	UserEntity findByUsername(String username);
}
