package com.example.demo.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * ユーザー情報更新リクエストデータ
 * 補足説明：このクラスは、UserRequestクラスを継承して作成しています。
 * ※このクラスは、チェック処理の対象項目が、新規登録画面と全て一致する場合のみ使えるクラスです。
 * 例えば、更新画面ではパスワードのみは別画面でやりたいとなった場合は、このクラスは使えません。
 *
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class WorkUpdateRequest extends WorkRequest implements Serializable {

  /**
   * WorkID
   */
  @NotNull
  private Long id;
}