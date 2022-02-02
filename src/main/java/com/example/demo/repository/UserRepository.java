package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.User;

/**
 * ユーザー情報 Repository
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	// idで検索する。
	@Query(value = "SELECT * FROM USERS WHERE USERS.ID = ?1", nativeQuery=true)
	List<User> findByUserId(Long Id);

	// emailで検索する。
	List<User> findByEmail(String Email);

	@Query(value = "SELECT * FROM USERS WHERE USERS.ID <> ?1 AND USERS.EMAIL = ?2", nativeQuery=true)
	// ユーザーIDとemailで検索する。
	List<User> findByEmail(long Id, String Email);
}