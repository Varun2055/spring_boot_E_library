package ebook.library.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import ebook.library.data.entity.RoleEntity;
import ebook.library.data.entity.UserEntity;

public interface RoleRepository extends JpaRepository<RoleEntity, Integer>{

	RoleEntity findByCode(String code);
}
