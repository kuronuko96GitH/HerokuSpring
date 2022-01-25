package com.example.demo.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * ユーザー情報更新リクエストデータ
 * 補足説明：このクラスは、UserRequestクラスを継承して作成しています。
 *
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class UserUpdateRequest extends UserRequest implements Serializable {

  /**
   * ユーザーID
   */
  @NotNull
  private Long id;
}