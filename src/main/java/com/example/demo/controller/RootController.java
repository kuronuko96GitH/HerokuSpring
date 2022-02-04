package com.example.demo.controller;

//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.demo.auth.AuthUser;
import com.example.demo.dto.UserRequest;
import com.example.demo.dto.UserUpdateRequest;
import com.example.demo.dto.UserUpdateRequestPass;
import com.example.demo.dto.WorkRequest;
import com.example.demo.dto.WorkRequestSearch;
import com.example.demo.dto.WorkUpdateRequest;
import com.example.demo.entity.User;
import com.example.demo.entity.Work;
import com.example.demo.mdl.DateEdit;
import com.example.demo.service.UserService;
import com.example.demo.service.WorkService;

/**
 * ユーザー情報 Controller
 */
@Controller
public class RootController {

  /**
   * ユーザー情報 Service
   */
  @Autowired
  private UserService userService;

  /**
   * 勤怠(work)情報 Service
   */
  @Autowired
  private WorkService workService;

  /**
   * ログイン情報
   */
  private AuthUser authUser;

  /**
   * ログイン情報の設定
   * @param AuthUser authUser
   * @return
   * 補足説明（ログイン情報を取得するタイミングは、トップページにアクセスした時です。
   * ログアウト時にも、トップページに自動アクセスするので、再ログイン時にログイン情報を再取得します。）
   */
	private void setAuthUser(AuthUser authUser) {
		this.authUser = authUser;
		this.authUser.setTitle("Java(Spring Boot)ポートフォリオ");

		String strDateYMD = DateEdit.getSysDate("yyyy年M月d日");
		this.authUser.setSysdateYMD(strDateYMD);
		// sysdateYmd(YYYY年M月D日)

		// よく使うパラメータをメモ
		// authUser.getId();
		// authUser.getEmail();
		// authUser.getUsername();
		// authUser.getRoles().get(0);	// (出力例)『ROLE_USER』

		// System.out.println(authUser.getRoles()); // List<String>…(出力例)『[ROLE_USER]』
	}


  /**
   * トップページを表示
   * @param model Model
   * @return トップページ
   * 補足(ローカル環境)：URL…http://localhost:8080/
   */
  @GetMapping(value = "/")
  public String root(@AuthenticationPrincipal AuthUser userDetails, Model model) {

	// 他のコントローラ処理でも、ログイン情報を扱えるように、
	// トップページにアクセスしたタイミングで、ログイン情報を取得。
	setAuthUser(userDetails);

	// ログイン情報のパラメータを渡す。
	model.addAttribute("authUser", authUser);


	return "index";
  }

  /**
   * ログイン画面を表示
   * @param model Model
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




  /**
   * About Me画面を表示
   * @param model Model
   * @return About Me画面
   * 補足(ローカル環境)：URL…http://localhost:8080/others/aboutme.html
   */
  @GetMapping(value = "/others/aboutme")
  public String aboutme(Model model) {
	// ログイン情報のパラメータを渡す。
	model.addAttribute("authUser", authUser);
	
	return "others/aboutme";
  }

  /**
   * Link画面を表示
   * @param model Model
   * @return Link画面
   */
  @GetMapping(value = "/others/portfolio")
  public String portfolio(Model model) {
	// ログイン情報のパラメータを渡す。
	model.addAttribute("authUser", authUser);
	
	return "others/portfolio";
  }

  /**
   * 開発ドキュメント画面を表示
   * @param model Model
   * @return 開発ドキュメント画面
   */
  @GetMapping(value = "/others/devdoc")
  public String devdoc(Model model) {
	// ログイン情報のパラメータを渡す。
	model.addAttribute("authUser", authUser);
		
	return "others/devdoc";
  }




  /**
   * ユーザー情報一覧画面を表示
   * @param model Model
   * @return ユーザー情報一覧画面
   */
  @GetMapping(value = "/user/list")
  public String displayList(Model model) {

	// ログイン情報のパラメータを渡す。
	model.addAttribute("authUser", authUser);

	if (authUser.getRoles().get(0).equals("ROLE_USER")) {
		// 『ROLE_USER』の場合は、ユーザー情報一覧を閲覧したとしても、自分のユーザーした検索できない。
		List<User> userlist = userService.findByUserId(authUser.getId());
		model.addAttribute("userlist", userlist);
	} else {
		// ユーザー情報の全検索
		List<User> userlist = userService.searchAll();
		model.addAttribute("userlist", userlist);
	}

	return "user/list";
  }

  /**
   * ユーザー新規登録画面を表示
   * @param model Model
   * @return ユーザー情報一覧画面
   */
  @GetMapping(value = "/user/add")
  public String displayAdd(Model model) {
	// ログイン情報のパラメータを渡す。
	model.addAttribute("authUser", authUser);

    model.addAttribute("userRequest", new UserRequest());
    return "user/add";
  }
  
  /**
   * ユーザー新規登録
   * @param userRequest リクエストデータ
   * @param model Model
   * @return ユーザー情報一覧画面
   */
  @RequestMapping(value = "/user/create", method = RequestMethod.POST)
  public String create(@Validated @ModelAttribute UserRequest userRequest, BindingResult result, Model model) {

	// ログイン情報のパラメータを渡す。
	model.addAttribute("authUser", authUser);

//    int intCnt = 0; // デバッグ用
//    String strX = ""; // デバッグ用

	if (result.hasErrors()) {
      // 入力チェックエラーの場合
      List<String> errorList = new ArrayList<String>();
      for (ObjectError error : result.getAllErrors()) {
        errorList.add(error.getDefaultMessage());
//        strX = errorList.get(intCnt); // デバッグ用
//        intCnt++; // デバッグ用
      }
      model.addAttribute("validationError", errorList);
      return "user/add";
    }

    // 新規登録しようしたメールアドレスが、データベースに存在してないかを検索
    Integer intCnt = userService.findByEmailCnt(userRequest.getEmail());
    
	if ( intCnt > 0) {
		  // データベースに同じメールアドレスが存在していた場合。
//	    model.addAttribute("validationError", "既に登録済のメールアドレスです。");

	    List<String> errorList = new ArrayList<String>();
        errorList.add("既に登録済のメールアドレスです。");
        model.addAttribute("validationError", errorList);
	    
	    return "user/add";  
	}


    // ユーザー情報の登録
    userService.create(userRequest);

    // ユーザー情報一覧画面に戻る
    // (ログイン画面側から新規登録の場合は、強制的にログイン画面へ一度戻されてから、再ログインの流れになります。)
    return "redirect:/user/list";
//    return "login"; // ログイン画面に戻る
  }
  
  /**
   * ユーザー情報詳細画面を表示
   * @param id 表示するユーザーID
   * @param model Model
   * @return ユーザー情報詳細画面
   */
  @GetMapping("/user/{id}")
  public String displayView(@PathVariable Long id, Model model) {

	// ログイン情報のパラメータを渡す。
	model.addAttribute("authUser", authUser);

    User user = userService.findById(id);
    model.addAttribute("userData", user);
    return "user/view";
  }

  /**
   * ユーザー編集画面を表示
   * @param id 表示するユーザーID
   * @param model Model
   * @return ユーザー編集画面
   */
  @GetMapping("/user/{id}/edit")
  public String displayEdit(@PathVariable Long id, Model model) {

	// ログイン情報のパラメータを渡す。
	model.addAttribute("authUser", authUser);

    User user = userService.findById(id);
    UserUpdateRequest userUpdateRequest = new UserUpdateRequest();
    userUpdateRequest.setId(user.getId());
    userUpdateRequest.setName(user.getName());
    userUpdateRequest.setEmail(user.getEmail());
//    userUpdateRequest.setPassword(user.getPassword());
    userUpdateRequest.setPhone(user.getPhone());
    userUpdateRequest.setAddress(user.getAddress());
    model.addAttribute("userUpdateRequest", userUpdateRequest);
    return "user/edit";
  }

  /**
   * ユーザー更新
   * @param userRequest リクエストデータ
   * @param model Model
   * @return ユーザー情報詳細画面
   */
  @RequestMapping(value = "/user/update", method = RequestMethod.POST)
  public String update(@Validated @ModelAttribute UserUpdateRequest userUpdateRequest, BindingResult result, Model model) {

	// ログイン情報のパラメータを渡す。
	model.addAttribute("authUser", authUser);

//    long lngCnt = 0; // デバッグ用
//    String strX = ""; // デバッグ用

    if (result.hasErrors()) {
      // 入力チェックエラーの場合
      List<String> errorList = new ArrayList<String>();
      for (ObjectError error : result.getAllErrors()) {
        errorList.add(error.getDefaultMessage());
//      strX = userUpdateRequest.getEmail(); // デバッグ用
//      lngCnt = userUpdateRequest.getId(); // デバッグ用
      }
      model.addAttribute("validationError", errorList);
      return "user/edit";
    }


    if (!isAuthRoleCheck(userUpdateRequest.getId())) {
    // 権限チェックがエラーの場合、更新処理はさせない。
      model.addAttribute("validationError", "管理者権限が無いため、編集はできません。");
      return "user/edit";
    }



    // 更新しようしたメールアドレスが、データベースに存在してないかを検索
    Integer intCnt = userService.findByEmailCnt(userUpdateRequest.getId(), userUpdateRequest.getEmail());
    
	if ( intCnt > 0) {
		  // データベースに同じメールアドレスが存在していた場合。
	    List<String> errorList = new ArrayList<String>();
        errorList.add("既に登録済のメールアドレスです。");
        model.addAttribute("validationError", errorList);
	    
	    return "user/edit";  
	}


    // ユーザー情報の更新
    userService.update(userUpdateRequest);
    return String.format("redirect:/user/%d", userUpdateRequest.getId());
  }


  /**
   * ユーザー(パスワード)編集画面を表示
   * @param id 表示するユーザーID
   * @param model Model
   * @return ユーザー(パスワード)編集画面
   */
  @GetMapping("/user/{id}/editpass")
  public String displayEditPass(@PathVariable Long id, Model model) {

	// ログイン情報のパラメータを渡す。
	model.addAttribute("authUser", authUser);

    User user = userService.findById(id);
    UserUpdateRequestPass userUpdateRequest = new UserUpdateRequestPass();
    // パスワードの情報は、セキュリィ対策として保持しない
    userUpdateRequest.setId(user.getId());
//    userUpdateRequest.setPassword(user.getPassword());
    model.addAttribute("userUpdateRequestPass", userUpdateRequest);
    return "user/editpass";
  }

  /**
   * ユーザー(パスワード)更新
   * @param userRequest リクエストデータ
   * @param model Model
   * @return ユーザー情報詳細画面
   */
  @RequestMapping(value = "/user/updatepass", method = RequestMethod.POST)
  public String updatePass(@Validated @ModelAttribute UserUpdateRequestPass userUpdateRequestPass, BindingResult result, Model model) {

	// ログイン情報のパラメータを渡す。
	model.addAttribute("authUser", authUser);

    if (result.hasErrors()) {
      List<String> errorList = new ArrayList<String>();
      for (ObjectError error : result.getAllErrors()) {
        errorList.add(error.getDefaultMessage());
      }
      model.addAttribute("validationError", errorList);
      return "user/editpass";
    }
    

    if (!isAuthRoleCheck(userUpdateRequestPass.getId())) {
    // 権限チェックがエラーの場合、更新処理はさせない。
      model.addAttribute("validationError", "管理者権限が無いため、編集はできません。");
      return "user/editpass";
    }

    // ユーザー情報（パスワードのみ）の更新
    userService.updatePass(userUpdateRequestPass);
    return String.format("redirect:/user/%d", userUpdateRequestPass.getId());
  }


  /**
   * ユーザー情報削除
   * @param id 表示するユーザーID
   * @param model Model
   * @return ユーザー情報詳細画面
   */
  @GetMapping("/user/{id}/delete")
  public String delete(@PathVariable Long id, Model model) {

	// ログイン情報のパラメータを渡す。
	model.addAttribute("authUser", authUser);


	boolean blnErrCnk = true;
	Long lngAuthId = authUser.getId(); // ログイン情報のユーザーIDを取得
	
    if (!isAuthRoleCheck(id)) {
    // 権限チェックがエラーの場合、削除処理はさせない。
      model.addAttribute("validationError", "管理者権限が無いため、削除はできません。");
      blnErrCnk = false;
    }

    if (lngAuthId.equals(id)) {
    // ログイン中のユーザーと、削除対象のユーザーが同じだった場合。
      model.addAttribute("validationError", "ログイン中のユーザーは、削除できません。");
      blnErrCnk = false;
    }
    
    if (!blnErrCnk) {
    	// エラーチェックに、エラーがあった場合

        User user = userService.findById(id);
        model.addAttribute("userData", user);
        // ユーザー詳細画面を表示。
        return "user/view";
    }


    // ユーザー情報の削除
    userService.delete(id);
    return "redirect:/user/list";
  }


	private boolean isAuthRoleCheck(@PathVariable Long id) {
		
		// メニュー画面にアクセスした時に取得した、ログイン情報を使用して権限チェックをする。
		boolean blnChk = false;
		String strRoles = authUser.getRoles().get(0);	// (出力例)『ROLE_USER』
		Long lngAuthId = authUser.getId();
		
		// 編集対象のIDが、ログイン情報のIDと同じ場合は、権限チェックをせずに許可を出す。
		if (lngAuthId.equals(id)) {
			return true;
		}

		if (strRoles.equals("ROLE_ADMIN")) {
			// 管理者権限ありのため、編集・削除の許可を出す。
			blnChk = true;
		}
		
		return blnChk;
	}




	  /**
	   * 勤怠情報一覧画面を表示
	   * @param model Model
	   * @return 勤怠情報一覧画面
	   */
	  @GetMapping(value = "/work/list")
	  public String displayListWork(Model model) {

		// ログイン情報のパラメータを渡す。
		model.addAttribute("authUser", authUser);

		// 勤怠情報一覧画面(勤怠年月)検索のために、検索条件の年月日の空データを作っておく。
		// ここで作成しておかないと、HTML側でnullエラーになる。
	    model.addAttribute("workRequest", new WorkRequestSearch());


		// 勤怠情報の検索
		List<Work> worklist = workService.findByUserID(authUser.getId());
		model.addAttribute("worklist", worklist);


		return "work/list";
	  }

	  /**
	   * 勤怠情報一覧画面(勤怠年月日)検索
	   * @param WorkRequest workRequest（検索条件：勤怠開始日）
	   * @param model Model
	   * @return 勤怠情報一覧画面
	   * @throws ParseException 
	   */
//	  @GetMapping(value = "/work/search")
	  @RequestMapping(value = "/work/search", method = RequestMethod.POST)
	  public String displayListWorkSearch(@Validated @ModelAttribute WorkRequestSearch workRequest, BindingResult result, Model model) {
//	  public String displayListWorkSearch(@Validated @ModelAttribute WorkRequestSearch workRequest, BindingResult result, Model model) throws ParseException {

		// ログイン情報のパラメータを渡す。
		model.addAttribute("authUser", authUser);

		// 勤怠情報一覧画面(勤怠年月)検索のために、検索条件の年月日のパラメータを渡しておく。
		// ここで作成しておかないと、HTML側でnullエラーになる。
	    model.addAttribute("workRequest", workRequest);

		if (result.hasErrors()) {
		      // 入力チェックエラーの場合
		      List<String> errorList = new ArrayList<String>();
		      for (ObjectError error : result.getAllErrors()) {
		        errorList.add(error.getDefaultMessage());
		      }
		      model.addAttribute("validationError", errorList);
		      return "work/list";
		}

		Date dateStart = null; // 勤怠開始年月日
		Date dateEnd = null; // 勤怠終了年月日
		
		String StrSearchCd = "ALL"; // 年月日の検索コード：
									// "ALL"：検索条件指定なし（勤怠開始日と勤怠終了日のどちらも空白。）
									// "LN"：(左側)勤怠開始日のみ。(右側)勤怠終了日は空白(Null)。
									// "NR"：(右側)勤怠終了日のみ。(左側)勤怠開始日は空白(Null)。
									// "LR"：(左右)勤怠開始日と勤怠終了日の両方入力あり。


		// 検索条件の入力チェック(開始年月日)
		if (workRequest.getStartDateY().isEmpty()
			&& workRequest.getStartDateM().isEmpty()
			&& workRequest.getStartDateD().isEmpty() ) {
			// 年・月・日が空白の場合は、エラーチェックの対象外。
				StrSearchCd = "ALL"; // "ALL"：検索条件指定なし（勤怠開始日と勤怠終了日のどちらも空白。）

		} else {
			String StrChk = workRequest.getStartDateY();
			if (!isValueCheck(StrChk, "勤怠開始日(年)", model)) {
		          return "work/list";
			}
			StrChk = workRequest.getStartDateM();
			if (!isValueCheck(StrChk, "勤怠開始日(月)", model)) {
		          return "work/list";
			}
			StrChk = workRequest.getStartDateD();
			if (!isValueCheck(StrChk, "勤怠開始日(日)", model)) {
		          return "work/list";
			}

			StrSearchCd = "LN"; // "LN"：(左側)勤怠開始日のみ。(右側)勤怠終了日は空白。
		}

		if ( StrSearchCd.equals("LN") ) {
			// "LN"：(左側)勤怠開始日のみ。(右側)勤怠終了日は空白。
			dateStart = DateEdit.getDate(workRequest.getStartDateY(), workRequest.getStartDateM(), workRequest.getStartDateD());
/*
		    String strDateS = String.format("%04d", Integer.parseInt(workRequest.getStartDateY())) + "/"
					+ String.format("%02d", Integer.parseInt(workRequest.getStartDateM())) + "/"
					+ String.format("%02d", Integer.parseInt(workRequest.getStartDateD())) + " 00:00:00";

			SimpleDateFormat sdFormatS = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
			dateStart = sdFormatS.parse(strDateS);
*/
		}



		// 検索条件の入力チェック(終了年月日)
		if (workRequest.getEndDateY().isEmpty()
			&& workRequest.getEndDateM().isEmpty()
			&& workRequest.getEndDateD().isEmpty() ) {
			// 年・月・日が空白の場合は、エラーチェックの対象外。

			if ( StrSearchCd.equals("LN") ) {
				// 年月日の検索コードが
				// "LN"：(左側)勤怠開始日のみ。(右側)勤怠終了日は空白。
				
				// 例）検索条件の入力状態…開始日：2022/XX/XX　～　終了日：空白
				StrSearchCd = "LN"; // "LN"：(左側)勤怠開始日のみ。(右側)勤怠終了日は空白。

			} else {
				// 例）検索条件の入力状態…開始日：空白　～　終了日：空白
				StrSearchCd = "ALL"; // "ALL"：検索条件指定なし（勤怠開始日と勤怠終了日のどちらも空白。）
			}

		} else {
			String StrChk = workRequest.getEndDateY();
			if (!isValueCheck(StrChk, "勤怠終了日(年)", model)) {
		          return "work/list";
			}
			StrChk = workRequest.getEndDateM();
			if (!isValueCheck(StrChk, "勤怠終了日(月)", model)) {
		          return "work/list";
			}
			StrChk = workRequest.getEndDateD();
			if (!isValueCheck(StrChk, "勤怠終了日(日)", model)) {
		          return "work/list";
			}

			if ( StrSearchCd.equals("LN") ) {
				// 年月日の検索コードが
				// "LN"：(左側)勤怠開始日のみ。(右側)勤怠終了日は空白。

				// 例）検索条件の入力状態…開始日：2022/XX/XX　～　終了日：2022/XX/XX
				StrSearchCd = "LR"; // "LR"：(左右)勤怠開始日と勤怠終了日の両方入力あり。

			} else {
				// 例）検索条件の入力状態…開始日：空白　～　終了日：2022/XX/XX
				StrSearchCd = "NR"; // "NR"：(右側)勤怠終了日のみ。(左側)勤怠開始日は空白(Null)。
			}
		}

		if ( StrSearchCd.equalsIgnoreCase("LR") || StrSearchCd.equals("NR") ) {
			// "LR"：(左右)勤怠開始日と勤怠終了日の両方入力あり。
			// または　"NR"：(右側)勤怠終了日のみ。(左側)勤怠開始日は空白(Null)。
/*
		    String strDateE = String.format("%04d", Integer.parseInt(workRequest.getEndDateY())) + "/"
					+ String.format("%02d", Integer.parseInt(workRequest.getEndDateM())) + "/"
					+ String.format("%02d", Integer.parseInt(workRequest.getEndDateD())) + " 23:59:59";

			SimpleDateFormat sdFormatE = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
			dateEnd = sdFormatE.parse(strDateE);
*/
			dateEnd = DateEdit.getDateTime(workRequest.getEndDateY(), workRequest.getEndDateM(), workRequest.getEndDateD(), "23", "59", "59");
		}

		List<Work> worklist;
		
		// 年月日の検索コードで検索条件を変更する。
		if (StrSearchCd.equalsIgnoreCase("LN")) {
			// "LN"：(左側)勤怠開始日のみ。(右側)勤怠終了日は空白(Null)。
			worklist = workService.findByDate(authUser.getId(), dateStart, null);
		} else if (StrSearchCd.equals("NR")) {
			// "NR"：(右側)勤怠終了日のみ。(左側)勤怠開始日は空白(Null)。
			worklist = workService.findByDate(authUser.getId(), null, dateEnd);
		} else if (StrSearchCd.equals("LR")) {
			// "LR"：(左右)勤怠開始日と勤怠終了日の両方入力あり。
			worklist = workService.findByDate(authUser.getId(), dateStart, dateEnd);
		} else {
			// "ALL"：検索条件指定なし（勤怠開始日と勤怠終了日のどちらも空白。）
			worklist = workService.findByUserID(authUser.getId());
		}


		if (worklist.size() == 0) {
			// 該当データ無し。
			model.addAttribute("searchMsg", "該当データがありません。");
		}
		model.addAttribute("worklist", worklist);

		return "work/list";
	  }

	  /**
	   * 勤怠情報一覧画面(勤怠年月)検索
	   * @param WorkRequest workRequest（検索条件：勤怠開始日）
	   * @param model Model
	   * @return 勤怠情報一覧画面
	   * @throws ParseException 
	   */
	  @RequestMapping(value = "/work/searchym", method = RequestMethod.POST)
	  public String displayListWorkSearchYM(@Validated @ModelAttribute WorkRequestSearch workRequest, BindingResult result, Model model) {
//	  public String displayListWorkSearchYM(@Validated @ModelAttribute WorkRequestSearch workRequest, BindingResult result, Model model) throws ParseException {
//	  public String displayListWorkSearchYM(@Validated @ModelAttribute WorkRequest workRequest, BindingResult result, Model model) throws ParseException {

		// ログイン情報のパラメータを渡す。
		model.addAttribute("authUser", authUser);

		// 勤怠情報一覧画面(勤怠年月)検索のために、検索条件の年月日のパラメータを渡しておく。
		// ここで作成しておかないと、HTML側でnullエラーになる。
	    model.addAttribute("workRequest", workRequest);

		if (result.hasErrors()) {
		      // 入力チェックエラーの場合
		      List<String> errorList = new ArrayList<String>();
		      for (ObjectError error : result.getAllErrors()) {
		        errorList.add(error.getDefaultMessage());
		      }
		      model.addAttribute("validationError", errorList);
		      return "work/list";
		}

		Date dateStart = null; // 勤怠開始年月日
		Date dateEnd = null; // 勤怠終了年月日
		
		String StrSearchCd = "ALL"; // 年月日の検索コード：
									// "ALL"：検索条件指定なし（検索条件の勤怠年月が空白。）


		// 検索条件の入力チェック(検索年月)
		if (workRequest.getSearchDateY().isEmpty()
			&& workRequest.getSearchDateM().isEmpty() ) {
			// 年・月・日が空白の場合は、エラーチェックの対象外。
//				StrSearchCd = "ALL"; // "ALL"：検索条件指定なし（勤怠開始日と勤怠終了日のどちらも空白。）

		} else {
			String StrChk = workRequest.getSearchDateY();
			if (!isValueCheck(StrChk, "勤怠年月(年)", model)) {
		          return "work/list";
			}
			StrChk = workRequest.getSearchDateM();
			if (!isValueCheck(StrChk, "勤怠年月(月)", model)) {
		          return "work/list";
			}

			StrSearchCd = "LR"; // "LR"：勤怠年月入力あり。
		}

		if ( !StrSearchCd.equals("ALL") ) {
			// 勤怠年月の入力があった場合

			// 範囲検索の開始日（該当月の初日）
			dateStart = DateEdit.getDate(workRequest.getSearchDateY(), workRequest.getSearchDateM(), "1");

			// 範囲検索の終了日(該当月の末日)
			dateEnd = DateEdit.getDateLastDay(workRequest.getSearchDateY(), workRequest.getSearchDateM());
/*
			// 範囲検索の年月
		    String strDateYM =  String.format("%04d", Integer.parseInt(workRequest.getSearchDateY())) + "/"
					+ String.format("%02d", Integer.parseInt(workRequest.getSearchDateM()));


			// 範囲検索の開始日
		    String strDateS = strDateYM + "/01 00:00:00";

			SimpleDateFormat sdFormatS = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
			dateStart = sdFormatS.parse(strDateS);


			// 範囲検索の終了日
			// 年月+("/01")を引数として渡し、末日を取得する。
		    String strDateE = strDateYM + "/" + DateEdit.getLastDay(strDateYM + "/01") + " 00:00:00";

			SimpleDateFormat sdFormatE = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
			dateEnd = sdFormatE.parse(strDateE);
*/
		}


		List<Work> worklist;
		
		if (StrSearchCd.equals("ALL")) {
			// "ALL"：検索条件指定なし（勤怠年月が空白。）
			worklist = workService.findByUserID(authUser.getId());
		} else {
			// 年月日の検索コードで検索条件を変更する。
			worklist = workService.findByDate(authUser.getId(), dateStart, dateEnd);
		}


		if (worklist.size() == 0) {
			// 該当データ無し。
			model.addAttribute("searchMsg", "該当データがありません。");
		}
		model.addAttribute("worklist", worklist);

		return "work/list";
	  }

	  /**
	   * 勤怠情報の新規登録画面を表示
	   * @param model Model
	   * @return 勤怠情報一覧画面
	   */
	  @GetMapping(value = "/work/add")
	  public String displayAddWork(Model model) {

		// ログイン情報のパラメータを渡す。
		model.addAttribute("authUser", authUser);

	    model.addAttribute("workRequest", new WorkRequest());
		    
	    return "work/add";
	  }


	  /**
	   * 勤怠情報の新規登録画面を表示
	   * (勤怠情報一覧のコピーボタンを押した時のイベント)
	   * @param id 表示するワークID
	   * @param model Model
	   * @return 勤怠情報の新規登録画面
	   */
	  @GetMapping("/work/addcopy{id}")
	  public String displayAddCopyWork(@PathVariable Long id, Model model) {

		// ログイン情報のパラメータを渡す。
		model.addAttribute("authUser", authUser);
		
		Work work = workService.findById(id);
	    WorkRequest workRequest = new WorkRequest();
	    
	    workRequest.setContent(work.getContent());


	    // 開始日時(年)
	    workRequest.setStartDateY(DateEdit.getDate(work.getStartDate(), "yyyy"));
	    // 開始日時(月)
	    workRequest.setStartDateM(DateEdit.getDate(work.getStartDate(), "M"));
	    // 開始日時(日)
	    workRequest.setStartDateD(DateEdit.getDate(work.getStartDate(), "d"));
	    // 開始日時(時間)
	    workRequest.setStartDateHH(DateEdit.getDate(work.getStartDate(), "H"));
	    // 開始日時(分)
	    workRequest.setStartDateMI(DateEdit.getDate(work.getStartDate(), "m"));


	    // 終了日時(年)
	    workRequest.setEndDateY(DateEdit.getDate(work.getEndDate(), "yyyy"));
	    // 終了日時(月)
	    workRequest.setEndDateM(DateEdit.getDate(work.getEndDate(), "M"));
	    // 終了日時(日)
	    workRequest.setEndDateD(DateEdit.getDate(work.getEndDate(), "d"));
	    // 終了日時(時間)
	    workRequest.setEndDateHH(DateEdit.getDate(work.getEndDate(), "H"));
	    // 終了日時(分)
	    workRequest.setEndDateMI(DateEdit.getDate(work.getEndDate(), "m"));

/*
	    // 開始日時
	    String strDateS = new SimpleDateFormat("yyyy/MM/dd HH:mm").format(work.getStartDate());
	    
	    // 開始日時(年)
	    workRequest.setStartDateY(strDateS.substring(0, 4));
	    // 開始日時(月)
	    workRequest.setStartDateM(strDateS.substring(5, 7));
	    // 開始日時(日)
	    workRequest.setStartDateD(strDateS.substring(8, 10));
	    // 開始日時(時間)
	    workRequest.setStartDateHH(strDateS.substring(11, 13));
	    // 開始日時(分)
	    workRequest.setStartDateMI(strDateS.substring(14, 16));


	    // 終了日時
	    String strDateE = new SimpleDateFormat("yyyy/MM/dd HH:mm").format(work.getEndDate());

	    // 終了日時(年)
	    workRequest.setEndDateY(strDateE.substring(0, 4));
	    // 終了日時(月)
	    workRequest.setEndDateM(strDateE.substring(5, 7));
	    // 終了日時(日)
	    workRequest.setEndDateD(strDateE.substring(8, 10));
	    // 終了日時(時間)
	    workRequest.setEndDateHH(strDateE.substring(11, 13));
	    // 終了日時(分)
	    workRequest.setEndDateMI(strDateE.substring(14, 16));
*/

	    model.addAttribute("workRequest", workRequest);

	    return "work/add";
	  }


	  /**
	   * 勤怠情報新規登録
	   * @param workRequest リクエストデータ
	   * @param model Model
	   * @return 勤怠情報一覧画面
	   */

	  @RequestMapping(value = "/work/create", method = RequestMethod.POST)
	  public String createWork(@Validated @ModelAttribute WorkRequest workRequest, BindingResult result, Model model) {

		// ログイン情報のパラメータを渡す。
		model.addAttribute("authUser", authUser);
	
		if (result.hasErrors()) {
	      // 入力チェックエラーの場合
	      List<String> errorList = new ArrayList<String>();
	      for (ObjectError error : result.getAllErrors()) {
	        errorList.add(error.getDefaultMessage());
	      }
	      model.addAttribute("validationError", errorList);
	      return "work/add";
	    }

	    // 勤怠情報の新規登録
	    workService.create(workRequest, authUser.getId()); // ログイン時のユーザーIDを設定。
/*
		try {
		    workService.create(workRequest, authUser.getId()); // ログイン時のユーザーIDを設定。
		} catch (Exception e) {
			e.printStackTrace();
		}
*/
	    return "redirect:/work/list";
	  }


	  /**
	   * 打刻登録画面を表示
	   * @param model Model
	   * @return 打刻登録画面
	   * @throws ParseException 
	   */
	  @GetMapping(value = "/work/stamping")
	  public String displayStamping(Model model) {

		// ログイン情報のパラメータを渡す。
		model.addAttribute("authUser", authUser);

		// 勤怠情報一覧画面(勤怠年月)検索のために、検索条件の年月日の空データを作っておく。
		// ここで作成しておかないと、HTML側でnullエラーになる。
	    model.addAttribute("workRequest", new WorkRequestSearch());


	    Date dateStart;
	    Date dateEnd;
	    
		List<Work> worklist = new ArrayList<>();


        // 現在日を取得する
//        Date dateNow = new Date();
//        Date dateNow = DateEdit.getDate(DateEdit.getSysDate("yyyy"), DateEdit.getSysDate("MM"), DateEdit.getSysDate("dd"));
        dateStart = DateEdit.getDate(DateEdit.getSysDate("yyyy"), DateEdit.getSysDate("MM"), DateEdit.getSysDate("dd"));
		dateEnd = DateEdit.getDateTime(DateEdit.getSysDate("yyyy"), DateEdit.getSysDate("MM"), DateEdit.getSysDate("dd"), "23", "59", "59");

		// 今日の日付で検索。
		worklist = workService.findByDate(authUser.getId(), dateStart, dateEnd);



		if (worklist.size() == 0) {
			// 該当データ無し。(今日の勤怠データが作成されてない場合)
//			model.addAttribute("searchMsg", "該当データがありません。");

		    // 本日の勤怠情報（１レコード・一件分のデータ）
		    Work work = new Work();

//		    work.setUserId(userID);
//		    work.setContent(workRequest.getContent());
		    work.setContent("未登録");

			// 当月の１日～末日で検索。
//			worklist = workService.findByDate(authUser.getId(), dateStart, dateEnd);
		    worklist.add(work);

			model.addAttribute("worklistNow", worklist);

		} else {
			// 今日の勤退データ一件あり

			model.addAttribute("worklistNow", worklist);
		}



		// 今月分の勤怠情報を取得する。
		// 今月の初日
		dateStart = DateEdit.getDate(DateEdit.getSysDate("yyyy"), DateEdit.getSysDate("MM"), "1");

		// 今月の末日
		dateEnd  = DateEdit.getDateLastDay(DateEdit.getSysDate("yyyy"), DateEdit.getSysDate("MM"));

//		List<Work> worklist;
		
		// 当月の１日～末日で勤怠情報を検索。
		worklist = workService.findByDate(authUser.getId(), dateStart, dateEnd);


		if (worklist.size() == 0) {
			// 該当データ無し。
			model.addAttribute("searchMsg", "該当データがありません。");
		}
		model.addAttribute("worklist", worklist);


		return "work/stamping";
	  }

	  /**
	   * 打刻情報登録（出勤・登録）
	   * @param model Model
	   * @return 打刻登録画面
	   */
	  @GetMapping(value = "/work/stampin")
	  public String stampInAdd(Model model) {

		// ログイン情報のパラメータを渡す。
		model.addAttribute("authUser", authUser);

	    // 打刻情報の新規登録
	    workService.createStampIn(authUser.getId()); // ログイン時のユーザーIDを設定。

	    return "redirect:/work/stamping";
	  }

	  /**
	   * 打刻情報登録（出勤・更新）
	   * @param model Model
	   * @return 打刻登録画面
	   */
	  @GetMapping(value = "/work/stampin{id}")
	  public String stampInUpd(@PathVariable Long id, Model model) {

		// ログイン情報のパラメータを渡す。
		model.addAttribute("authUser", authUser);

	    // 打刻情報の(出勤)更新処理
	    workService.updateStampIn(id);

	    return "redirect:/work/stamping";
	  }

	  /**
	   * 打刻情報登録（退勤・更新）
	   * @param model Model
	   * @return 打刻登録画面
	   */
	  @GetMapping(value = "/work/stampout{id}")
	  public String stampOutUpd(@PathVariable Long id, Model model) {

		// ログイン情報のパラメータを渡す。
		model.addAttribute("authUser", authUser);

	    // 打刻情報の(退勤)更新処理
	    workService.updateStampOut(id);

	    return "redirect:/work/stamping";
	  }

	  /**
	   * 勤怠情報詳細画面を表示
	   * @param id 表示するワークID
	   * @param model Model
	   * @return 勤怠情報詳細画面
	   */
	  @GetMapping("/work/{id}")
	  public String displayViewWork(@PathVariable Long id, Model model) {

		// ログイン情報のパラメータを渡す。
		model.addAttribute("authUser", authUser);

	    Work work = workService.findById(id);
	    model.addAttribute("workData", work);
	    return "work/view";
	  }

	  /**
	   * 勤怠情報編集画面を表示
	   * @param id 表示するワークID
	   * @param model Model
	   * @return ユーザー編集画面
	   */
	  @GetMapping("/work/{id}/edit")
	  public String displayEditWork(@PathVariable Long id, Model model) {

		// ログイン情報のパラメータを渡す。
		model.addAttribute("authUser", authUser);

	    Work work = workService.findById(id);
	    WorkUpdateRequest workUpdateRequest = new WorkUpdateRequest();
	    workUpdateRequest.setId(work.getId());
	    workUpdateRequest.setContent(work.getContent());


	    // 開始日時(年)
	    workUpdateRequest.setStartDateY(DateEdit.getDate(work.getStartDate(), "yyyy"));
	    // 開始日時(月)
	    workUpdateRequest.setStartDateM(DateEdit.getDate(work.getStartDate(), "M"));
	    // 開始日時(日)
	    workUpdateRequest.setStartDateD(DateEdit.getDate(work.getStartDate(), "d"));
	    // 開始日時(時間)
	    workUpdateRequest.setStartDateHH(DateEdit.getDate(work.getStartDate(), "H"));
	    // 開始日時(分)
	    workUpdateRequest.setStartDateMI(DateEdit.getDate(work.getStartDate(), "m"));


	    // 終了日時(年)
	    workUpdateRequest.setEndDateY(DateEdit.getDate(work.getEndDate(), "yyyy"));
	    // 終了日時(月)
	    workUpdateRequest.setEndDateM(DateEdit.getDate(work.getEndDate(), "M"));
	    // 終了日時(日)
	    workUpdateRequest.setEndDateD(DateEdit.getDate(work.getEndDate(), "d"));
	    // 終了日時(時間)
	    workUpdateRequest.setEndDateHH(DateEdit.getDate(work.getEndDate(), "H"));
	    // 終了日時(分)
	    workUpdateRequest.setEndDateMI(DateEdit.getDate(work.getEndDate(), "m"));
/*
	    // 開始日時
	    String strDateS = new SimpleDateFormat("yyyy/MM/dd HH:mm").format(work.getStartDate());

	    // 開始日時(年)
	    workUpdateRequest.setStartDateY(strDateS.substring(0, 4));
	    // 開始日時(月)
	    workUpdateRequest.setStartDateM(strDateS.substring(5, 7));
	    // 開始日時(日)
	    workUpdateRequest.setStartDateD(strDateS.substring(8, 10));
	    // 開始日時(時間)
	    workUpdateRequest.setStartDateHH(strDateS.substring(11, 13));
	    // 開始日時(分)
	    workUpdateRequest.setStartDateMI(strDateS.substring(14, 16));


	    // 終了日時
	    String strDateE = new SimpleDateFormat("yyyy/MM/dd HH:mm").format(work.getEndDate());

	    // 終了日時(年)
	    workUpdateRequest.setEndDateY(strDateE.substring(0, 4));
	    // 終了日時(月)
	    workUpdateRequest.setEndDateM(strDateE.substring(5, 7));
	    // 終了日時(日)
	    workUpdateRequest.setEndDateD(strDateE.substring(8, 10));
	    // 終了日時(時間)
	    workUpdateRequest.setEndDateHH(strDateE.substring(11, 13));
	    // 終了日時(分)
	    workUpdateRequest.setEndDateMI(strDateE.substring(14, 16));
*/

	    model.addAttribute("workUpdateRequest", workUpdateRequest);
	    return "work/edit";
	  }

	  /**
	   * 勤怠情報更新
	   * @param workRequest リクエストデータ
	   * @param model Model
	   * @return 勤怠情報詳細画面
	   */
	  @RequestMapping(value = "/work/update", method = RequestMethod.POST)
	  public String updateWork(@Validated @ModelAttribute WorkUpdateRequest workUpdateRequest, BindingResult result, Model model) {

	    if (result.hasErrors()) {
	      // 入力チェックエラーの場合
	      List<String> errorList = new ArrayList<String>();
	      for (ObjectError error : result.getAllErrors()) {
	        errorList.add(error.getDefaultMessage());
	      }
	      model.addAttribute("validationError", errorList);
	      return "work/edit";
	    }

	    // 勤怠情報の更新
		workService.update(workUpdateRequest, authUser.getId()); // ログイン時のユーザーIDをパラメータとして渡す。
/*
		try {
			workService.update(workUpdateRequest, authUser.getId()); // ログイン時のユーザーIDをパラメータとして渡す。
		} catch (Exception e) {
			e.printStackTrace();
		}
*/
	    return String.format("redirect:/work/%d", workUpdateRequest.getId());
	  }

	  /**
	   * 勤怠情報削除
	   * @param id 表示するユーザーID
	   * @param model Model
	   * @return 勤怠情報詳細画面
	   */
	  @GetMapping("/work/{id}/delete")
	  public String deleteWork(@PathVariable Long id, Model model) {

	    // 勤怠情報の削除
	    workService.delete(id);
	    return "redirect:/work/list";
	  }


	  /**
	   * 数値入力チェック（勤怠情報一覧画面の検索条件など）
	   * @param StrChk チェックする文字列
	   * @param StrMsg エラーチェック対象の項目名
	   * @param model Model
	   * @return 
	   */
		private boolean isValueCheck(String StrChk, String StrMsg, Model model) {
			
			// 入力チェック…開始日時(年・月・日)
			if (StrChk == null || StrChk.isEmpty()) {
		          model.addAttribute("validationError", StrMsg + "を入力して下さい。");
		          return false;
			}
			boolean isNumeric =  StrChk.matches("[+-]?\\d*(\\.\\d+)?");
			if (!isNumeric) {
		          model.addAttribute("validationError", StrMsg + "に正しい数値を入力して下さい。");
		          return false;
			}

			return true;
		}
}