package com.example.demo.mdl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class DateEdit {

	/**
	 * 指定した日付文字列（yyyy/MM/dd or yyyy-MM-dd）
	 * における月末日付を返します。
	 * 
	 * @param strDate 対象の日付文字列（引数例："2008/02/01"
	 * @return 月末の日付					⇒戻り値："29"）
	 */
	public static String getLastDay(String strDate) {

	    if (strDate == null || strDate.length() != 10) {
	        throw new IllegalArgumentException(
	                "引数の文字列["+ strDate +"]" +
	                "は不正です。");
	    }

	    Integer yyyy = Integer.parseInt(strDate.substring(0,4));
	    Integer MM = Integer.parseInt(strDate.substring(5,7));
	    Integer dd = Integer.parseInt(strDate.substring(8,10));
	    Calendar cal = Calendar.getInstance();
	    cal.set(yyyy,MM-1,dd);
	    Integer intLast = cal.getActualMaximum(Calendar.DATE);

	    return String.format("%02d", intLast); // ゼロ埋め処理はしてますが、末日で一桁は無いです。

/* デバッグ例:呼び出し元のクラスが『SampleClass』とした場合
	    System.out.println("SampleClass:" + DateEdit.getLastDay("2007/01/01")); // 実行結果："31"
	    System.out.println("SampleClass:" + DateEdit.getLastDay("2007/02/01")); // 実行結果："28"
	    System.out.println("SampleClass:" + DateEdit.getLastDay("2008/02/01")); // 実行結果："29"
*/
	}



	/**
	 * 文字列の『年』『月』『日』を、Date型で返します。
	 * 
	 * @param 文字列の『年』『月』『日』
	 * @return Date型の戻り値
	 */
	public static Date getDate(String strDateY, String strDateM, String strDateD) {

	    String strDate =  String.format("%04d", Integer.parseInt(strDateY)) + "/"
						+ String.format("%02d", Integer.parseInt(strDateM)) + "/"
						+ String.format("%02d", Integer.parseInt(strDateD)) + " 00:00:00";

		SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");

		Date dateValue = null;
		
		try {
			dateValue = sdFormat.parse(strDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}

	    return dateValue;
	}


	/**
	 * 文字列の『年』『月』『日』『時』『分』『秒』を、Date型で返します。
	 * 
	 * @param 文字列の『年』『月』『日』『時』『分』『秒』
	 * @return Date型の戻り値
	 */
	public static Date getDateTime(String strDateY, String strDateM, String strDateD, String strTimeH, String strTimeM, String strTimeS) {

	    String strDate =  String.format("%04d", Integer.parseInt(strDateY)) + "/"
						+ String.format("%02d", Integer.parseInt(strDateM)) + "/"
						+ String.format("%02d", Integer.parseInt(strDateD)) + " "
	    				+ String.format("%02d", Integer.parseInt(strTimeH)) + ":"
	    				+ String.format("%02d", Integer.parseInt(strTimeM)) + ":"
	    				+ String.format("%02d", Integer.parseInt(strTimeS));


		SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");

		Date dateValue = null;
		
		try {
			dateValue = sdFormat.parse(strDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}

	    return dateValue;
	}


	/**
	 * 文字列の『年』『月』を、該当月の末日に変換し、Date型で返します。
	 * 
	 * @param 文字列の『年』『月』
	 * @return Date型の戻り値（末日）
	 */
	public static Date getDateLastDay(String strDateY, String strDateM) {

	    String strDateYM = String.format("%04d", Integer.parseInt(strDateY)) + "/" + String.format("%02d", Integer.parseInt(strDateM));
	    String strLastDay = DateEdit.getLastDay(strDateYM + "/01");
	    
	    String strDate =  strDateYM + "/" + strLastDay	+ " 00:00:00";


		SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");

		Date dateValue = null;
		
		try {
			dateValue = sdFormat.parse(strDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}

	    return dateValue;
	}



	/**
	 * 指定された書式で、システム日付を取得
	 * 
	 * @param strFormat…書式の指定
	 * @return 指定書式のシステム日付
	 */
	public static String getSysDate(String strFormat) {

		// 現在の日時を取得
		LocalDateTime dateNow = LocalDateTime.now();
//		System.out.println(dateNow); // Date型…2022-02-03T13:31:31.667928

		// 書式を指定
		DateTimeFormatter datetimeformatter = DateTimeFormatter.ofPattern(strFormat);
		// 使用可能な書式例
		// "yyyy/MM/dd HH:mm:ss"
		// "yyyy/MM/dd"
		// "yyyy年MM月dd日"

		String strDate = datetimeformatter.format(dateNow);
/*
		// "yyyy年M月d日"(※format関数にて、ゼロ埋めは消せるので、この処理は不要です)
		if (strFormat.equals("yyyy年M月d")) {
		    Integer intMM = Integer.parseInt(strDate.substring(5,7));
		    Integer intdd = Integer.parseInt(strDate.substring(8,10));
		    
		    strDate = strDate.substring(0,4) + intMM.toString() + intdd.toString();
		}
*/
		return strDate;
	}




	/**
	 * 指定された書式で、(String型)日付を取得
	 * 
	 * @param dateValue…Date型（yyyy-MM-dd HH:mm:ss　のような、時刻までセットのDate型を想定）
	 *			strFormat…書式の指定例
	 *					『年月日』のみを取得："yyyy/MM/dd"
	 *					『年月日』のみを取得："yyyy年MM月dd日"
	 *
	 *					『年』のみを取得："yyyy"
	 *					『月』のみを取得："MM"…十の位を０埋めで取得、"M"…０埋めはしない
	 *					『日』のみを取得："dd"…十の位を０埋めで取得、"d"…０埋めはしない
	 *
	 *					『時』のみを取得："HH"…十の位を０埋めで取得、"H"…０埋めはしない
	 *					『分』のみを取得："mm"…十の位を０埋めで取得、"m"…０埋めはしない
	 *					『日』のみを取得："ss"…十の位を０埋めで取得、"s"…０埋めはしない
	 * @return 指定書式のシステム日付
	 */
	public static String getDate(Date dateValue, String strFormat) {

	    // デバッグ用
//	    String strDebug = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(dateValue);

	    // 引数で指定された書式の文字列を取得
	    String strDate = new SimpleDateFormat(strFormat).format(dateValue);

		return strDate;
	}
}