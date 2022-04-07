package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.*;

import javax.transaction.Transactional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.example.demo.model.SiteUser;
import com.example.demo.repository.SiteUserRepository;
import com.example.demo.util.Role;

@SpringBootTest
@Transactional
class UserDetailServiceImplTest {
	
	@Autowired
	SiteUserRepository repository;
	
	@Autowired
	UserDetailServiceImpl service;

	@Test
	void usernameが存在するときにユーザー詳細を取得できることを期待するTest() throws Exception {
		//準備
		var user = new SiteUser();
		user.setUsername("Harada");
		user.setPassword("password");
		user.setEmail("harada@example.com");
		user.setRole(Role.USER.name());
		repository.save(user);
		
		//実行
		var actual = service.loadUserByUsername("Harada");
		//検証
		assertEquals(user.getUsername(), actual.getUsername());
	}
	
	@Test
	@DisplayName("ユーザーが存在しない場合、例外をスローします")
	void whenUsernameDoesNotExist_throwException() {
		assertThrows(UsernameNotFoundException.class, () -> service.loadUserByUsername("Takeda"));
	}

}
