package com.example.demo.dto;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Data;

/**
 * 勤怠情報 リクエストデータ
 */
@Data
public class WorkRequest implements Serializable {
  /**
   * コンテンツ
   */
  @NotEmpty(message = "勤怠内容を入力して下さい。")
  @Size(max = 100, message = "勤怠内容は100桁以内で入力して下さい。")
  private String content;


/*
  // 開始日時
//  @Size(max = 11, message = "電話番号は11桁以内で入力して下さい。")
  @Pattern(regexp = "\\d{4}/\\d{2}/\\d{2} \\d{2}:\\d{2}", message = "開始日時は日付形式で入力して下さい。(入力例：2000/01/01 01:01)")
  @NotEmpty(message = "開始日時を入力して下さい。")
  private String startDate;
*/
  /**
   * 開始日時(年)
   */
  @NotEmpty(message = "開始日時(年)を入力して下さい。")
  @Pattern(regexp = "\\d{4}", message = "開始日時(年)に正しい日付を入力して下さい。")
  private String startDateY;

  /**
   * 開始日時(月)
   */
  @NotEmpty(message = "開始日時(月)を入力して下さい。")
  @Pattern(regexp = "\\d{1,2}", message = "開始日時(月)に正しい日付を入力して下さい。")
  private String startDateM;

  /**
   * 開始日時(年)
   */
  @NotEmpty(message = "開始日時(日)を入力して下さい。")
  @Pattern(regexp = "\\d{1,2}", message = "開始日時(日)に正しい日付を入力して下さい。")
  private String startDateD;

  /**
  //開始日時(時間)
   */
  @NotEmpty(message = "開始日時(時)を入力して下さい。")
  @Pattern(regexp = "\\d{1,2}", message = "開始日時(時)は数値で入力して下さい。")
  private String startDateHH;

  /**
   * 開始日時(分)
   */
  @NotEmpty(message = "開始日時(分)を入力して下さい。")
  @Pattern(regexp = "\\d{1,2}", message = "開始日時(分)は数値で入力して下さい。")
  private String startDateMI;



/*
  // 終了日時
  @Pattern(regexp = "\\d{4}/\\d{2}/\\d{2} \\d{2}:\\d{2}", message = "終了日時は日付形式で入力して下さい。(入力例：2000/01/01 01:01)")
  @NotEmpty(message = "終了日時を入力して下さい。")
  private String endDate;
*/
  /**
   * 終了日時(年)
   */
  @NotEmpty(message = "終了日時(年)を入力して下さい。")
  @Pattern(regexp = "\\d{4}", message = "終了日時(年)に正しい日付を入力して下さい。")
  private String endDateY;

  /**
   * 終了日時(月)
   */
  @NotEmpty(message = "終了日時(月)を入力して下さい。")
  @Pattern(regexp = "\\d{1,2}", message = "終了日時(月)に正しい日付を入力して下さい。")
  private String endDateM;

  /**
   * 終了日時(年)
   */
  @NotEmpty(message = "終了日時(日)を入力して下さい。")
  @Pattern(regexp = "\\d{1,2}", message = "終了日時(日)に正しい日付を入力して下さい。")
  private String endDateD;

  /**
   * 終了日時(時間)
   */
  @NotEmpty(message = "終了日時(時)を入力して下さい。")
  @Pattern(regexp = "\\d{1,2}", message = "終了日時(時)は数値で入力して下さい。")
  private String endDateHH;

  /**
   * 終了日時(分)
   */
  @NotEmpty(message = "終了日時(分)を入力して下さい。")
  @Pattern(regexp = "\\d{1,2}", message = "終了日時(分)は数値で入力して下さい。")
  private String endDateMI;

}