package com.example.demo.mdl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.Calendar;
import java.util.Date;

import com.example.demo.entity.SystemInfo;

public class DateEdit {

	/**
	 * 日付チェック
	 * @param value 検証対象の値（「yyyyMMdd」「yyyy/MM/dd」「yyyy-MM-dd」形式を対象とする）
	 * @return 結果（true：日付、false：日付ではない）
	 */
	public static boolean isDate(String value) {
	    boolean result = false;
	    if (value != null) {
	        try {
			    // "-"と"/"は、空白("")に置き替える。
	            String strChkDate = value.replace("-", "").replace("/", "");

	            // 日付チェック
	            DateTimeFormatter.ofPattern("uuuuMMdd").withResolverStyle(ResolverStyle.STRICT).parse(strChkDate, LocalDate::from);
	            // withResolverStyle(ResolverStyle.STRICT)を指定することで、有効な日付（存在する日付）のみチェックOKとしています。
	            // ※withResolverStyle(ResolverStyle.STRICT)を指定しないと、「2021-02-29」のような存在しない日付は「2021-02-28」に勝手にパースされてしまうので要注意です。
	            // また、DateTimeFormatterでは「yyyy」ではなく「uuuu」で指定します。

	            result = true;
	        } catch (Exception e) {
	            result = false;
	        }
	    }
	    return result;
	}

	/**
	 * 文字列の『年』『月』『日』が、正しい日付かをチェックする。
	 * @param 文字列の『年』『月』『日』
	 * @return 結果（true：日付、false：日付ではない）
	 */
	public static boolean isDate(String strDateY, String strDateM, String strDateD) {

		boolean result = false;
	    boolean isNumeric = false; // 数値チェック用

		// 入力チェック(年)
		if (strDateY == null || strDateY.isEmpty()) {
			return false;
		}
		// 数値チェック(年)
		isNumeric =  strDateY.matches("[+-]?\\d*(\\.\\d+)?");
		if (!isNumeric) {
			return false;
		}
		// 年数の有効範囲チェック
		if (Integer.parseInt(strDateY) < SystemInfo.getVALID_YEAR()) {
			// ※今回のプロジェクトにおいては、1900年以上を有効な日付とします。
			//　（生年月日などの日付チェックで、１００歳を超える会員登録はありえないと判断。）
			return false;
		}
		// 入力チェック(月)
		if (strDateM == null || strDateM.isEmpty()) {
			return false;
		}
		// 数値チェック(月)
		isNumeric =  strDateM.matches("[+-]?\\d*(\\.\\d+)?");
		if (!isNumeric) {
			return false;
		}
		// 入力チェック(日)
		if (strDateD == null || strDateD.isEmpty()) {
			return false;
		}
		// 数値チェック(日)
		isNumeric =  strDateD.matches("[+-]?\\d*(\\.\\d+)?");
		if (!isNumeric) {
			return false;
		}


        try {
    	    String strChkDate =  String.format("%04d", Integer.parseInt(strDateY))
					+ String.format("%02d", Integer.parseInt(strDateM))
					+ String.format("%02d", Integer.parseInt(strDateD));
            
            // 日付チェック
            DateTimeFormatter.ofPattern("uuuuMMdd").withResolverStyle(ResolverStyle.STRICT).parse(strChkDate, LocalDate::from);
            // withResolverStyle(ResolverStyle.STRICT)を指定することで、有効な日付（存在する日付）のみチェックOKとしています。
            // ※withResolverStyle(ResolverStyle.STRICT)を指定しないと、「2021-02-29」のような存在しない日付は「2021-02-28」に勝手にパースされてしまうので要注意です。
            // また、DateTimeFormatterでは「yyyy」ではなく「uuuu」で指定します。

            result = true;
        } catch (Exception e) {
            result = false;
        }

	    return result;
	}

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

		SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

		Date dateValue = null;
		
		try {
			dateValue = sdFormat.parse(strDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}

	    return dateValue;
	}

	/**
	 * 文字列の『年』『月』『日』を、指定された書式のString型で返します。
	 * 
	 * @param 文字列の『年』『月』『日』
	 * @param strFormat…書式の指定
	 * @return String型の戻り値
	 */
	public static String getFormatDate(String strDateY, String strDateM, String strDateD, String strFormat) {

		Date dateValue = null;

	    String strDate =  String.format("%04d", Integer.parseInt(strDateY)) + "/"
				+ String.format("%02d", Integer.parseInt(strDateM)) + "/"
				+ String.format("%02d", Integer.parseInt(strDateD)) + " 00:00:00";

		SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");


		try {
			dateValue = sdFormat.parse(strDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}

	    // 引数で指定された書式の文字列を取得
	    strDate = new SimpleDateFormat(strFormat).format(dateValue);

	    return strDate;
	}

	/**
	 * 文字列の『年月日(yyyy-MM-dd or yyyy/MM/dd)』『時』『分』『秒』を、Date型で返します。
	 * 
	 * @param 文字列の『年』『月』『日』『時』『分』『秒』
	 * @return Date型の戻り値
	 */
	public static Date getDateYMDTime(String strDateYMD, String strTimeH, String strTimeM, String strTimeS) {

	    // "-"は、"/"に置き替える。
        String strReplaceDate = strDateYMD.replace("-", "/");

	    String strDate =  strReplaceDate + " "
	    				+ String.format("%02d", Integer.parseInt(strTimeH)) + ":"
	    				+ String.format("%02d", Integer.parseInt(strTimeM)) + ":"
	    				+ String.format("%02d", Integer.parseInt(strTimeS));


		SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

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


		SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

		Date dateValue = null;
		
		try {
			dateValue = sdFormat.parse(strDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}

	    return dateValue;
	}

	/**
	 * 文字列の『年』『月』『日』『時』『分』『秒』を、指定された書式のString型で返します。
	 * 
	 * @param 文字列の『年』『月』『日』『時』『分』『秒』
	 * @param strFormat…書式の指定
	 * @return String型の戻り値
	 */
	public static String getFormatDateTime(String strDateY, String strDateM, String strDateD, String strTimeH, String strTimeM, String strTimeS, String strFormat) {

		Date dateValue = null;
	    String strDate =  String.format("%04d", Integer.parseInt(strDateY)) + "/"
				+ String.format("%02d", Integer.parseInt(strDateM)) + "/"
				+ String.format("%02d", Integer.parseInt(strDateD)) + " "
				+ String.format("%02d", Integer.parseInt(strTimeH)) + ":"
				+ String.format("%02d", Integer.parseInt(strTimeM)) + ":"
				+ String.format("%02d", Integer.parseInt(strTimeS));

		SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");


		try {
			dateValue = sdFormat.parse(strDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}

	    // 引数で指定された書式の文字列を取得
	    strDate = new SimpleDateFormat(strFormat).format(dateValue);

	    return strDate;
	}


	/**
	 * 文字列の『年月日(yyyy-MM-dd or yyyy/MM/dd)』『時』『分』『秒』を、指定された書式のString型で返します。
	 * 
	 * @param 文字列の『年月日(yyyy-MM-dd or yyyy/MM/dd)』『時』『分』『秒』
	 * @param strFormat…書式の指定
	 * @return String型の戻り値
	 */
	public static String getFormatDateYMDTime(String strDateYMD, String strTimeH, String strTimeM, String strTimeS, String strFormat) {

	    // "-"は、"/"に置き替える。
        String strReplaceDate = strDateYMD.replace("-", "/");
 
		Date dateValue = null;
	    String strDate =  strReplaceDate + " "
				+ String.format("%02d", Integer.parseInt(strTimeH)) + ":"
				+ String.format("%02d", Integer.parseInt(strTimeM)) + ":"
				+ String.format("%02d", Integer.parseInt(strTimeS));

		SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");


		try {
			dateValue = sdFormat.parse(strDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}

	    // 引数で指定された書式の文字列を取得
	    strDate = new SimpleDateFormat(strFormat).format(dateValue);

	    return strDate;
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
	    
	    String strDate =  strDateYM + "/" + strLastDay	+ " 23:59:59";


		SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

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



	/**
	 * 指定された日付の曜日を取得します。
	 * 
	 * @param 検証対象の値（「yyyyMMdd」「yyyy/MM/dd」「yyyy-MM-dd」形式を対象とする）
	 * @return 曜日
	 */
	public static String getYoubi(String strCheckDate) {

		try{
		    //曜日
		    String strYoubi[] = {"日","月","火","水","木","金","土"};

		    // "-"と"/"は、空白("")に置き替える。
            String strdate = strCheckDate.replace("-", "").replace("/", "");

		    //日付チェック
		    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		    sdf.setLenient(false);
		    sdf.parse(strdate);
	
		    //年・月を取得する
		    Integer intY = Integer.parseInt(strdate.substring(0,4));
		    Integer intM = Integer.parseInt(strdate.substring(4,6))-1;
		    Integer intD = Integer.parseInt(strdate.substring(6,8));
	
		    //取得した年月の最終年月日を取得する
		    Calendar cal = Calendar.getInstance();
		    cal.set(intY, intM, intD);
	
		    //YYYYMMDD形式にして変換して返す
		    return strYoubi[cal.get(Calendar.DAY_OF_WEEK)-1];
	
		} catch (Exception ex){
		    return null;
		}
	}
}