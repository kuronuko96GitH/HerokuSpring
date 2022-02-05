package com.example.demo.dto;

import java.io.Serializable;

import javax.validation.constraints.Pattern;

import lombok.Data;

/**
 * 勤退情報（検索条件用） リクエストデータ
 */
@Data
public class WorkRequestSearch implements Serializable {

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
   * 開始日時(年)
   * (任意入力。空白も可とする。)
   */
  @Pattern(regexp = "\\d{0,4}?$", message = "勤退開始日(年)に正しい日付を入力して下さい。")
  private String startDateY;

  /**
   * 開始日時(月)
   * (任意入力。空白も可とする。)
   */
  @Pattern(regexp = "\\d{0,2}?$", message = "勤退開始日(月)に正しい日付を入力して下さい。")
  private String startDateM;

  /**
   * 開始日時(年)
   * (任意入力。空白も可とする。)
   */
  @Pattern(regexp = "\\d{0,2}?$", message = "勤退開始日(日)に正しい日付を入力して下さい。")
  private String startDateD;

  /**
  //開始日時(時間)
   * (任意入力。空白も可とする。)
   */
  @Pattern(regexp = "\\d{0,2}?$", message = "開始日時(時)に正しい数値を入力して下さい。")
  private String startDateHH;

  /**
   * 開始日時(分)
   * (任意入力。空白も可とする。)
   */
  @Pattern(regexp = "\\d{0,2}?$", message = "開始日時(分)に正しい数値を入力して下さい。")
  private String startDateMI;



  /**
   * 終了日時(年)
   * (任意入力。空白も可とする。)
   */
  @Pattern(regexp = "\\d{0,4}?$", message = "勤退終了日(年)に正しい日付を入力して下さい。")
  private String endDateY;

  /**
   * 終了日時(月)
   * (任意入力。空白も可とする。)
   */
  @Pattern(regexp = "\\d{0,2}?$", message = "勤退終了日(月)に正しい日付を入力して下さい。")
  private String endDateM;

  /**
   * 終了日時(年)
   * (任意入力。空白も可とする。)
   */
  @Pattern(regexp = "\\d{0,2}?$", message = "勤退終了日(日)に正しい日付を入力して下さい。")
  private String endDateD;

  /**
   * 終了日時(時間)
   * (任意入力。空白も可とする。)
   */
  @Pattern(regexp = "\\d{0,2}?$", message = "終了日時(時)に正しい数値を入力して下さい。")
  private String endDateHH;

  /**
   * 終了日時(分)
   * (任意入力。空白も可とする。)
   */
  @Pattern(regexp = "\\d{0,2}?$", message = "終了日時(分)に正しい数値を入力して下さい。")
  private String endDateMI;
}