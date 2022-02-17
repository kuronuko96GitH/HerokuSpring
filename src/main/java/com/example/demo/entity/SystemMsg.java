package com.example.demo.entity;

public class SystemMsg {
/*
	// 『I-0XX』共通メッセージコード
	public static final String INFO_CODE_001 = "I-001";

	public static final String INFO_CODE_999 = "I-999";


	// 『I-0XX』共通メッセージ

	private static final String INFO_MSG_001 = "該当データがありません。";

	private static final String INFO_MSG_999 = "メッセージの取得に失敗しました。システム管理者にお問い合わせ願います。";




	// 『CI-0XX』共通メッセージコード
	public static final String C_INFO_CODE_001 = "CI-001";

	public static final String C_INFO_CODE_999 = "CI-999";


	// 『CI-0XX』共通メッセージ
	private static final String C_INFO_MSG_999 = "メッセージの取得に失敗しました。システム管理者にお問い合わせ願います。";
*/



	// 『E-0XX』共通エラーコード
	public static final String ERR_CODE_001 = "E-001";
	public static final String ERR_CODE_002 = "E-002";

	// 『E-1XX』ユーザー情報エラー
/*
	public static final String ERR_CODE_101 = "E-101";
	public static final String ERR_CODE_102 = "E-102";
	public static final String ERR_CODE_103 = "E-103";
	public static final String ERR_CODE_104 = "E-104";
	public static final String ERR_CODE_105 = "E-105";
	public static final String ERR_CODE_106 = "E-106";
*/
	// 『E-9XX』例外エラーコード
	public static final String ERR_CODE_999 = "E-999";



	// 『E-0XX』共通エラー
	private static final String ERR_MSG_001 = "タイムアウトにより、システム情報の取得に失敗しました。";
	private static final String ERR_MSG_002 = "タイムアウトにより、ユーザー情報の取得に失敗しました。";

	// 『E-1XX』ユーザー情報エラー
	//★★★現状、ユーザー情報と勤退情報のエラーメッセージに関しては、
	//わざわざクラス化するメリットを感じないため、保留とする。
/*
	private static final String ERR_MSG_101 = "管理者権限が無いため、編集はできません。";
	private static final String ERR_MSG_102 = "管理者権限が無いため、パスワードの編集はできません。";
	private static final String ERR_MSG_103 = "管理者権限が無いため、削除はできません。";
	private static final String ERR_MSG_104 = "既に登録済のメールアドレスです。";
	private static final String ERR_MSG_105 = "生年月日に正しい日付を入力して下さい。";
	private static final String ERR_MSG_106 = "ログイン中のユーザーは、削除できません。";

	// 『E-2XX』勤退情報エラー
	private static final String ERR_MSG_201 = "『勤退情報』XXXの取得に失敗しました。";
*/
	// 『E-9XX』例外エラー
	private static final String ERR_MSG_999 = "システムに例外エラーが発生しました。システム管理者にお問い合わせ願います。";



	public static String getErrMsg(String ErrMsgCode) {
		// 該当するエラーコードのエラーメッセージを取得する。
		String strMsg = "";

		if (ErrMsgCode.equals(ERR_CODE_001)) {
			strMsg = ERR_CODE_001 + " " + ERR_MSG_001;
		} else if (ErrMsgCode.equals(ERR_CODE_002)) {
			strMsg = ERR_CODE_002 + " " + ERR_MSG_002;
/*
		} else if (ErrMsgCode.equals(ERR_CODE_101)) {
			// "管理者権限が無いため、編集はできません。";
			strMsg = ERR_MSG_101;
		} else if (ErrMsgCode.equals(ERR_CODE_102)) {
			// "管理者権限が無いため、パスワードの編集はできません。";
			strMsg = ERR_MSG_102;
		} else if (ErrMsgCode.equals(ERR_CODE_103)) {
			// "管理者権限が無いため、削除はできません。";
			strMsg = ERR_MSG_103;
		} else if (ErrMsgCode.equals(ERR_CODE_104)) {
			// "既に登録済のメールアドレスです。"
			strMsg = ERR_MSG_104;
		} else if (ErrMsgCode.equals(ERR_CODE_105)) {
			// "生年月日に正しい日付を入力して下さい。"
			strMsg = ERR_MSG_105;
		} else if (ErrMsgCode.equals(ERR_CODE_106)) {
			// "ログイン中のユーザーは、削除できません。"
			strMsg = ERR_MSG_106;
*/
		} else {
			strMsg = ERR_CODE_999 + " " + ERR_MSG_999;
		}
		return strMsg;
	}

/*
	public static String getInfoMsg(String InfoMsgCode) {
		// 該当するInfoコードのエラーメッセージを取得する。
		String strMsg = "";

		if (InfoMsgCode.equals(INFO_CODE_001)) {
			// 検索ボタンを押した時の『該当データがありません。』メッセージを取得。
			strMsg = INFO_MSG_001;
		} else {
			strMsg = INFO_CODE_999 + " " + INFO_MSG_999;
		}
		return strMsg;
	}


	public static String getCustomInfoMsg(String InfoMsgCode, String strValue) {
		// 該当するInfoコードのエラーメッセージを取得する。
		String strMsg = "";

		if (InfoMsgCode.equals(C_INFO_CODE_001)) {
			// "【検索結果】条件に該当するデータは" + XXX + "件です。"
			strMsg = "【検索結果】条件に該当するデータは" + strValue + "件です。";
		} else {
			strMsg = C_INFO_CODE_999 + " " + C_INFO_MSG_999;
		}
		return strMsg;
	}
*/
}