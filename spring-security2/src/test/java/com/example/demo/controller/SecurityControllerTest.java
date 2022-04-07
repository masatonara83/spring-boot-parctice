package com.example.demo.controller;

import static org.hamcrest.CoreMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.model.SiteUser;
import com.example.demo.util.Role;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class SecurityControllerTest {
	
	@Autowired
	MockMvc mockMvc;

	@Test
	void 登録エラーがある時にエラー表示することを期待() throws Exception {
		mockMvc.perform(post("/register")
				.flashAttr("user", new SiteUser()) //入力の属性を設定
				.with(csrf()) //CSRFトークンを自動生成
				)
				.andExpect(model().hasErrors()) //エラーがあることを検証
				.andExpect(view().name("register")); //指定したHTMLを表示しているかを検証
	}
	
	@Test
	void 管理者ユーザーとして登録する時に成功するかを検証() throws Exception {
		//準備
		var user = new SiteUser();
		user.setUsername("管理者ユーザー");
		user.setPassword("password");
		user.setEmail("admin@example.com");
		user.setGender(0);
		user.setAdmin(true);
		user.setRole(Role.ADMIN.name());
		
		//検証
		mockMvc.perform(post("/register").flashAttr("user", user).with(csrf()))
				.andExpect(model().hasNoErrors()) //エラーがないことを検証
				.andExpect(redirectedUrl("/login?register")) //指定したURLにレダイレクトされつか
				.andExpect(status().isFound()); //ステータスコードがFound（302）であること
	}
	
	@Test
	@WithMockUser(username="admin", roles="ADMIN")
	void 管理者ユーザーがログイン時ユーザーの一覧を表示できるかを検証() throws Exception{
		mockMvc.perform(get("/admin/list"))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("ユーザ一覧"))) //HTMLの表示内容に指定した文字列が含まれているか
				.andExpect(view().name("list"));
	}

}
