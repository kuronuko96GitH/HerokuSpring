package com.example.demo.dto;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * ユーザー情報 リクエストデータ
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class UserUpdateRequest implements Serializable {
  /**
   * ユーザーID
   */
  @NotNull(message = "IDが取得できません。")
  private Long id;

  /**
   * 名前
   */
  @NotEmpty(message = "名前を入力してください")
  @Size(max = 100, message = "名前は100桁以内で入力して下さい。")
  private String name;
  /**
   * メールアドレス
   */
  @NotEmpty(message = "メールアドレスを入力してください")
  @Size(max = 255, message = "メールアドレスは255桁以内で入力して下さい。")
  private String email;
  /**
   * パスワード
   */
//  @NotEmpty(message = "パスワードを入力してください")
//  private String password;
  /**
   * 住所
   */
  @Size(max = 255, message = "住所は255桁以内で入力して下さい。")
  private String address;
  /**
   * 電話番号
   */
//@Pattern(regexp = "0\\d{1,4}-\\d{1,4}-\\d{4}", message = "電話番号の形式で入力して下さい。(入力例：00-0000-0000 又は 090-0000-0000)")
  @Pattern(regexp = "\\d{0,11}", message = "電話番号は数値を入力して下さい。") // 未入力も可とする。ただし、MAXを超えると『数値を入力～』のメッセージになるのが微妙。
  @Size(max = 11, message = "電話番号は11桁以内で入力して下さい。")
  private String phone;
}