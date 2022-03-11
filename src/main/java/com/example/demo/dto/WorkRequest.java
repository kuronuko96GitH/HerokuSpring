package com.example.demo.dto;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Data;

/**
 * 勤退情報 リクエストデータ
 */
@Data
public class WorkRequest implements Serializable {
  /**
   * コンテンツ
   */
  @NotEmpty(message = "勤退内容を入力して下さい。")
  @Size(max = 100, message = "勤退内容は100桁以内で入力して下さい。")
  private String content;



  // 出勤日（年月日…yyyy-MM-dd）
//  @Pattern(regexp = "\\d{4}/\\d{2}/\\d{2} \\d{2}:\\d{2}", message = "出勤日は日付形式で入力して下さい。(入力例：2000/01/01 01:01)")
  @NotEmpty(message = "出勤日を入力して下さい。")
  private String startDateYMD;
/*【旧版】年月日のテキストボックス３つが別々ver
  // 出勤日(年)
//@Size(max = 11, message = "電話番号は11桁以内で入力して下さい。")
  @NotEmpty(message = "出勤日(年)を入力して下さい。")
  @Pattern(regexp = "\\d{4}", message = "出勤日(年)に正しい日付を入力して下さい。")
  private String startDateY;

  // 出勤日(月)
  @NotEmpty(message = "出勤日(月)を入力して下さい。")
  @Pattern(regexp = "\\d{1,2}", message = "出勤日(月)に正しい日付を入力して下さい。")
  private String startDateM;

  // 出勤日(日)
  @NotEmpty(message = "出勤日(日)を入力して下さい。")
  @Pattern(regexp = "\\d{1,2}", message = "出勤日(日)に正しい日付を入力して下さい。")
  private String startDateD;
*/
  // 出勤日(時間)
  @NotEmpty(message = "出勤日(時)を入力して下さい。")
  @Pattern(regexp = "\\d{1,2}", message = "出勤日(時)に正しい数値を入力して下さい。")
  private String startDateHH;

  // 出勤日(分)
  @NotEmpty(message = "出勤日(分)を入力して下さい。")
  @Pattern(regexp = "\\d{1,2}", message = "出勤日(分)に正しい数値を入力して下さい。")
  private String startDateMI;



  // 退勤日（年月日…yyyy-MM-dd）
//  @Pattern(regexp = "\\d{4}/\\d{2}/\\d{2} \\d{2}:\\d{2}", message = "退勤日は日付形式で入力して下さい。(入力例：2000/01/01 01:01)")
  @NotEmpty(message = "退勤日を入力して下さい。")
  private String endDateYMD;
/*【旧版】年月日のテキストボックス３つが別々ver
  // 退勤日(年)
  @NotEmpty(message = "退勤日(年)を入力して下さい。")
  @Pattern(regexp = "\\d{4}", message = "退勤日(年)に正しい日付を入力して下さい。")
  private String endDateY;

  // 退勤日(月)
  @NotEmpty(message = "退勤日(月)を入力して下さい。")
  @Pattern(regexp = "\\d{1,2}", message = "退勤日(月)に正しい日付を入力して下さい。")
  private String endDateM;

  // 退勤日(日)
  @NotEmpty(message = "退勤日(日)を入力して下さい。")
  @Pattern(regexp = "\\d{1,2}", message = "退勤日(日)に正しい日付を入力して下さい。")
  private String endDateD;
*/
  // 退勤日(時間)
  @NotEmpty(message = "退勤日(時)を入力して下さい。")
  @Pattern(regexp = "\\d{1,2}", message = "退勤日(時)に正しい数値を入力して下さい。")
  private String endDateHH;

  // 退勤日(分)
  @NotEmpty(message = "退勤日(分)を入力して下さい。")
  @Pattern(regexp = "\\d{1,2}", message = "退勤日(分)に正しい数値を入力して下さい。")
  private String endDateMI;
}