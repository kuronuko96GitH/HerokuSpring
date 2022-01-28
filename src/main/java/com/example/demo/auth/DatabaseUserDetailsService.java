package com.example.demo.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class DatabaseUserDetailsService implements UserDetailsService {
	
	@Autowired
	private AuthUserRepository authUserRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		AuthUser user = authUserRepository.identifyUser(username);
		if (user == null) {
			// メールアドレスが間違っており、データベースから取得したデータがnullだった場合。
			throw new UsernameNotFoundException(username);
		}
		return user;
//		return authUserRepository.identifyUser(username);
	}
}