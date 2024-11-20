package ebook.library.data.service;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;

import ebook.library.data.entity.RoleEntity;
import ebook.library.data.entity.UserEntity;
import ebook.library.data.repositories.RoleRepository;
import ebook.library.data.repositories.UserRepository;

@Service
public class RoleService extends CrudService<RoleEntity, Integer> {

	private RoleRepository roleRepository;

	public RoleService(@Autowired RoleRepository repository) {
		this.roleRepository = repository;
	}

	@Override
	protected RoleRepository getRepository() {
		return roleRepository;
	}

	public Collection<RoleEntity> findAll() {
		return roleRepository.findAll();
	}
	
	public RoleEntity getRoleByCode(String roleCode) {
		return roleRepository.findByCode(roleCode);
	}
}
