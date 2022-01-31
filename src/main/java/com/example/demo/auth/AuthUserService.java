package com.example.demo.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthUserService {
	
	@Autowired
	private AuthUserRepository authUserRepository;

	// 作ってみたが、このクラスは現状使う予定なし。(2022/01/29時点)
	public boolean isDupplicateEmail(String email) throws UsernameNotFoundException {
//	public boolean isDupplicateEmail(String email) throws Exception {
		AuthUser user = authUserRepository.identifyUser(email);
		  return user != null;
	}

}