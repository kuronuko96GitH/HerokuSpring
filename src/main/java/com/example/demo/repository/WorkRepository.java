package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Work;

/**
 * ユーザー情報 Repository
 */
@Repository
public interface WorkRepository extends JpaRepository<Work, Long> {

	// ユーザーIDで検索する。
	@Query(value = "SELECT * FROM WORKS WHERE USER_ID = ?1 ORDER BY START_AT, END_AT", nativeQuery=true)
	List<Work> findByUserID(Long userid);
//	public List<Work> findByUserID(Long userid);
}