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
   * 性別
   */
  private String gender;
//  private Integer gender;
  /**
   * 住所
   */
  @Size(max = 255, message = "住所は255桁以内で入力して下さい。")
  private String address;
  /**
   * 電話番号
   */
//@Positive(message = "電話番号に正しい数値を入力して下さい。") // 整数チェック＆必須入力のチェックも同時にされます。
//@Pattern(regexp = "0\\d{1,4}-\\d{1,4}-\\d{4}", message = "電話番号の形式で入力して下さい。(入力例：00-0000-0000 又は 090-0000-0000)")
  @Pattern(regexp = "\\d{0,11}", message = "電話番号に正しい数値を入力して下さい。") // 未入力も可とする。
  @Size(max = 11, message = "電話番号は11桁以内で入力して下さい。")
  private String phone;

  /**
   * 生年月日(年)
   */
//  @NotEmpty(message = "生年月日(年)を入力して下さい。")
  @Pattern(regexp = "\\d{0,4}", message = "生年月日(年)に正しい日付を入力して下さい。")
  private String birthdayY;

  /**
   * 生年月日(月)
   */
//  @NotEmpty(message = "生年月日(月)を入力して下さい。")
  @Pattern(regexp = "\\d{0,2}", message = "生年月日(月)に正しい日付を入力して下さい。")
  private String birthdayM;

  /**
   * 生年月日(年)
   */
//  @NotEmpty(message = "生年月日(日)を入力して下さい。")
  @Pattern(regexp = "\\d{0,2}", message = "生年月日(日)に正しい日付を入力して下さい。")
  private String birthdayD;
  /**
   * 年齢
   */
  @Pattern(regexp = "\\d{0,3}", message = "年齢に正しい数値を入力して下さい。")
  private String age;
}