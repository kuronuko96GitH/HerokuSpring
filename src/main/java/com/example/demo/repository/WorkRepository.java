package com.example.demo.repository;

import java.util.Date;
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

	// ユーザーIDと勤怠開始日で検索する。
	@Query(value = "SELECT * FROM WORKS WHERE USER_ID = ?1 AND START_AT >= ?2 ORDER BY START_AT, END_AT", nativeQuery=true)
	List<Work> findByStartDate(Long userid, Date startDate);

//	@Query(value = "SELECT * FROM WORKS WHERE USER_ID = ?1 AND END_AT <= ?2 ORDER BY START_AT, END_AT", nativeQuery=true)
	// ユーザーIDと勤怠終了日(勤怠開始日)で検索する。
	@Query(value = "SELECT * FROM WORKS WHERE USER_ID = ?1 AND START_AT <= ?2 ORDER BY START_AT, END_AT", nativeQuery=true)
	List<Work> findByEndDate(Long userid, Date endDate);

	// ユーザーIDと勤怠開始日～勤怠終了日(勤怠開始日)で検索する。
//	@Query(value = "SELECT * FROM WORKS WHERE USER_ID = ?1 AND START_AT >= ?2 AND END_AT <= ?3 ORDER BY START_AT, END_AT", nativeQuery=true)
	@Query(value = "SELECT * FROM WORKS WHERE USER_ID = ?1 AND START_AT >= ?2 AND START_AT <= ?3 ORDER BY START_AT, END_AT", nativeQuery=true)
	List<Work> findByDate(Long userid, Date startDate, Date endDate);

	// 選択中のIDは除外して、ユーザーIDと勤怠開始日～勤怠終了日(勤怠開始日)で検索する。
	// 更新時の重複チェックなどに使用。
	@Query(value = "SELECT * FROM WORKS WHERE ID <> ?1 AND USER_ID = ?2 AND START_AT >= ?3 AND START_AT <= ?4 ORDER BY START_AT, END_AT", nativeQuery=true)
	List<Work> findByDateDuplicate(Long id, Long userid, Date startDate, Date endDate);

	// ユーザーIDとSQL(WHERE)文で検索する。（※このやり方は、上手くいかないようです。）
//	@Query(value = "SELECT * FROM WORKS WHERE USER_ID = ?1 ?2 ORDER BY START_AT, END_AT", nativeQuery=true)
//	List<Work> findSql(Long userid, String sql);

	// ユーザーIDと勤怠内容で検索する。
//	@Query(value = "SELECT * FROM WORKS WHERE USER_ID = ?1 AND content = ?2 ORDER BY START_AT, END_AT", nativeQuery=true)
//	List<Work> findByContent(Long userid, String content);
}