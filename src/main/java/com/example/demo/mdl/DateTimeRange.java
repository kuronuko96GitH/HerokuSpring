package com.example.demo.mdl;

import java.time.Duration;
import java.time.LocalDateTime;

public class DateTimeRange {

	public static final Integer INT_SHISYAGONYU = 0; // 四捨五入
	public static final Integer INT_KIRISUTE = 1;		// 切り捨て（例：労働時間の３０分未満は切り捨て）
	public static final Integer INT_KIRIAGE = 2;		// 切り上げ


	/**
	 * 日時の範囲チェック（開始日時と終了日時の範囲チェック）
	 * @param 開始日（「yyyy/MM/dd HH:mm:ss」形式を対象とする）
	 * @param 終了日（「yyyy/MM/dd HH:mm:ss」形式を対象とする）
	 * @return 結果（true：正常終了、false：異常終了）
	 */
	public static boolean isRangeChk(String strStartDateTime, String strEndDateTime) {

		boolean isResult = true; // true：正常終了

	    // 空白("")に置き替え用。
        String strDate = "";

	    //年・月・日
	    Integer intY;
	    Integer intM;
	    Integer intD;
	    Integer intHH;
	    Integer intMI;
	    Integer intSS;


	    // 開始日時の取得
	    strDate = strStartDateTime;
	    
	    //年・月・日・時・分を数値型にする。
	    intY = Integer.parseInt(strDate.substring(0,4));
	    intM = Integer.parseInt(strDate.substring(5,7));
	    intD = Integer.parseInt(strDate.substring(8,10));
	    intHH = Integer.parseInt(strDate.substring(11,13));
	    intMI = Integer.parseInt(strDate.substring(14,16));
	    intSS = Integer.parseInt(strDate.substring(17,19));

	    LocalDateTime dateStart = LocalDateTime.of(intY, intM, intD, intHH, intMI, intSS);


	    // 終了日時の取得
	    strDate = strEndDateTime;

	    //年・月・日・時・分を数値型にする。
	    intY = Integer.parseInt(strDate.substring(0,4));
	    intM = Integer.parseInt(strDate.substring(5,7));
	    intD = Integer.parseInt(strDate.substring(8,10));
	    intHH = Integer.parseInt(strDate.substring(11,13));
	    intMI = Integer.parseInt(strDate.substring(14,16));
	    intSS = Integer.parseInt(strDate.substring(17,19));

	    LocalDateTime dateEnd = LocalDateTime.of(intY, intM, intD, intHH, intMI, intSS);


		if (dateStart.isAfter(dateEnd)) {
			// 終了日が、開始日よりも過去になってるため、エラーとする。
			return false;
		}

	    return isResult;
	}


	/**
	 * 開始日時と終了日時までの時間数を取得。
	 * @param 開始日（「yyyy/MM/dd HH:mm:ss」形式を対象とする）
	 * @param 終了日（「yyyy/MM/dd HH:mm:ss」形式を対象とする）
	 * @param 切り捨て？　四捨五入？　呼び出し元から処理のパターンを選択させる。
	 * 			（↑未実装）
	 * @return double（小数点を含む時間数）
	 */
	public static double getRangeHours(String strStartDateTime, String strEndDateTime, Integer intStatus) {

		double dblHours = 0; // (小数点を含む、時間数を取得する。)

	    // 空白("")に置き替え用。
        String strDate = "";

	    //年・月・日
	    Integer intY;
	    Integer intM;
	    Integer intD;
	    Integer intHH;
	    Integer intMI;
//	    Integer intSS;

	    boolean isBreakTime = true; // お昼休憩の有無(true:お昼休憩あり、開始時間が13:00:00以降の場合)

	    // 開始日時の取得
	    strDate = strStartDateTime;

	    //年・月・日・時・分を数値型にする。
	    intY = Integer.parseInt(strDate.substring(0,4));
	    intM = Integer.parseInt(strDate.substring(5,7));
	    intD = Integer.parseInt(strDate.substring(8,10));
	    intHH = Integer.parseInt(strDate.substring(11,13));
	    intMI = Integer.parseInt(strDate.substring(14,16));
//	    intSS = Integer.parseInt(strDate.substring(17,19));

//	    LocalDateTime dateStart = LocalDateTime.of(intY, intM, intD, intHH, intMI, intSS);
	    LocalDateTime dateBegin = LocalDateTime.of(intY, intM, intD, intHH, intMI);// 開始日時

	    // 昼休憩の終了時間
	    LocalDateTime dateBreakTime = LocalDateTime.of(intY, intM, intD, 12, 59);

	    if (dateBegin.isAfter(dateBreakTime)) {
	    	// 開始時間がお昼休憩より後（１３時以降の出勤）で、
	    	// 午後出勤（午後からの労働）だった場合
	    	isBreakTime = false; // お昼休憩無し
	    }


	    // 終了日時の取得
	    strDate = strEndDateTime;

	    //年・月・日・時・分を数値型にする。
	    intY = Integer.parseInt(strDate.substring(0,4));
	    intM = Integer.parseInt(strDate.substring(5,7));
	    intD = Integer.parseInt(strDate.substring(8,10));
	    intHH = Integer.parseInt(strDate.substring(11,13));
	    intMI = Integer.parseInt(strDate.substring(14,16));
//	    intSS = Integer.parseInt(strDate.substring(17,19));

//	    LocalDateTime dateEnd = LocalDateTime.of(intY, intM, intD, intHH, intMI, intSS);
	    LocalDateTime dateEnd = LocalDateTime.of(intY, intM, intD, intHH, intMI);// 終了日時

	    // 昼休憩の開始時間
	    dateBreakTime = LocalDateTime.of(intY, intM, intD, 12, 01);

	    if (dateEnd.isBefore(dateBreakTime)) {
	    	// 終了時間が、お昼休憩の開始時間（１２時０１分）より前で、
	    	// 午前出勤のみ労働だった場合
	    	isBreakTime = false; // お昼休憩無し
	    }



	    // 期間分の時間を取得する
	    Duration summerVacationDuration = Duration.between(dateBegin, dateEnd);

	    // 労働時間数(時間)を取得
	    Long lngHours = summerVacationDuration.toMinutes() / 60;
	    // 労働時間数(分)を取得
	    Long lngMinutes = summerVacationDuration.toMinutes() % 60;


	    // 労働時間数(分)を、30分単位(0.5時間)で計算する。
	    double dblMin = (lngMinutes / 30) * 0.5;

	    if (isBreakTime) {
	    	// お昼休憩あり。

	    	// 労働時間数 ＝ 時間数 － 昼休憩の１時間 ＋ 分(0.5時間計算)
	    	dblHours = lngHours - 1 + dblMin;

	    	if (dblHours < 0) {
	    		// 計算後の時間数がマイナスの場合
	    		dblHours = 0; // ０時間を再設定。
	    	}

	    } else {
	    	// 午後出勤

	    	// 労働時間数 ＝ 時間数 ＋ 分(0.5時間計算)
	    	dblHours = lngHours + dblMin;
		}

	    return dblHours;
	}
}
