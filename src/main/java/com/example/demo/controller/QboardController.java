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
import com.example.demo.dto.QboardRequestSearch;
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

	// 質問板ヘッダ情報一覧画面の検索ボタン処理のために、検索条件の入力項目の空データを作っておく。
	// ここで作成しておかないと、HTML側でnullエラーになる。
    model.addAttribute("qboardRequestSearch", new QboardRequestSearch());


	// 質問板一覧画面の初期表示は、検索処理をしない。
	List<Qboard> qboardheadlist = null;
//	// 質問板ヘッダ情報の検索
//	List<Qboard> qboardheadlist = qboardService.findList();

	model.addAttribute("qboardheadlist", qboardheadlist);


	return "qboard/headlist";
  }




  /**
   * 質問板（ボディ）情報一覧画面を表示
   * @param model Model
   * @return 質問板情報一覧画面
   */
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

	// 質問板情報の検索
	List<Qboard> qboardlist = qboardService.findBodyList(headId);
	model.addAttribute("qboardlist", qboardlist);


	return "qboard/list";
  }



  /**
   * 質問板情報一覧　検索
   * @param QboardRequestSearch qboardRequestSearch（検索条件の入力項目）
   * @param model Model
   * @return 質問板情報一覧画面
   */
  @RequestMapping(value = "/qboard/search", method = RequestMethod.POST)
  public String displayListWorkSearch(@Validated @ModelAttribute QboardRequestSearch qboardRequestSearch, BindingResult result, Model model) {

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
	      return "qboard/headlist";
	}


	List<Qboard> qboardheadlist;

	// データ検索処理
	qboardheadlist = qboardService.searchQboard(authUser.getId(), qboardRequestSearch);


	if (qboardheadlist.size() == 0) {
		// 該当データ無し。
		model.addAttribute("msgSearchErr", "該当データがありません。");
//		model.addAttribute("msgInfo", "該当データがありません。");
	} else {
		// 該当件数の取得。
		Integer intCnt = Integer.valueOf(qboardheadlist.size());
		model.addAttribute("msgSearchInfo", "【検索結果】条件に該当するデータは" + intCnt.toString() + "件です。");
//		model.addAttribute("msgSearchInfo", "【検索結果】" + intCnt.toString() + "件のデータがありました。");
	}
	model.addAttribute("qboardheadlist", qboardheadlist);

	return "qboard/headlist";
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

	// 投稿情報の作成
//    model.addAttribute("qboardRequest", new QboardRequest());
	QboardRequest qboardRequest = new QboardRequest();
	qboardRequest.setHeadId(qboardService.getHeadId()); // キャンセルボタン用に、ヘッダIDをパラメータとして設定しておく。
    model.addAttribute("qboardRequest", qboardRequest);

	// 追加投稿対象のボディデータの検索
	List<Qboard> qboardlist = qboardService.findBodyList(qboardService.getHeadId());
	model.addAttribute("qboardlist", qboardlist);


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

	// 追加投稿対象のボディデータの検索
	List<Qboard> qboardlist = qboardService.findBodyList(qboardService.getHeadId());
	model.addAttribute("qboardlist", qboardlist);

	if (result.hasErrors()) {
      // 入力チェックエラーの場合
      List<String> errorList = new ArrayList<String>();
      for (ObjectError error : result.getAllErrors()) {
        errorList.add(error.getDefaultMessage());
      }
      model.addAttribute("validationError", errorList);
      return "qboard/add";
    }

    // 質問板情報の追加投稿データ登録
	// （新規登録時は、ヘッダIDは０が設定されてます。データ作成時にヘッダIDを採番します）
	qboardService.create(qboardRequest, authUser.getId(), qboardService.getHeadId());

	if ( qboardService.getHeadId() == 0 ) { 
		// 新規登録（質問投稿）の場合
		
		// 新規登録されたデータのヘッドIDの取得。
		qboardService.setHeadId(qboardService.getMaxHeadId());		
//		return "redirect:/qboard/headlist";
	}

	return String.format("redirect:/qboard/%d", qboardService.getHeadId());
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
	  qboardService.delUpdate(id, authUser.getId(), 9); // ログイン時のユーザーIDをパラメータとして渡す。
//	  qboardService.update(qboardUpdateRequest, authUser.getId()); // ログイン時のユーザーIDをパラメータとして渡す。

//	  return "redirect:/qboard/headlist";
	  return String.format("redirect:/qboard/%d", qboardService.getHeadId());

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
		// 前画面IDが"list"(質問板一覧画面)。
		return "redirect:/work/list";
	}
*/
  }



  /**
   * 質問板情報削除（荒らしなどによる違反者投稿の削除）
   * @param id 削除するユーザーID
   * @param model Model
   * @return 質問板情報詳細画面
   */
  @GetMapping("/qboard/{id}/deleteAdmin")
  public String deleteQboardAdmin(@PathVariable Long id, Model model) {

	  // 既に投稿したデータの削除
	  qboardService.delUpdate(id, authUser.getId(), 8); // ログイン時のユーザーIDをパラメータとして渡す。
//	  qboardService.update(qboardUpdateRequest, authUser.getId()); // ログイン時のユーザーIDをパラメータとして渡す。

//	  return "redirect:/qboard/headlist";
	  return String.format("redirect:/qboard/%d", qboardService.getHeadId());

  }



  /**
   * 質問板情報　ペナルティ削除データの解除
   * （管理者のミスなどにより、誤って削除したデータを復活させる）
   * @param id 削除するユーザーID
   * @param model Model
   * @return 質問板情報詳細画面
   */
  @GetMapping("/qboard/{id}/releaseAdmin")
  public String releaseQboardAdmin(@PathVariable Long id, Model model) {

	  // 管理者のミスなどにより、誤って削除したデータを復活させる
	  qboardService.delUpdate(id, authUser.getId(), 1); // 状態コードを１（未削除）パラメータとして渡す。
//	  qboardService.update(qboardUpdateRequest, authUser.getId()); // ログイン時のユーザーIDをパラメータとして渡す。

//	  return "redirect:/qboard/headlist";
	  return String.format("redirect:/qboard/%d", qboardService.getHeadId());

  }

}
