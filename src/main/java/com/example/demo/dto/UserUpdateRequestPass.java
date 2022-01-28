package com.example.demo.dto;

import java.io.Serializable;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * ユーザー情報（パスワードのみ） リクエストデータ
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class UserUpdateRequestPass implements Serializable {
  /**
   * ユーザーID
   */
  @NotNull(message = "IDが取得できません。")
  private Long id;

  /**
   * パスワード
   */
  @NotEmpty(message = "パスワードを入力して下さい。")
  @Size(min = 8, max = 20, message = "パスワードは８桁以上、２０桁以内で入力して下さい。")
  private String password;

  /**
   * パスワード（確認用）
   */
  @NotEmpty(message = "パスワード（確認用）を入力して下さい。")
  @Size(min = 8, max = 20, message = "パスワード（確認用）は８桁以上、２０桁以内で入力して下さい。")
  private String passwordConfirm;
  
  
  /**
   * パスワードとパスワード（確認用）のチェック
   */
  @AssertTrue(message = "パスワードとパスワード（確認用）は同じ値を入力して下さい。")
  public boolean isPasswordConfirmed() {
	// 補足説明：この処理をする時は、【PasswordConfirmed】なる、『架空』の『Boolean型』メソッドがある前提で処理をしますが、
	//　チェック対象の戻り値として、PasswordConfirmedの『Boolean型』を作る必要はありません。
  	if (password == null && passwordConfirm == null) {
  		return true;
  	}
  	// 補足説明：メッセージ内容を記入しなかった場合は、『true にしてください』のメッセージが表示されます。
  	return password.equals(passwordConfirm);
  }
}