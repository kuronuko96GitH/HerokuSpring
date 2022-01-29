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

	// ユーザー名、検索したい項目名（String型）で検索する。
	List<User> findByName(String Name);

	// email、検索したい項目名（String型）で検索する。
	List<User> findByEmail(String Email);
/*
	@Query("SELECT * AS EMAIL FROM USERS WHERE USERS.EMAIL = ?1")
	List<User> findByEmailCnt(String Email);
*/
	/* この書き方は駄目らしい。保留
	@Query("SELECT * AS EMAIL FROM USERS WHERE USERS.ID <> ?1 AND USERS.NAME = ?2")
	// email、検索したい項目名（String型）で検索する。
	List<User> findByIdName(long Id, String Name);

	@Query("SELECT * AS EMAIL FROM USERS WHERE USERS.ID <> ?1 AND USERS.EMAIL = ?2")
	// email、検索したい項目名（String型）で検索する。
	List<User> findByIdEmail(long Id, String Email);
	*/
}