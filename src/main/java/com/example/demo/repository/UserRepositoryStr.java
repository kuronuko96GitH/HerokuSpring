package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.User;

/**
 * ユーザー情報 Repository
 */
@Repository
public interface UserRepositoryStr extends JpaRepository<User, String> {

	// emailなど、検索したい項目名（String型）で検索する。
	List<User> findByName(String Name);

	// emailなど、検索したい項目名（String型）で検索する。
	List<User> findByEmail(String Email);
/*
	@Query("SELECT * AS EMAIL FROM USERS WHERE USERS.EMAIL = ?1")
	List<User> findByEmailCnt(String Email);
*/
}