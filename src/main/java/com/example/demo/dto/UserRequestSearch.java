package com.example.demo.dto;

import java.io.Serializable;

import javax.validation.constraints.Pattern;

import lombok.Data;

/**
 * ユーザー情報（検索条件用） リクエストデータ
 */
@Data
public class UserRequestSearch implements Serializable {
	  /**
	   * 男性（チェックボックス用）
	   */
	  private Boolean checkMale;
	  /**
	   * 女性（チェックボックス用）
	   */
	  private Boolean checkFemale;
	  /**
	   * 年齢（開始条件）
	   */
	  // 未入力も可とする。(※『null』データの場合、バリデーションチェックの対象外となります。)
	  @Pattern(regexp = "\\d{1,3}", message = "年齢(開始条件)は3桁以内で、正しい数値を入力して下さい。")
	  private String age;
	  /**
	   * 年齢（終了条件）
	   */
	  // 未入力も可とする。(※『null』データの場合、バリデーションチェックの対象外となります。)
	  @Pattern(regexp = "\\d{1,3}", message = "年齢(終了条件)は3桁以内で、正しい数値を入力して下さい。")
	  private String endAge;
}