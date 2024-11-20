package ebook.library.data.service;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;

import ebook.library.data.entity.Roles;
import ebook.library.data.entity.UserEntity;
import ebook.library.data.repositories.UserRepository;

@Service
public class UserService extends CrudService<UserEntity, Integer> {

	private UserRepository userRepository;
	private PasswordEncoder passwordEncoder;
	private RoleService roleService;

	public UserService(@Autowired UserRepository repository, @Autowired PasswordEncoder passwordEncoder, @Autowired RoleService roleService){
		this.userRepository = repository;
		this.roleService = roleService;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	protected UserRepository getRepository() {
		return userRepository;
	}

	public Collection<UserEntity> findAll() {
		return userRepository.findAll();
	}

	public void saveAll(List<UserEntity> users) {
		userRepository.saveAll(users);
	}
	
	public void save(UserEntity user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		if(user.getRoles().isEmpty()) {
			user.getRoles().add(roleService.getRoleByCode(Roles.USER.name()));
		}
		userRepository.save(user);
	}
}
