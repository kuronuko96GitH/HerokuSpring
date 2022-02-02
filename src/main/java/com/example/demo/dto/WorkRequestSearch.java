package com.example.demo.dto;

import java.io.Serializable;

import javax.validation.constraints.Pattern;

import lombok.Data;

/**
 * 勤怠情報（検索条件用） リクエストデータ
 * 		★2022/02/02時点　作成はしましたが、必須ではなく任意入力をしたかったので、
 * 		このクラスを入力チェックとして呼び出してるクラスはありません。
 */
@Data
public class WorkRequestSearch implements Serializable {
  /**
   * コンテンツ
   */
//  @NotEmpty(message = "勤怠内容を入力して下さい。")
//  @Size(max = 100, message = "勤怠内容は100桁以内で入力して下さい。")
//  private String content;

  /**
   * 検索年月（年）
   */
//  @NotEmpty(message = "勤怠年月(年)を入力して下さい。")
  // @NotNull…任意入力(Nullはエラーだが、""はOKとする。)
//  @NotNull(message = "勤怠年月(年)を入力して下さい。")
  @Pattern(regexp = "\\d{0,4}?$", message = "勤怠年月(年)に正しい日付を入力して下さい。")
  private String searchDateY;

  /**
   * 検索年月（月）
   */
//  @NotEmpty(message = "勤怠年月(月)を入力して下さい。")
//  @NotNull(message = "勤怠年月(月)を入力して下さい。")
  @Pattern(regexp = "\\d{0,2}?$", message = "勤怠年月(月)に正しい日付を入力して下さい。")
  private String searchDateM;

 
  /**
   * 開始日時(年)
   */
//  @NotNull(message = "開始日時(年)を入力して下さい。")
  @Pattern(regexp = "\\d{0,4}?$", message = "勤怠開始日(年)に正しい日付を入力して下さい。")
  private String startDateY;

  /**
   * 開始日時(月)
   */
//  @NotNull(message = "開始日時(月)を入力して下さい。")
  @Pattern(regexp = "\\d{0,2}?$", message = "勤怠開始日(月)に正しい日付を入力して下さい。")
  private String startDateM;

  /**
   * 開始日時(年)
   */
//  @NotNull(message = "開始日時(日)を入力して下さい。")
  @Pattern(regexp = "\\d{0,2}?$", message = "勤怠開始日(日)に正しい日付を入力して下さい。")
  private String startDateD;

  /**
  //開始日時(時間)
   */
//  @NotNull(message = "開始日時(時)を入力して下さい。")
  @Pattern(regexp = "\\d{0,2}?$", message = "開始日時(時)は数値で入力して下さい。")
  private String startDateHH;

  /**
   * 開始日時(分)
   */
//  @NotNull(message = "開始日時(分)を入力して下さい。")
  @Pattern(regexp = "\\d{0,2}?$", message = "開始日時(分)は数値で入力して下さい。")
  private String startDateMI;



  /**
   * 終了日時(年)
   */
//  @NotNull(message = "勤怠終了日(年)を入力して下さい。")
  @Pattern(regexp = "\\d{0,4}?$", message = "勤怠終了日(年)に正しい日付を入力して下さい。")
  private String endDateY;

  /**
   * 終了日時(月)
   */
//  @NotNull(message = "勤怠終了日(月)を入力して下さい。")
  @Pattern(regexp = "\\d{0,2}?$", message = "勤怠終了日(月)に正しい日付を入力して下さい。")
  private String endDateM;

  /**
   * 終了日時(年)
   */
//  @NotNull(message = "勤怠終了日(日)を入力して下さい。")
  @Pattern(regexp = "\\d{0,2}?$", message = "勤怠終了日(日)に正しい日付を入力して下さい。")
  private String endDateD;

  /**
   * 終了日時(時間)
   */
//  @NotNull(message = "終了日時日(時)を入力して下さい。")
  @Pattern(regexp = "\\d{0,2}?$", message = "終了日時(時)は数値で入力して下さい。")
  private String endDateHH;

  /**
   * 終了日時(分)
   */
//  @NotNull(message = "終了日時(分)を入力して下さい。")
  @Pattern(regexp = "\\d{0,2}?$", message = "終了日時(分)は数値で入力して下さい。")
  private String endDateMI;

}