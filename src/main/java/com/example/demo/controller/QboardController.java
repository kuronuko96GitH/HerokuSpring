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
import com.example.demo.dto.QboardRequest;
import com.example.demo.entity.Qboard;
import com.example.demo.entity.SystemInfo;
import com.example.demo.entity.SystemMsg;
import com.example.demo.service.QboardService;

/**
 * Qboard情報 Controller
 */
@Controller
public class QboardController {
	/**
	* セッション情報（複数のコンローラ間で共有したいログイン情報などを格納する）
	*/
	@Autowired
	private HttpSession session;

	/**
	* 質問板(Qboard)情報 Service
	*/
	@Autowired
	private QboardService qboardService;

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
	 * システム情報とログイン情報の設定(前画面ID情報も取得)
	 * @param Model model
	 * @return
	 */
	private String setAuthUser(Model model, String backid) {

		String strRtnForm = null;

		// RootControllerクラスで設定した、セッション情報を取得。
		systemInfo = (SystemInfo)session.getAttribute("SessionSysInfo");
		// システム情報のパラメータを渡す。
		model.addAttribute("sysInfo", systemInfo);



		// RootControllerクラスで設定した、セッション情報を取得。
		authUser = (AuthUser)session.getAttribute("SessionAuthUser");

		if (backid != null) {
			// 前画面IDのパラメータを設定する。
			authUser.setBackId(backid);

			// 例えば、詳細ボタンのある①勤退一覧画面、②打刻登録画面、③報酬計算画面の
			// どれか３つから詳細ボタンを押した時に、
			// 画面遷移して来た、一つ前の画面が分からなくなるので、
			// このタイミングで前画面IDの設定をする。
		}

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
   * 質問板ヘッダ情報一覧画面を表示
   * @param model Model
   * @return 質問板ヘッダ情報一覧画面
   */
  @GetMapping(value = "/qboard/headlist")
  public String displayHeadListQboard(Model model) {

	// ログイン情報の取得と設定。(このタイミングで、前画面IDも設定しておく)
//	setAuthUser(model, "list");
	String strRtnForm = setAuthUser(model, null);
//	String strRtnForm = setAuthUser(model, "list");
	if (strRtnForm != null) {
		// セッション情報の取得に失敗した場合
		//システムエラー画面を表示
		return strRtnForm;
	}
/*
	// 質問板ヘッダ情報一覧画面(勤退年月)検索のために、検索条件の年月日の空データを作っておく。
	// ここで作成しておかないと、HTML側でnullエラーになる。
    model.addAttribute("qboardRequestSearch", new QboardRequestSearch());
*/

	// 質問板ヘッダ情報の検索
	List<Qboard> qboardheadlist = qboardService.findList();
//	List<QboardH> qboardheadlist = qboardHService.findByUserID(authUser.getId());
	model.addAttribute("qboardheadlist", qboardheadlist);


	return "qboard/headlist";
  }




  /**
   * 質問板情報一覧画面を表示
   * @param model Model
   * @return 質問板情報一覧画面
   */
//  @GetMapping(value = "/qboard/list{headId}")
  @GetMapping(value = "/qboard/{headId}")
  public String displayListQboard(@PathVariable Integer headId, Model model) {

	// ログイン情報の取得と設定。(このタイミングで、前画面IDも設定しておく)
//	setAuthUser(model, "list");
	String strRtnForm = setAuthUser(model, null);
//	String strRtnForm = setAuthUser(model, "list");
	if (strRtnForm != null) {
		// セッション情報の取得に失敗した場合
		//システムエラー画面を表示
		return strRtnForm;
	}

	// 投稿処理用のヘッダIDを、サービスクラスで取得しておく。
	qboardService.setHeadId(headId);

/*
	// 質問板情報一覧画面(勤退年月)検索のために、検索条件の年月日の空データを作っておく。
	// ここで作成しておかないと、HTML側でnullエラーになる。
    model.addAttribute("qboardRequestSearch", new QboardRequestSearch());
*/

	// 質問板情報の検索
	List<Qboard> qboardlist = qboardService.findBodyList(headId);
//	List<QboardH> qboardheadlist = qboardHService.findByUserID(authUser.getId());
	model.addAttribute("qboardlist", qboardlist);


	return "qboard/list";
  }



  /**
   * 質問板情報の新規登録画面を表示
   * @param model Model
   * @return 質問板情報一覧画面
   */
  @GetMapping(value = "/qboard/new")
  public String displayNewQboard(Model model) {

	// ログイン情報の取得と設定。
	String strRtnForm = setAuthUser(model, null);
	if (strRtnForm != null) {
		// セッション情報の取得に失敗した場合
		//システムエラー画面を表示
		return strRtnForm;
	}

	// 投稿処理用のヘッダIDは、新規質問投稿時にはサービスクラスに０を設定しておく。
	qboardService.setHeadId(0);
	// 投稿処理用のヘッダIDを、サービスクラスで取得しておく。
//	qboardService.setHeadId(headId);

    model.addAttribute("qboardRequest", new QboardRequest());
	    
    return "qboard/add";
//    return "qboard/new";
  }


  /**
   * 質問板情報新規登録
   * @param qboardRequest リクエストデータ
   * @param model Model
   * @return 質問板情報一覧画面
   */
 /*
  @RequestMapping(value = "/qboard/create", method = RequestMethod.POST)
  public String createQboard(@Validated @ModelAttribute QboardRequest qboardRequest, BindingResult result, Model model) {

	// ログイン情報の取得と設定。
	String strRtnForm = setAuthUser(model, null);
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
      return "qboard/add";
    }


    // 質問板情報の新規登録（質問の投稿）
	qboardService.create(qboardRequest, authUser.getId(), 0);

  return "redirect:/qboard/headlist";
//    return "redirect:/qboard/list";
  }
*/




  /**
   * 質問板情報の返信投稿画面を表示
   * @param model Model
   * @return 質問板情報一覧画面
   */
//  @GetMapping(value = "/qboard/add{headId}")
  //  public String displayAddQboard(@PathVariable Integer headId, Model model) {
  @GetMapping(value = "/qboard/add")
  public String displayAddQboard(Model model) {

	// ログイン情報の取得と設定。
	String strRtnForm = setAuthUser(model, null);
	if (strRtnForm != null) {
		// セッション情報の取得に失敗した場合
		//システムエラー画面を表示
		return strRtnForm;
	}

    model.addAttribute("qboardRequest", new QboardRequest());
	    
    return "qboard/add";
  }


  /**
   * 質問板情報新規登録
   * @param qboardRequest リクエストデータ
   * @param model Model
   * @return 質問板情報一覧画面
   */
  @RequestMapping(value = "/qboard/create", method = RequestMethod.POST)
  public String createNextQboard(@Validated @ModelAttribute QboardRequest qboardRequest, BindingResult result, Model model) {

	// ログイン情報の取得と設定。
	String strRtnForm = setAuthUser(model, null);
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
      return "qboard/add";
    }

	// 投稿処理用のヘッダIDを、サービスクラスで取得しておく。
//	qboardService.setHeadId(headId);

    // 質問板情報の追加投稿データ登録
	qboardService.create(qboardRequest, authUser.getId(), qboardService.getHeadId()); // ログイン時のユーザーIDを設定。
//	qboardService.create(qboardRequest, authUser.getId()); // ログイン時のユーザーIDを設定。
/*
	if (authUser.getBackId().equals("list")) {
		// 前画面IDが"list"(勤退一覧画面)。
	    return "redirect:/qboard/list";
	} else {

		// 前画面IDが"stamping"(打刻登録画面)。
	    return "redirect:/qboard/stamping";		
	}
*/
  return "redirect:/qboard/headlist";
//    return "redirect:/qboard/list";
  }



  /**
   * 質問板情報削除
   * @param id 削除するユーザーID
   * @param model Model
   * @return 質問板情報詳細画面
   */
  @GetMapping("/qboard/{id}/delete")
  public String deleteQboard(@PathVariable Long id, Model model) {

	  // 既に投稿したデータの削除
	  qboardService.delUpdate(id, authUser.getId()); // ログイン時のユーザーIDをパラメータとして渡す。
//	  qboardService.update(qboardUpdateRequest, authUser.getId()); // ログイン時のユーザーIDをパラメータとして渡す。

	  return "redirect:/qboard/headlist";
//	  return String.format("redirect:/qboard/%d", qboardUpdateRequest.getId());

/*
    // 質問板情報の削除
    workService.delete(id);


	if (authUser.getBackId().equals("stamping")) {
		// 前画面IDが"stamping"(打刻登録画面)。
	    return "redirect:/work/stamping";		

	} else if (authUser.getBackId().equals("reward")) {
		// 前画面IDが"reward"(報酬計算画面)。
		return "redirect:/work/reward";

	} else {
		// 前画面IDが"list"(勤退一覧画面)。
		return "redirect:/work/list";
	}
*/
  }

}
