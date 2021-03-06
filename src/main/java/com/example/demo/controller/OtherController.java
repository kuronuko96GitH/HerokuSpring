package com.example.demo.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.auth.AuthUser;
import com.example.demo.entity.SystemMsg;
import com.example.demo.entity.SystemInfo;

/**
 * Other情報 Controller
 */
@Controller
public class OtherController {
	/**
	* セッション情報（複数のコンローラ間で共有したいログイン情報などを格納する）
	*/
	@Autowired
	private HttpSession session;
//	HttpSession session;   // アクセス修飾子を省略（※現在のクラスと同じパッケージの、全てのクラスからアクセスできる）

	/**
	 * ログイン情報
	 */
	private AuthUser authUser;

	/**
	* システム情報
	*/
	private SystemInfo systemInfo;

	/**
	 * システム情報とログイン情報の設定
	 * @param Model model
	 * @return
	 */
	private String setAuthUser(Model model) {

		String strRtnForm = null;

		// RootControllerクラスで設定した、セッション情報を取得。
		systemInfo = (SystemInfo)session.getAttribute("SessionSysInfo");
		// システム情報のパラメータを渡す。
		model.addAttribute("sysInfo", systemInfo);


		// RootControllerクラスで設定した、セッション情報を取得。
		authUser = (AuthUser)session.getAttribute("SessionAuthUser");
		// ログイン情報のパラメータを渡す。
		model.addAttribute("authUser", authUser);

		if (systemInfo == null) {
			// システム情報のセッション情報が取得できなかった場合、
			//システムエラー画面を表示させる。
			strRtnForm = "syserror";
		    model.addAttribute("validationError", SystemMsg.getErrMsg(SystemMsg.ERR_CODE_001));
			return strRtnForm;
		}

		if (authUser == null) {
			// ログイン情報のセッション情報が取得できなかった場合、
			//システムエラー画面を表示させる。
			strRtnForm = "syserror";
		    model.addAttribute("validationError", SystemMsg.getErrMsg(SystemMsg.ERR_CODE_002));
			return strRtnForm;
		}

		return strRtnForm;
	}

  /**
   * About Me画面を表示
   * @param model Model
   * @return About Me画面
   * 補足(ローカル環境)：URL…http://localhost:8080/others/aboutme.html
   */
//  @RequestMapping(value = "/load", method = RequestMethod.GET)
  @GetMapping(value = "/others/aboutme")
  public String aboutme(Model model) {

	// ログイン情報の取得と設定。
	String strRtnForm = setAuthUser(model);
	if (strRtnForm != null) {
		// セッション情報の取得に失敗した場合
		//システムエラー画面を表示
		return strRtnForm;
	}

	return "others/aboutme";
  }

  /**
   * Link画面を表示
   * @param model Model
   * @return Link画面
   */
  @GetMapping(value = "/others/portfolio")
  public String portfolio(Model model) {

	// ログイン情報の取得と設定。
	String strRtnForm = setAuthUser(model);
	if (strRtnForm != null) {
		// セッション情報の取得に失敗した場合
		//システムエラー画面を表示
		return strRtnForm;
	}

	return "others/portfolio";
  }

  /**
   * 開発ドキュメント画面を表示
   * @param model Model
   * @return 開発ドキュメント画面
   */
  @GetMapping(value = "/others/devdoc")
  public String devdoc(Model model) {

	// ログイン情報の取得と設定。
	String strRtnForm = setAuthUser(model);
	if (strRtnForm != null) {
		// セッション情報の取得に失敗した場合
		//システムエラー画面を表示
		return strRtnForm;
	}

	return "others/devdoc";
  }

  /**
   * 開発履歴画面を表示
   * @param model Model
   * @return 開発履歴画面
   */
  @GetMapping(value = "/others/devhistory")
  public String devhistory(Model model) {

	// ログイン情報の取得と設定。
	String strRtnForm = setAuthUser(model);
	if (strRtnForm != null) {
		// セッション情報の取得に失敗した場合
		//システムエラー画面を表示
		return strRtnForm;
	}

	return "others/devhistory";
  }
}
