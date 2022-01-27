package com.example.demo.auth;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AuthUserRepository {

	// SQLに対応している
	public AuthUser identifyUser(String email);
}
