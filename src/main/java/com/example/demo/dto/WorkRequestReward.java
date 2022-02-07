package com.example.demo.dto;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Data;

/**
 * 勤退情報（報酬計算用） リクエストデータ
 */
@Data
public class WorkRequestReward implements Serializable {

  /**
   * 検索年月（年）
   * (任意入力。空白も可とする。)
   */
  @Pattern(regexp = "\\d{0,4}?$", message = "勤退年月(年)に正しい日付を入力して下さい。")
  private String searchDateY;

  /**
   * 検索年月（月）
   * (任意入力。空白も可とする。)
   */
  @Pattern(regexp = "\\d{0,2}?$", message = "勤退年月(月)に正しい日付を入力して下さい。")
  private String searchDateM;

  /**
   * 時間単価
   */
  @NotEmpty(message = "時間単価を入力して下さい。")
  @Pattern(regexp = "\\d{1,4}", message = "時間単価に正しい数値を入力して下さい。")
  @Size(max = 4, message = "時間単価は4桁以内で入力して下さい。")
  private String tanka;

  /**
   * 紹介手数料(％：割合)
   */
  @NotEmpty(message = "紹介手数料(％)を入力して下さい。")
  @Pattern(regexp = "\\d{1,2}", message = "紹介手数料(％)に正しい数値を入力して下さい。")
  @Size(max = 2, message = "紹介手数料(％)は2桁以内で入力して下さい。")
  private String margin;

  /**
   * 合計労働時間数
   */
  private String workSumHours; 

  /**
   * 報酬金額
   */
  private String sumReward;
}