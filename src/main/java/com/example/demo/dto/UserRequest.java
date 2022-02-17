package com.example.demo.dto;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.Data;

/**
 * ユーザー情報 リクエストデータ
 */
@Data
public class UserRequest implements Serializable {
  /**
   * 名前
   */
  @NotEmpty(message = "名前を入力して下さい。")
  @Size(max = 100, message = "名前は100桁以内で入力して下さい。")
  private String name;
  /**
   * メールアドレス
   */
  @NotEmpty(message = "メールアドレスを入力して下さい。")
  @Size(max = 255, message = "メールアドレスは255桁以内で入力して下さい。")
  private String email;
  /**
   * パスワード
   */
  @NotEmpty(message = "パスワードを入力して下さい。")
  @Size(min = 8, max = 20, message = "パスワードは８桁以上、２０桁以内で入力して下さい。")
  private String password;
/*
  // 住所
  @Size(max = 255, message = "住所は255桁以内で入力して下さい。")
  private String address;
  // 電話番号
//@Pattern(regexp = "0\\d{1,4}-\\d{1,4}-\\d{4}", message = "電話番号の形式で入力して下さい。(入力例：00-0000-0000 又は 090-0000-0000)")
  @Pattern(regexp = "\\d{0,11}", message = "電話番号に正しい数値を入力して下さい。") // 未入力も可とする。
  @Size(max = 11, message = "電話番号は11桁以内で入力して下さい。")
//  @Positive(message = "電話番号は数値を入力して下さい。") // 必須入力のチェックも同時にされます。
  private String phone;
*/
}