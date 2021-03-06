package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.demo.auth.AuthUser;
import com.example.demo.dto.UserRequest;
import com.example.demo.dto.UserRequestSearch;
import com.example.demo.dto.UserUpdateRequest;
import com.example.demo.dto.UserUpdateRequestPass;
import com.example.demo.entity.SystemInfo;
import com.example.demo.entity.SystemMsg;
import com.example.demo.entity.User;
import com.example.demo.mdl.DateEdit;
import com.example.demo.service.UserService;

/**
 * User情報 Controller
 */
@Controller
public class UserController {
	/**
	* セッション情報（複数のコンローラ間で共有したいログイン情報などを格納する）
	*/
	@Autowired
	private HttpSession session;

	/**
	* ユーザー情報 Service
	*/
	@Autowired
	private UserService userService;

	/**
	 * ログイン情報
	 */
	private AuthUser authUser;

	/**
	* システム情報
	*/
	private SystemInfo systemInfo;

	/**
	 * 未入力項目はバリデーション（入力チェック）の対象外とするメソッド
	 * @param binder
	 */
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		// SpringBootの仕様により、未入力値は空白("")を埋める仕様らしいが、
		//未入力の項目は『null』が設定されるように、本メソッドでカイスタマイズをする。
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));

		// 補足説明
		// StringTrimmerEditor(true)　内の説明によると、
		//『emptyAsNull true if an empty String is to betransformed into null』
		//【和訳】『emptyAsNull 空の文字列をnullに変換する場合はtrue』とのことです。
	}

	/**
	 * システム情報の設定
	 * @param Model model
	 * @return
	 */
	private String setSysInfo(Model model) {

		String strRtnForm = null;

		// RootControllerクラスで設定した、セッション情報を取得。
		systemInfo = (SystemInfo)session.getAttribute("SessionSysInfo");
		// システム情報のパラメータを渡す。
		model.addAttribute("sysInfo", systemInfo);

		if (systemInfo == null) {
			// システム情報のセッション情報が取得できなかった場合、
			//システムエラー画面を表示させる。
			strRtnForm = "syserror";
		    model.addAttribute("validationError", SystemMsg.getErrMsg(SystemMsg.ERR_CODE_001));
			return strRtnForm;
		}

		return strRtnForm;
	}

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
   * ユーザー情報一覧画面を表示
   * @param model Model
   * @return ユーザー情報一覧画面
   */
  @GetMapping(value = "/user/list")
  public String displayList(Model model) {

	// ログイン情報の取得と設定。
	String strRtnForm = setAuthUser(model);
	if (strRtnForm != null) {
		// セッション情報の取得に失敗した場合
		//システムエラー画面を表示
		return strRtnForm;
	}


	// ユーザー情報一覧画面の検索処理のために、検索条件の入力項目の空データを作っておく。
	// ここで作成しておかないと、HTML側でnullエラーになる。
    model.addAttribute("userRequestSearch", new UserRequestSearch());

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
   * ユーザー情報一覧　検索
   * @param UserRequestSearch userRequestSearch（検索条件の入力項目）
   * @param model Model
   * @return ユーザー情報一覧画面
   */
  @RequestMapping(value = "/user/search", method = RequestMethod.POST)
  public String displayListUserSearch(@Validated @ModelAttribute UserRequestSearch userRequestSearch, BindingResult result, Model model) {

	// ログイン情報の取得と設定。
	String strRtnForm = setAuthUser(model);
	if (strRtnForm != null) {
		// セッション情報の取得に失敗した場合
		//システムエラー画面を表示
		return strRtnForm;
	}

	if (result.hasErrors()) {
	      // 入力チェックエラーの場合
	      List<String> errorList = new ArrayList<String>();
	      for (ObjectError error : result.getAllErrors()) {
	        errorList.add(error.getDefaultMessage());
	      }
	      model.addAttribute("validationError", errorList);
	      return "user/list";
	}


	List<User> userlist;

	// データ検索処理
	userlist = userService.searchUser(userRequestSearch);

	if (userlist.size() == 0) {
		// 該当データ無し。
//		model.addAttribute("msgSearchErr", SystemMsg.getInfoMsg(SystemMsg.INFO_CODE_001));
		model.addAttribute("msgSearchErr", "該当データがありません。");
	} else {
		// 該当件数の取得。
		Integer intCnt = Integer.valueOf(userlist.size());
//		model.addAttribute("msgSearchInfo", SystemMsg.getCustomInfoMsg(SystemMsg.C_INFO_CODE_001, intCnt.toString()));
		model.addAttribute("msgSearchInfo", "【検索結果】条件に該当するデータは" + intCnt.toString() + "件です。");
	}
	model.addAttribute("userlist", userlist);

	return "user/list";
  }


  /**
   * （ログイン⇒）ユーザー新規登録画面を表示
   * @param model Model
   * @return ログイン画面
   */
  @GetMapping(value = "/user/addlogin")
  public String displayAddLogin(Model model) {

	// システム情報の取得と設定。
	String strRtnForm = setSysInfo(model);
	if (strRtnForm != null) {
		// セッション情報の取得に失敗した場合
		//システムエラー画面を表示
		return strRtnForm;
	}

    model.addAttribute("userRequest", new UserRequest());
    return "user/addlogin";
  }

  /**
   * （ログイン⇒）ユーザー新規登録
   * @param userRequest リクエストデータ
   * @param model Model
   * @return ログイン画面
   */
  @RequestMapping(value = "/user/createlogin", method = RequestMethod.POST)
  public String createLogin(@Validated @ModelAttribute UserRequest userRequest, BindingResult result, Model model) {

	// システム情報の取得と設定。
	String strRtnForm = setSysInfo(model);
	if (strRtnForm != null) {
		// セッション情報の取得に失敗した場合
		//システムエラー画面を表示
		return strRtnForm;
	}

	if (result.hasErrors()) {
      // 入力チェックエラーの場合
      List<String> errorList = new ArrayList<String>();
      for (ObjectError error : result.getAllErrors()) {
        errorList.add(error.getDefaultMessage());
      }
      model.addAttribute("validationError", errorList);
      return "user/addlogin";
    }

    // 新規登録しようしたメールアドレスが、データベースに存在してないかを検索
    Integer intCnt = userService.findByEmailCnt(userRequest.getEmail());

	if ( intCnt > 0) {
		// データベースに同じメールアドレスが存在していた場合。
	    List<String> errorList = new ArrayList<String>();
        errorList.add("既に登録済のメールアドレスです。");
//        errorList.add(SystemMsg.getErrMsg(SystemMsg.ERR_CODE_104));
        model.addAttribute("validationError", errorList);
	    
	    return "user/addlogin";
	}


    // ユーザー情報の登録
    userService.createLogin(userRequest);

    // (ログイン画面側から新規登録の場合は、強制的にログイン画面へ一度戻されてから、再ログインの流れになります。)
    return "login"; // ログイン画面に戻る
  }

  /**
   * ユーザー新規登録画面を表示
   * @param model Model
   * @return ユーザー情報一覧画面
   */
  @GetMapping(value = "/user/add")
  public String displayAdd(Model model) {

	// ログイン情報の取得と設定。
	String strRtnForm = setAuthUser(model);
	if (strRtnForm != null) {
		// セッション情報の取得に失敗した場合
		//システムエラー画面を表示
		return strRtnForm;
	}

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

	// ログイン情報の取得と設定。
	String strRtnForm = setAuthUser(model);
	if (strRtnForm != null) {
		// セッション情報の取得に失敗した場合
		//システムエラー画面を表示
		return strRtnForm;
	}

//	    int intCnt = 0; // デバッグ用
//	    String strX = ""; // デバッグ用

	if (result.hasErrors()) {
      // 入力チェックエラーの場合
      List<String> errorList = new ArrayList<String>();
      for (ObjectError error : result.getAllErrors()) {
        errorList.add(error.getDefaultMessage());
//	        strX = errorList.get(intCnt); // デバッグ用
//	        intCnt++; // デバッグ用
      }
      model.addAttribute("validationError", errorList);
      return "user/add";
    }

    // 新規登録しようしたメールアドレスが、データベースに存在してないかを検索
    Integer intCnt = userService.findByEmailCnt(userRequest.getEmail());
    
	if ( intCnt > 0) {
		// データベースに同じメールアドレスが存在していた場合。
//		    model.addAttribute("validationError", "既に登録済のメールアドレスです。");
	    List<String> errorList = new ArrayList<String>();
//        errorList.add(SystemMsg.getErrMsg(SystemMsg.ERR_CODE_104));
        errorList.add("既に登録済のメールアドレスです。");
        model.addAttribute("validationError", errorList);

	    return "user/add";
	}


    // ユーザー情報の登録
    userService.create(userRequest);

    // ユーザー情報一覧画面に戻る
    return "redirect:/user/list";
  }



  /**
   * ユーザー情報詳細の画面表示用データの作成
   * @param User user
   * @return ユーザー情報詳細の画面表示用データ
   */
  private UserUpdateRequest getUserUpdateRequest(User user) {

	    UserUpdateRequest userUpdateRequest = new UserUpdateRequest();
	    userUpdateRequest.setId(user.getId());
	    userUpdateRequest.setName(user.getName());
	    userUpdateRequest.setEmail(user.getEmail());
//	    userUpdateRequest.setPassword(user.getPassword());
	    // 性別（取得したコードを、文字列情報に変換する）
	    userUpdateRequest.setGender(User.getChgGender(user.getGender()));
	    userUpdateRequest.setAddress(user.getAddress());
	    userUpdateRequest.setPhone(user.getPhone());

	    if (user.getBirthday() == null) {
		    // 生年月日(年)
	    	userUpdateRequest.setBirthdayY("");
		    // 生年月日(月)
	    	userUpdateRequest.setBirthdayM("");
		    // 生年月日(日)
	    	userUpdateRequest.setBirthdayD("");	    	

	    } else {
		    // 生年月日(年)
		    userUpdateRequest.setBirthdayY(DateEdit.getDate(user.getBirthday(), "yyyy"));
		    // 生年月日(月)
		    userUpdateRequest.setBirthdayM(DateEdit.getDate(user.getBirthday(), "M"));
		    // 生年月日(日)
		    userUpdateRequest.setBirthdayD(DateEdit.getDate(user.getBirthday(), "d"));
	//	    userUpdateRequest.setBirthday(user.getBirthday());
	    }

	    if (user.getAge() == null) {
	    	userUpdateRequest.setAge("");
	    } else {
	    	userUpdateRequest.setAge(user.getAge());	    	
	    }

	    return userUpdateRequest;
  }


  /**
   * ユーザー情報詳細画面を表示
   * @param id 表示するユーザーID
   * @param model Model
   * @return ユーザー情報詳細画面
   */
  @GetMapping("/user/{id}")
  public String displayView(@PathVariable Long id, Model model) {

	// ログイン情報の取得と設定。
	String strRtnForm = setAuthUser(model);
	if (strRtnForm != null) {
		// セッション情報の取得に失敗した場合
		//システムエラー画面を表示
		return strRtnForm;
	}

    User user = userService.findById(id);
//  model.addAttribute("userData", user);

    // ユーザー情報詳細の画面表示用データの作成
    UserUpdateRequest userUpdateRequest = getUserUpdateRequest(user);
    model.addAttribute("userData", userUpdateRequest);

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

	// ログイン情報の取得と設定。
	String strRtnForm = setAuthUser(model);
	if (strRtnForm != null) {
		// セッション情報の取得に失敗した場合
		//システムエラー画面を表示
		return strRtnForm;
	}

    User user = userService.findById(id);
    // ユーザー情報詳細の画面表示用データの作成
    UserUpdateRequest userUpdateRequest = getUserUpdateRequest(user);
    model.addAttribute("userUpdateRequest", userUpdateRequest);

    //性別のセレクトボックス（画面情報用）データの作成。
    List<String> GenderList = User.newGenderList();
    model.addAttribute("allGender", GenderList);


    if (!isAuthRoleCheck(id)) {
    // 権限チェックがエラーの場合、更新処理はさせない。
      model.addAttribute("validationError", "管理者権限が無いため、編集はできません。");
//      model.addAttribute("validationError", SystemMsg.getErrMsg(SystemMsg.ERR_CODE_101));
      model.addAttribute("userData", userUpdateRequest);
      // ユーザー詳細画面を表示。
      return "user/view";
    }


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

	// ログイン情報の取得と設定。
	String strRtnForm = setAuthUser(model);
	if (strRtnForm != null) {
		// セッション情報の取得に失敗した場合
		//システムエラー画面を表示
		return strRtnForm;
	}

	// 入力チェックでエラーになった時用に、
    //性別のセレクトボックス（画面情報用）データをここで作成しておく。
    List<String> GenderList = User.newGenderList();
    model.addAttribute("allGender", GenderList);
//    model.addAttribute("userUpdateRequest", userUpdateRequest);

//	    long lngCnt = 0; // デバッグ用
//	    String strX = ""; // デバッグ用

    if (result.hasErrors()) {
      // 入力チェックエラーの場合
      List<String> errorList = new ArrayList<String>();
      for (ObjectError error : result.getAllErrors()) {
        errorList.add(error.getDefaultMessage());
//	      strX = userUpdateRequest.getEmail(); // デバッグ用
//	      lngCnt = userUpdateRequest.getId(); // デバッグ用
      }
      model.addAttribute("validationError", errorList);
      return "user/edit";
    }

	boolean isDateYMD = false; // 日付チェック用(false：正しい日付形式ではない)

	// 生年月日の入力チェック

//	if (userUpdateRequest.getBirthdayY().isEmpty()
//			&& userUpdateRequest.getBirthdayM().isEmpty()
//			&& userUpdateRequest.getBirthdayD().isEmpty() ) {
	if (userUpdateRequest.getBirthdayY() == null
			&& userUpdateRequest.getBirthdayM() == null
			&& userUpdateRequest.getBirthdayD() == null ) {
		// 生年月日が空白の場合

	} else {
		isDateYMD = DateEdit.isDate(userUpdateRequest.getBirthdayY(), userUpdateRequest.getBirthdayM(), userUpdateRequest.getBirthdayD());
		if (!isDateYMD) {
	        model.addAttribute("validationError", "生年月日に正しい日付を入力して下さい。");
//	        model.addAttribute("validationError", SystemMsg.getErrMsg(SystemMsg.ERR_CODE_105));
	        return "user/edit";
		}

	}

    // 更新しようしたメールアドレスが、データベースに存在してないかを検索
    Integer intCnt = userService.findByEmailCnt(userUpdateRequest.getId(), userUpdateRequest.getEmail());
    
	if ( intCnt > 0) {
		  // データベースに同じメールアドレスが存在していた場合。
	    List<String> errorList = new ArrayList<String>();
        errorList.add("既に登録済のメールアドレスです。");
//        errorList.add(SystemMsg.getErrMsg(SystemMsg.ERR_CODE_104));
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

	// ログイン情報の取得と設定。
	String strRtnForm = setAuthUser(model);
	if (strRtnForm != null) {
		// セッション情報の取得に失敗した場合
		//システムエラー画面を表示
		return strRtnForm;
	}

    User user = userService.findById(id);


    if (!isAuthRoleCheck(id)) {
    // 権限チェックがエラーの場合、更新処理はさせない。
      model.addAttribute("validationError", "管理者権限が無いため、パスワードの編集はできません。");
//      model.addAttribute("validationError", SystemMsg.getErrMsg(SystemMsg.ERR_CODE_102));

      // ユーザー情報詳細の画面表示用データの作成
      UserUpdateRequest userUpdateRequest = getUserUpdateRequest(user);

      model.addAttribute("userData", userUpdateRequest);
      // ユーザー詳細画面を表示。
      return "user/view";
    }


    UserUpdateRequestPass userUpdateRequestPass = new UserUpdateRequestPass();

    userUpdateRequestPass.setId(user.getId());
    // パスワードの情報は、セキュリィ対策として保持しない
//	    userUpdateRequest.setPassword(user.getPassword());

    model.addAttribute("userUpdateRequestPass", userUpdateRequestPass);
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

	// ログイン情報の取得と設定。
	String strRtnForm = setAuthUser(model);
	if (strRtnForm != null) {
		// セッション情報の取得に失敗した場合
		//システムエラー画面を表示
		return strRtnForm;
	}

    if (result.hasErrors()) {
      List<String> errorList = new ArrayList<String>();
      for (ObjectError error : result.getAllErrors()) {
        errorList.add(error.getDefaultMessage());
      }
      model.addAttribute("validationError", errorList);
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

	// ログイン情報の取得と設定。
	String strRtnForm = setAuthUser(model);
	if (strRtnForm != null) {
		// セッション情報の取得に失敗した場合
		//システムエラー画面を表示
		return strRtnForm;
	}


	boolean blnErrCnk = true;
	Long lngAuthId = authUser.getId(); // ログイン情報のユーザーIDを取得

    if (!isAuthRoleCheck(id)) {
    // 権限チェックがエラーの場合、削除処理はさせない。
      model.addAttribute("validationError", "管理者権限が無いため、削除はできません。");
//      model.addAttribute("validationError", SystemMsg.getErrMsg(SystemMsg.ERR_CODE_103));
      blnErrCnk = false;
    }

    if (lngAuthId.equals(id)) {
    // ログイン中のユーザーと、削除対象のユーザーが同じだった場合。
      model.addAttribute("validationError", "ログイン中のユーザーは、削除できません。");
//      model.addAttribute("validationError", SystemMsg.getErrMsg(SystemMsg.ERR_CODE_106));
      blnErrCnk = false;
    }

    if (!blnErrCnk) {
    	// エラーチェックに、エラーがあった場合
        User user = userService.findById(id);
        // ユーザー情報詳細の画面表示用データの作成
        UserUpdateRequest userUpdateRequest = getUserUpdateRequest(user);
        model.addAttribute("userData", userUpdateRequest);
//        model.addAttribute("userData", user);
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
}
