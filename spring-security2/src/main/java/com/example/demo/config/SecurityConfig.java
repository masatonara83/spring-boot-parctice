package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.example.demo.util.Role;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	private final UserDetailsService userDetailsService;
	

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Override
	public void configure(WebSecurity web) throws Exception {
		//セキュリティ設定を無視（ignoring）するパスを指定
		//通常cssやjs、imgなどの静的リソースを指定
		web.ignoring().antMatchers("/js/**","/css/**","/img/**","/webjars/**");
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests()
			.antMatchers("/login", "/error", "/register").permitAll() //「/login"」と「/error」をアクセス可能に
			.antMatchers("/admin/**").hasRole(Role.ADMIN.name()) //「/admin」はAdminユーザーだけがアクセス可能
			.anyRequest().authenticated()
			.and()
			.formLogin()
			.loginPage("/login") //ログイン時のURL設定
			.defaultSuccessUrl("/") //認証後にリダイレクトする場所を設定
			.and()
			.logout() //ログアウト設定
			.logoutRequestMatcher(new AntPathRequestMatcher("/logout")) //ログアウト時のURLを指定
			.and()
			.rememberMe().tokenValiditySeconds(86400); //tokenValiditySeconds(86400)を入れると有効期限を設定できる
			/* Remember-Me認証を許可します
			 * これを設定すると、ブラウザーを閉じて
			 * 再度開いても「ログインしたまま」にできる
			 * */
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//		auth.inMemoryAuthentication()
//			.withUser("admin") //ユーザー名 adminとuserの二つ用意
//			.password(passwordEncoder().encode("password"))
//			.authorities("ROLE_ADMIN")
//			.and()
//			.withUser("user")
//			.password(passwordEncoder().encode("password"))
//			.authorities("ROLE_USER");
		
		//UserDetailServiceを使用して、DBからユーザーを参照できるようにします
		auth.userDetailsService(userDetailsService)
			.passwordEncoder(passwordEncoder());
	}
}
