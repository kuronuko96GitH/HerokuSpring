package com.example.demo.entity;

public class SystemErr {

	public static final String ERR_CODE_001 = "E-001";

	public static final String ERR_CODE_002 = "E-002";



	private static final String ERR_MSG_001 = "E-001 タイムアウトにより、システム情報の取得に失敗しました。";

	private static final String ERR_MSG_002 = "E-002 タイムアウトにより、ユーザー情報の取得に失敗しました。";

	private static final String ERR_MSG_999 = "E-999 システムに例外エラーが発生しました。";

	public static String getErrMsg(String ErrCode) {
		// 該当するエラーコードのエラーメッセージを取得する。
		String strMsg = "";

		if (ErrCode == ERR_CODE_001) {
			strMsg = ERR_MSG_001;
		} else if (ErrCode == ERR_CODE_002) {
			strMsg = ERR_MSG_002;
		} else {
			strMsg = ERR_MSG_999;
		}
		return strMsg;
	}
}