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
  @NotEmpty(message = "勤務地情報を入力してください")
  @Size(max = 100, message = "勤務地情報は100桁以内で入力して下さい。")
  private String content;
  /**
   * 開始日時
   */
//  @Size(max = 11, message = "電話番号は11桁以内で入力して下さい。")
  @Pattern(regexp = "\\d{4}/\\d{2}/\\d{2} \\d{2}:\\d{2}", message = "開始日時は日付形式で入力して下さい。(入力例：2000/01/01 01:01)")
  @NotEmpty(message = "開始日時を入力してください")
  private String startDate;
  /**
   * 終了日時
   */
  @Pattern(regexp = "\\d{4}/\\d{2}/\\d{2} \\d{2}:\\d{2}", message = "終了日時は日付形式で入力して下さい。(入力例：2000/01/01 01:01)")
  @NotEmpty(message = "終了日時を入力してください")
  private String endDate;

}