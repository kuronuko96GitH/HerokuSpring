package com.example.demo.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import com.example.demo.dto.WorkUpdateRequest;
import com.example.demo.entity.User;
import com.example.demo.entity.Work;
import com.example.demo.service.UserService;
import com.example.demo.service.WorkService;

/**
 * ユーザー情報 Controller
 */
@Controller
public class UserController {
// 補足説明：
// Controllerアノテーションを付けたクラスで、
// index.htmlなどを呼び出す、ルート管理の処理を作成する。

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
   * トップページを表示
   * @param model Model
   * @return トップページ
   * 補足(ローカル環境)：URL…http://localhost:8080/
   */
  @GetMapping(value = "/")
  public String root(@AuthenticationPrincipal AuthUser userDetails) {

	  // 他のコントローラ処理でも、ログイン情報を扱えるように、
	  // トップページにアクセスしたタイミングで、ログイン情報を取得。
	  authUser = userDetails;
	  // authUser.getId();
	  // authUser.getEmail();
	  // authUser.getUsername();
	  // authUser.getRoles().get(0);	// (出力例)『ROLE_USER』

//      System.out.println(authUser.getRoles()); // List<String>…(出力例)『[ROLE_USER]』

	  return "index";
  }
/*
  @GetMapping(value = "/")
  public String root() {
	  return "index";
  }
*/
  @PostMapping(value = "/index")
  public String index() {
	  return "index";
  }
  
/*
@RequestMapping(value = "/", method = RequestMethod.GET)
public String helloWorld(Model model) {
  model.addAttribute("message", "Hello World!!");
  return "index";
}
*/
  

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
  public String sample2() {
	  return "sample2";
  }




  /**
   * About Me画面を表示
   * @param model Model
   * @return About Me画面
   * 補足(ローカル環境)：URL…http://localhost:8080/others/aboutme.html
   */
  @GetMapping(value = "/others/aboutme")
  public String aboutme() {
	  return "others/aboutme";
  }

  /**
   * Link画面を表示
   * @param model Model
   * @return Link画面
   */
  @GetMapping(value = "/others/portfolio")
  public String portfolio() {
	  return "others/portfolio";
  }

  /**
   * 開発ドキュメント画面を表示
   * @param model Model
   * @return 開発ドキュメント画面
   */
  @GetMapping(value = "/others/devdoc")
  public String devdoc() {
	  return "others/devdoc";
  }



  /**
   * ユーザー情報一覧画面を表示
   * @param model Model
   * @return ユーザー情報一覧画面
   */
  @GetMapping(value = "/user/list")
  public String displayList(Model model) {

	// 検索画面に、ログイン情報のパラメータを渡す。
	model.addAttribute("authId", authUser.getId());
	model.addAttribute("authName", authUser.getUsername());

	// ユーザー情報の全検索
	List<User> userlist = userService.searchAll();
	model.addAttribute("userlist", userlist);

	return "user/list";
  }

  /**
   * ユーザー新規登録画面を表示
   * @param model Model
   * @return ユーザー情報一覧画面
   */
  @GetMapping(value = "/user/add")
  public String displayAdd(Model model) {
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

/*
    // 新規登録しようした名前が、データベースに存在してないかを検索
	Integer intCnt = userService.findByNameCnt(userRequest.getName());

    if ( intCnt > 0) {
		  // データベースに同じ名前が存在していた場合。
//	    model.addAttribute("validationError", "既に登録済の名前です。");

	    List<String> errorList = new ArrayList<String>();
      errorList.add("既に登録済の名前です。");
      model.addAttribute("validationError", errorList);
	    
	    return "user/add";  
	}
*/
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
    

/*
    // 更新しようしたメールアドレスが、データベースに存在してないかを検索
    Integer intCnt = userService.findByEmailCnt(userUpdateRequest.getEmail());
    
	if ( intCnt > 0) {
		  // データベースに同じメールアドレスが存在していた場合。
//	    model.addAttribute("validationError", "既に登録済のメールアドレスです。");

	    List<String> errorList = new ArrayList<String>();
        errorList.add("既に登録済のメールアドレスです。");
        model.addAttribute("validationError", errorList);
	    
	    return "user/edit";  
	}
*/

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
	   * @return ユーザー情報一覧画面
	   */
	  @GetMapping(value = "/work/list")
	  public String displayListWork(Model model) {

		// 検索画面に、ログイン情報のパラメータを渡す。
		model.addAttribute("authId", authUser.getId());
		model.addAttribute("authName", authUser.getUsername());

		// ユーザー情報の全検索
//		List<Work> worklist = workService.searchAll();
		List<Work> worklist = workService.findByUserID(authUser.getId());
		model.addAttribute("worklist", worklist);

		return "work/list";
	  }

	  /**
	   * 勤怠情報新規登録画面を表示
	   * @param model Model
	   * @return 勤怠情報一覧画面
	   */

	  @GetMapping(value = "/work/add")
	  public String displayAddWork(Model model) {

/*
//	    Work work = userService.findById(id);
	    WorkRequest workRequest = new WorkRequest();
	    workRequest.setUserId(authUser.getId()); // ログイン時のユーザーIDを設定。
	    model.addAttribute("workRequest", workRequest);
*/
	    model.addAttribute("workRequest", new WorkRequest());
		    
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
//		  public String createWork(@Validated @ModelAttribute WorkRequest workRequest, BindingResult result, Model model) {

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
	      return "work/add";
	    }

/*
	    // 新規登録しようしたメールアドレスが、データベースに存在してないかを検索
	    Integer intCnt = workService.findByEmailCnt(workRequest.getEmail());
	    
		if ( intCnt > 0) {
			  // データベースに同じメールアドレスが存在していた場合。
//		    model.addAttribute("validationError", "既に登録済のメールアドレスです。");

		    List<String> errorList = new ArrayList<String>();
	        errorList.add("既に登録済のメールアドレスです。");
	        model.addAttribute("validationError", errorList);
		    
		    return "work/add";  
		}
*/

	    // 勤怠情報の新規登録
		try {
		    workService.create(workRequest, authUser.getId()); // ログイン時のユーザーIDを設定。
//		    workService.create(workRequest);
		} catch (Exception e) {
			e.printStackTrace();
		}
//	    workService.create(workRequest);
		
		
//	    return "login"; // 管理者ではないユーザーの新規登録の場合は、ログイン画面に戻る？
	    return "redirect:/work/list";
	  }

	  /**
	   * 勤怠情報詳細画面を表示
	   * @param id 表示するワークID
	   * @param model Model
	   * @return 勤怠情報詳細画面
	   */
	  @GetMapping("/work/{id}")
	  public String displayViewWork(@PathVariable Long id, Model model) {
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
	    Work work = workService.findById(id);
	    WorkUpdateRequest workUpdateRequest = new WorkUpdateRequest();
	    workUpdateRequest.setId(work.getId());
//	    workUpdateRequest.setUserID(authUser.getId()); // ログイン情報のユーザーIDをパラメータで渡す。
	    workUpdateRequest.setContent(work.getContent());

	    // 開始日時
//	    workUpdateRequest.setStartDate(work.getStartDate());
	    String strDateS = new SimpleDateFormat("yyyy/MM/dd HH:mm").format(work.getStartDate());
	    workUpdateRequest.setStartDate(strDateS);


	    // 終了日時
//	    workUpdateRequest.setEndDate(work.getEndDate());
	    String strDateE = new SimpleDateFormat("yyyy/MM/dd HH:mm").format(work.getEndDate());
	    workUpdateRequest.setEndDate(strDateE);

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
//	    long lngCnt = 0; // デバッグ用
//	    String strX = ""; // デバッグ用

	    if (result.hasErrors()) {
	      // 入力チェックエラーの場合
	      List<String> errorList = new ArrayList<String>();
	      for (ObjectError error : result.getAllErrors()) {
	        errorList.add(error.getDefaultMessage());
//	      strX = workUpdateRequest.getEmail(); // デバッグ用
//	      lngCnt = workUpdateRequest.getId(); // デバッグ用
	      }
	      model.addAttribute("validationError", errorList);
	      return "work/edit";
	    }

	    // 勤怠情報の更新
		try {
			workService.update(workUpdateRequest, authUser.getId()); // ログイン時のユーザーIDをパラメータとして渡す。
//		    workService.update(workUpdateRequest);
		} catch (Exception e) {
			e.printStackTrace();
		}
	    
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
/*
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

	        Work work = workService.findById(id);
	        model.addAttribute("workData", work);
	        // ユーザー詳細画面を表示。
	        return "work/view";
	    }
*/

	    // 勤怠情報の削除
	    workService.delete(id);
	    return "redirect:/work/list";
	  }

}