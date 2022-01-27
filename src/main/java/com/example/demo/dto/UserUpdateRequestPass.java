package com.example.demo.dto;

import java.io.Serializable;

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
  @NotNull
  private Long id;

  /**
   * パスワード
   */
  @NotEmpty(message = "パスワードを入力してください")
  @Size(max = 100, message = "パスワードは100桁以内で入力して下さい。")
  private String password;
}