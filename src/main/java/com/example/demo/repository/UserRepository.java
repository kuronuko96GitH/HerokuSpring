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

	// 年齢（開始年齢）で検索する。
	@Query(value = "SELECT * FROM USERS WHERE AGE >= ?1 ORDER BY ID", nativeQuery=true)
	List<User> findByStartAge(String startAge);

	// 年齢（終了年齢)で検索する。
	@Query(value = "SELECT * FROM USERS WHERE AGE <= ?1 ORDER BY ID", nativeQuery=true)
	List<User> findByEndAge(String endAge);

	// 年齢（開始年齢～終了年齢)で検索する。
	@Query(value = "SELECT * FROM USERS WHERE AGE >= ?1 AND AGE <= ?2 ORDER BY ID", nativeQuery=true)
	List<User> findByAge(String startAge, String endAge);

	// 年齢（開始年齢～終了年齢)＋αで検索する。
//	@Query(value = "SELECT * FROM USERS WHERE AGE >= ?1 AND AGE <= ?2 AND gender IN (1,2) ORDER BY ID", nativeQuery=true)
	@Query(value = "SELECT * FROM USERS WHERE AGE >= ?1 AND AGE <= ?2 AND GENDER = ?3 ORDER BY ID", nativeQuery=true)
	List<User> findByAge(String startAge, String endAge, Integer intGENDER);
}