package com.example.demo.mdl;

import java.util.Calendar;

public class DateEdit {

	/**
	 * 指定した日付文字列（yyyy/MM/dd or yyyy-MM-dd）
	 * における月末日付を返します。
	 * 
	 * @param strDate 対象の日付文字列（引数例："2008/02/01"
	 * @return 月末日付					⇒戻り値：29）
	 */
	public static int getLastDay(String strDate) {

	    if (strDate == null || strDate.length() != 10) {
	        throw new IllegalArgumentException(
	                "引数の文字列["+ strDate +"]" +
	                "は不正です。");
	    }

	    int yyyy = Integer.parseInt(strDate.substring(0,4));
	    int MM = Integer.parseInt(strDate.substring(5,7));
	    int dd = Integer.parseInt(strDate.substring(8,10));
	    Calendar cal = Calendar.getInstance();
	    cal.set(yyyy,MM-1,dd);
	    int last = cal.getActualMaximum(Calendar.DATE);
	    return last;
	}

	/**
	 * 指定した日付文字列（yyyy/MM/dd or yyyy-MM-dd）
	 * における月末日付を返します。
	 * 
	 * @param strDate 対象の日付文字列（引数例："2008/02/01"
	 * @return 月末日付					⇒戻り値："29"）
	 */
	public static String getLastDayStr(String strDate) {

	    if (strDate == null || strDate.length() != 10) {
	        throw new IllegalArgumentException(
	                "引数の文字列["+ strDate +"]" +
	                "は不正です。");
	    }

	    int yyyy = Integer.parseInt(strDate.substring(0,4));
	    int MM = Integer.parseInt(strDate.substring(5,7));
	    int dd = Integer.parseInt(strDate.substring(8,10));
	    Calendar cal = Calendar.getInstance();
	    cal.set(yyyy,MM-1,dd);
	    int last = cal.getActualMaximum(Calendar.DATE);

	    return String.format("%02d", last); // ゼロ埋め処理はしてますが、末日で一桁は無いです。

/* デバッグ例:呼び出し元のクラスが『SampleClass』とした場合
	    System.out.println("SampleClass:" + DateEdit.getLastDay("2007/01/01")); // 実行結果："31"
	    System.out.println("SampleClass:" + DateEdit.getLastDay("2007/02/01")); // 実行結果："28"
	    System.out.println("SampleClass:" + DateEdit.getLastDay("2008/02/01")); // 実行結果："29"
*/
	}
}