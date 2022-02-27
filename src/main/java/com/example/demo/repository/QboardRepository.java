package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Qboard;

/**
 * 質問板情報 Repository
 */
@Repository
public interface QboardRepository extends JpaRepository<Qboard, Long> {

	// 質問一覧（ヘッダ情報）を検索する。(返信投稿を除く、最初の質問だけを取得する)
	@Query(value = "SELECT * FROM QBOARDS WHERE BODY_ID = 1 AND STATUS_CODE < 8 ORDER BY CREATED_AT DESC", nativeQuery=true)
	List<Qboard> findList();

	// 質問一覧を検索する。(HEAD_IDに該当するボディデータを取得する)
	@Query(value = "SELECT * FROM QBOARDS WHERE HEAD_ID = ?1 AND STATUS_CODE <= 8 ORDER BY ID", nativeQuery=true)
	List<Qboard> findBodyList(Integer headId);


	// 最新のヘッダIDを取得する（LIMIT 1…最初の一件だけを取得）
	@Query(value = "SELECT * FROM QBOARDS WHERE BODY_ID = 1 ORDER BY HEAD_ID DESC LIMIT 1", nativeQuery=true)
	List<Qboard> findHeadIdMax();

	// 最新のボディIDを取得する（LIMIT 1…最初の一件だけを取得）
	@Query(value = "SELECT * FROM QBOARDS WHERE HEAD_ID = ?1 ORDER BY BODY_ID DESC LIMIT 1", nativeQuery=true)
	List<Qboard> findBodyIdMax(Integer headId);




	// ユーザーIDで検索する。
	@Query(value = "SELECT * FROM QBOARDS WHERE USER_ID = ?1 ORDER BY ID", nativeQuery=true)
	List<Qboard> findByUserID(Long userid);

}