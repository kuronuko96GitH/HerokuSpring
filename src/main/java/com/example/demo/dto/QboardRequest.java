package com.example.demo.dto;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.Data;

/**
 * 質問板情報 リクエストデータ
 */
@Data
public class QboardRequest implements Serializable {

  /**
   * ヘッダID
   */
  private Integer headId; // 入力チェックの対象外

  /**
   * 投稿名
   */
  @NotEmpty(message = "投稿名を入力して下さい。")
  @Size(max = 100, message = "投稿名は100桁以内で入力して下さい。")
  private String name;
  /**
   * コンテンツ
   */
  @NotEmpty(message = "投稿内容を入力して下さい。")
  @Size(max = 255, message = "投稿内容は255桁以内で入力して下さい。")
  private String content;

}