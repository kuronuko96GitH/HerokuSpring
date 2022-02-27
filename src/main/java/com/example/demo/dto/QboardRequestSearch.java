package com.example.demo.dto;

import java.io.Serializable;

import javax.validation.constraints.Size;

import lombok.Data;

/**
 * 質問板情報（検索条件用） リクエストデータ
 */
@Data
public class QboardRequestSearch implements Serializable {
	  /**
	   * 最新２０件（チェックボックス用）
	   */
//	  private Boolean checkMale;
	  /**
	   * 最新５０件（チェックボックス用）
	   */
//	  private Boolean checkFemale;

	  // 投稿内容（キーワード検索）
	  // 未入力も可とする。(※『null』データの場合、バリデーションチェックの対象外となります。)
	  @Size(max = 20, message = "検索キーワードは20文字以内で入力して下さい。")
	  private String content1;
/*
	  // 投稿内容（キーワード検索）
	  // 未入力も可とする。(※『null』データの場合、バリデーションチェックの対象外となります。)
	  @Size(max = 10, message = "キーワード２を10桁以内で入力して下さい。")
	  private String content2;

	  // 投稿内容（キーワード検索）
	  // 未入力も可とする。(※『null』データの場合、バリデーションチェックの対象外となります。)
	  @Size(max = 10, message = "キーワード３を10桁以内で入力して下さい。")
	  private String content3;
*/
}