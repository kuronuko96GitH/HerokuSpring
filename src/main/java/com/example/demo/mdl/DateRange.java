package com.example.demo.mdl;

import java.time.LocalDate;

public class DateRange {

/**
	 * 日付の範囲チェック（開始日と終了日の範囲チェック）
	 * @param 開始日（「yyyyMMdd」「yyyy/MM/dd」「yyyy-MM-dd」形式を対象とする）
	 * @param 終了日（「yyyyMMdd」「yyyy/MM/dd」「yyyy-MM-dd」形式を対象とする）
	 * @return 結果（true：正常終了、false：異常終了）
	 */
	public static boolean isRangeChk(String strStartDate, String strEndDate) {

		boolean isResult = true; // true：正常終了

	    // 空白("")に置き替え用。
        String strDate = "";

	    //年・月・日
	    Integer intY;
	    Integer intM;
	    Integer intD;


	    // 開始日の取得
	    // "-"と"/"は、空白("")に置き替える。
	    strDate = strStartDate.replace("-", "").replace("/", "");

	    //年・月・日を数値型にする。
	    intY = Integer.parseInt(strDate.substring(0,4));
	    intM = Integer.parseInt(strDate.substring(4,6));
	    intD = Integer.parseInt(strDate.substring(6,8));

		LocalDate dateStart = LocalDate.of(intY, intM, intD);


	    // 終了日の取得
	    // "-"と"/"は、空白("")に置き替える。
	    strDate = strEndDate.replace("-", "").replace("/", "");

	    //年・月・日を数値型にする。
	    intY = Integer.parseInt(strDate.substring(0,4));
	    intM = Integer.parseInt(strDate.substring(4,6));
	    intD = Integer.parseInt(strDate.substring(6,8));

		LocalDate dateEnd = LocalDate.of(intY, intM, intD);


		if (dateStart.isAfter(dateEnd)) {
			// 終了日が、開始日よりも過去になってるため、エラーとする。
			return false;
		}

	    return isResult;
	}
}
