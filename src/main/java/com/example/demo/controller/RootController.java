package com.example.demo.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;

import com.example.demo.auth.AuthUser;
import com.example.demo.mdl.DateEdit;

/**
 * ユーザー情報 Controller
 */
@Controller
public class RootController {
	/**
	* セッション情報（複数のコンローラ間で共有したいログイン情報などを格納する）
	*/
	@Autowired
	private HttpSession session;

	/**
	* ログイン情報
	*/
	private AuthUser authUser;


  /**
   * ログイン情報の設定
   * @param AuthUser authUser
   * @param model Model
   * @return
   * 補足説明（ログイン情報を取得するタイミングは、トップページにアクセスした時です。
   * ログアウト時にも、トップページに自動アクセスするので、再ログイン時にログイン情報を再取得します。）
   */
	private void setAuthUser(AuthUser authUser, Model model) {

		this.authUser = authUser;
		this.authUser.setTitle("Java(Spring Boot)ポートフォリオ");

		// システム日付を取得
		String strDateYMD = DateEdit.getSysDate("yyyy年M月d日");
		this.authUser.setSysdateYMD(strDateYMD);

		// システム日付の曜日を取得
		strDateYMD = DateEdit.getSysDate("yyyy/MM/dd");
		String strYoubi = DateEdit.getYoubi(strDateYMD);

		this.authUser.setSysdateYoubi(strYoubi);

		// よく使うパラメータをメモ
		// authUser.getId();
		// authUser.getEmail();
		// authUser.getUsername();
		// authUser.getRoles().get(0);	// (出力例)『ROLE_USER』

		// System.out.println(authUser.getRoles()); // List<String>…(出力例)『[ROLE_USER]』

	    // セッション情報に保存
		// (※このセッション情報は、他のコントローラクラスでも使用が可能です。)
	    session.setAttribute("SessionAuthUser", authUser);
	}


  /**
   * トップページを表示
   * @param AuthUser authUser
   * @param model Model
   * @return トップページ
   * 補足(ローカル環境)：URL…http://localhost:8080/
   */
  @GetMapping(value = "/")
  public String root(@AuthenticationPrincipal AuthUser userDetails, Model model) {

	// 他のコントローラ処理でも、ログイン情報を扱えるように、
	// トップページにアクセスしたタイミングで、ログイン情報を取得。
	setAuthUser(userDetails, model);

	// ログイン情報のパラメータを渡す。
	model.addAttribute("authUser", authUser);

	return "index";
  }


  /**
   * ログイン画面を表示
   * @return ログイン画面
   */
  @GetMapping(value = "/login")
  public String login() {
    return "login";
  }
//  補足説明：GetMapping…Getメソッドで送信されてきたリクエストを処理するアノテーション
//　ちなみに↓２つは同じ意味
//  @GetMapping("/login")
//  @RequestMapping(value = "/login", method = RequestMethod.POST)

  /**
   * ログイン画面でログインエラーが発生
   * @param model Model
   * @return ログイン画面
   */
  @PostMapping(value = "/loginerr")
  public String loginerr(@RequestAttribute("SPRING_SECURITY_LAST_EXCEPTION") 
		AuthenticationException ex,
		Model model) {
	model.addAttribute("authenticationException", ex);
	return "login";
  }


  /**
   * サンプル（テンプレート用）画面を表示
   * @param model Model
   * @return サンプル画面
   * 補足(ローカル環境)：URL…http://localhost:8080/sample.html
   */
  @GetMapping(value = "/sample")
  public String sample(){

      return "sample";
  }

  /**
   * サンプル（ログイン認証情報のチェック用）画面を表示
   * @param model Model
   * @return サンプル画面
   */
  @GetMapping(value = "/sample2")
  public String sample2(Model model) {

	// サンプル画面に、ログイン情報のパラメータを渡す。
	model.addAttribute("authId", authUser.getId());
	model.addAttribute("authName", authUser.getUsername());

	model.addAttribute("authUser", authUser);

	return "sample2";
  }
}