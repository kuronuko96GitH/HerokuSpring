package com.example.demo.controller;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
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
import com.example.demo.dto.WorkRequest;
import com.example.demo.dto.WorkRequestReward;
import com.example.demo.dto.WorkRequestSearch;
import com.example.demo.dto.WorkUpdateRequest;
import com.example.demo.entity.SystemInfo;
import com.example.demo.entity.Work;
import com.example.demo.mdl.DateEdit;
import com.example.demo.mdl.DateRange;
import com.example.demo.mdl.DateTimeRange;
import com.example.demo.service.WorkService;

/**
 * Work情報 Controller
 */
@Controller
public class WorkController {
	/**
	* セッション情報（複数のコンローラ間で共有したいログイン情報などを格納する）
	*/
	@Autowired
	private HttpSession session;

	/**
	* 勤退(work)情報 Service
	*/
	@Autowired
	private WorkService workService;

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
	 * ログイン情報の設定
	 * @param Model model
	 * @return
	 */
	private void setAuthUser(Model model) {

		// RootControllerクラスで設定した、セッション情報を取得。
		systemInfo = (SystemInfo)session.getAttribute("SessionSysInfo");
		// システム情報のパラメータを渡す。
		model.addAttribute("sysInfo", systemInfo);


		// RootControllerクラスで設定した、セッション情報を取得。
		authUser = (AuthUser)session.getAttribute("SessionAuthUser");
		// ログイン情報のパラメータを渡す。
		model.addAttribute("authUser", authUser);
	}

	/**
	 * ログイン情報の設定(前画面ID情報も取得)
	 * @param Model model
	 * @return
	 */
	private void setAuthUser(Model model, String backid) {

		// RootControllerクラスで設定した、セッション情報を取得。
		systemInfo = (SystemInfo)session.getAttribute("SessionSysInfo");
		// システム情報のパラメータを渡す。
		model.addAttribute("sysInfo", systemInfo);


		// RootControllerクラスで設定した、セッション情報を取得。
		authUser = (AuthUser)session.getAttribute("SessionAuthUser");
		// ログイン情報のパラメータを渡す。
//		model.addAttribute("authUser", authUser);

		// 前画面IDのパラメータを設定する。
		authUser.setBackId(backid);

		// 例えば、詳細ボタンのある①勤退一覧画面、②打刻登録画面、③報酬計算画面の
		// どれか３つから詳細ボタンを押した時に、
		// 画面遷移して来た、一つ前の画面が分からなくなるので、
		// このタイミングで前画面IDの設定をする。
		model.addAttribute("authUser", authUser);
	}


  /**
   * 勤退情報一覧画面を表示
   * @param model Model
   * @return 勤退情報一覧画面
   */
  @GetMapping(value = "/work/list")
  public String displayListWork(Model model) {

	// ログイン情報の取得と設定。(このタイミングで、前画面IDも設定しておく)
	setAuthUser(model, "list");
//	setAuthUser(model);

	// 勤退情報一覧画面(勤退年月)検索のために、検索条件の年月日の空データを作っておく。
	// ここで作成しておかないと、HTML側でnullエラーになる。
    model.addAttribute("workRequestSearch", new WorkRequestSearch());


	// 勤退情報の検索
	List<Work> worklist = workService.findByUserID(authUser.getId());
	model.addAttribute("worklist", worklist);


	return "work/list";
  }

  /**
   * 勤退情報一覧画面　勤退年月日の範囲検索
   * @param WorkRequest workRequest（検索条件：勤退開始日）
   * @param model Model
   * @return 勤退情報一覧画面
   * @throws ParseException 
   */
  @RequestMapping(value = "/work/search", method = RequestMethod.POST)
  public String displayListWorkSearch(@Validated @ModelAttribute WorkRequestSearch workRequestSearch, BindingResult result, Model model) {

	// ログイン情報の取得と設定。
	setAuthUser(model);

	// 勤退情報一覧画面(勤退年月)検索のために、検索条件の年月日のパラメータを渡しておく。
	// ここで作成しておかないと、HTML側でnullエラーになる。
//    model.addAttribute("workRequestSearch", workRequestSearch);

	if (result.hasErrors()) {
	      // 入力チェックエラーの場合
	      List<String> errorList = new ArrayList<String>();
	      for (ObjectError error : result.getAllErrors()) {
	        errorList.add(error.getDefaultMessage());
	      }
	      model.addAttribute("validationError", errorList);
	      return "work/list";
	}

	Date dateStart = null; // 勤退開始年月日
	Date dateEnd = null; // 勤退終了年月日

	boolean isDateYMD = false; // 日付チェック用(false：正しい日付形式ではない)
	String StrSearchCd = "ALL"; // 年月日の検索コード：
								// "ALL"：検索条件指定なし（勤退開始日と勤退終了日のどちらも空白。）
								// "LN"：(左側)勤退開始日のみ。(右側)勤退終了日は空白(Null)。
								// "NR"：(右側)勤退終了日のみ。(左側)勤退開始日は空白(Null)。
								// "LR"：(左右)勤退開始日と勤退終了日の両方入力あり。


	// 検索条件の入力チェック(開始年月日)
//	if (workRequestSearch.getStartDateY().isEmpty()
//			&& workRequestSearch.getStartDateM().isEmpty()
//			&& workRequestSearch.getStartDateD().isEmpty() ) {
	if (workRequestSearch.getStartDateY() == null
		&& workRequestSearch.getStartDateM() == null
		&& workRequestSearch.getStartDateD() == null ) {
		// 年・月・日が空白の場合は、エラーチェックの対象外。
			StrSearchCd = "ALL"; // "ALL"：検索条件指定なし（勤退開始日と勤退終了日のどちらも空白。）

	} else {
		isDateYMD = DateEdit.isDate(workRequestSearch.getStartDateY(), workRequestSearch.getStartDateM(), workRequestSearch.getStartDateD());
		if (!isDateYMD) {
	        model.addAttribute("validationError", "勤退開始日に正しい日付を入力して下さい。");
	        return "work/list";
		}

		StrSearchCd = "LN"; // "LN"：(左側)勤退開始日のみ。(右側)勤退終了日は空白。
	}

	if ( StrSearchCd.equals("LN") ) {
		// "LN"：(左側)勤退開始日のみ。(右側)勤退終了日は空白。
		dateStart = DateEdit.getDate(workRequestSearch.getStartDateY(), workRequestSearch.getStartDateM(), workRequestSearch.getStartDateD());
	}



	// 検索条件の入力チェック(終了年月日)
//	if (workRequestSearch.getEndDateY().isEmpty()
//			&& workRequestSearch.getEndDateM().isEmpty()
//			&& workRequestSearch.getEndDateD().isEmpty() ) {
	if (workRequestSearch.getEndDateY() == null
		&& workRequestSearch.getEndDateM() == null
		&& workRequestSearch.getEndDateD() == null ) {
		// 年・月・日が空白の場合は、エラーチェックの対象外。

		if ( StrSearchCd.equals("LN") ) {
			// 年月日の検索コードが
			// "LN"：(左側)勤退開始日のみ。(右側)勤退終了日は空白。
			
			// 例）検索条件の入力状態…開始日：2022/XX/XX　～　終了日：空白
			StrSearchCd = "LN"; // "LN"：(左側)勤退開始日のみ。(右側)勤退終了日は空白。

		} else {
			// 例）検索条件の入力状態…開始日：空白　～　終了日：空白
			StrSearchCd = "ALL"; // "ALL"：検索条件指定なし（勤退開始日と勤退終了日のどちらも空白。）
		}

	} else {
		isDateYMD = DateEdit.isDate(workRequestSearch.getEndDateY(), workRequestSearch.getEndDateM(), workRequestSearch.getEndDateD());
		if (!isDateYMD) {
	        model.addAttribute("validationError", "勤退終了日に正しい日付を入力して下さい。");
	        return "work/list";
		}

		if ( StrSearchCd.equals("LN") ) {
			// 年月日の検索コードが
			// "LN"：(左側)勤退開始日のみ。(右側)勤退終了日は空白。

			// 例）検索条件の入力状態…開始日：2022/XX/XX　～　終了日：2022/XX/XX
			StrSearchCd = "LR"; // "LR"：(左右)勤退開始日と勤退終了日の両方入力あり。

			// 開始日と終了日の範囲チェック。
			String strStartDate = DateEdit.getFormatDate(workRequestSearch.getStartDateY(), workRequestSearch.getStartDateM(), workRequestSearch.getStartDateD(), "yyyyMMdd");
			String strEndDate = DateEdit.getFormatDate(workRequestSearch.getEndDateY(), workRequestSearch.getEndDateM(), workRequestSearch.getEndDateD(), "yyyyMMdd");
			isDateYMD = DateRange.isRangeChk(strStartDate, strEndDate);

			if (!isDateYMD) {
		        model.addAttribute("validationError", "勤退終了日が、勤退開始日より過去になっています。");
		        return "work/list";
			}

		} else {
			// 例）検索条件の入力状態…開始日：空白　～　終了日：2022/XX/XX
			StrSearchCd = "NR"; // "NR"：(右側)勤退終了日のみ。(左側)勤退開始日は空白(Null)。
		}
	}

	if ( StrSearchCd.equalsIgnoreCase("LR") || StrSearchCd.equals("NR") ) {
		// "LR"：(左右)勤退開始日と勤退終了日の両方入力あり。
		// または　"NR"：(右側)勤退終了日のみ。(左側)勤退開始日は空白(Null)。
		dateEnd = DateEdit.getDateTime(workRequestSearch.getEndDateY(), workRequestSearch.getEndDateM(), workRequestSearch.getEndDateD(), "23", "59", "59");
	}

	List<Work> worklist;

	// 年月日の検索コードで検索条件を変更する。
	if (StrSearchCd.equalsIgnoreCase("LN")) {
		// "LN"：(左側)勤退開始日のみ。(右側)勤退終了日は空白(Null)。
		worklist = workService.findByDate(authUser.getId(), dateStart, null);
	} else if (StrSearchCd.equals("NR")) {
		// "NR"：(右側)勤退終了日のみ。(左側)勤退開始日は空白(Null)。
		worklist = workService.findByDate(authUser.getId(), null, dateEnd);
	} else if (StrSearchCd.equals("LR")) {
		// "LR"：(左右)勤退開始日と勤退終了日の両方入力あり。
		worklist = workService.findByDate(authUser.getId(), dateStart, dateEnd);
	} else {
		// "ALL"：検索条件指定なし（勤退開始日と勤退終了日のどちらも空白。）
		worklist = workService.findByUserID(authUser.getId());
	}


	if (worklist.size() == 0) {
		// 該当データ無し。
		model.addAttribute("msgInfo", "該当データがありません。");
	}
	model.addAttribute("worklist", worklist);

	return "work/list";
  }

  /**
   * 勤退情報一覧画面(勤退年月)検索
   * @param WorkRequest workRequest（検索条件：勤退開始日）
   * @param model Model
   * @return 勤退情報一覧画面
   * @throws ParseException 
   */
  @RequestMapping(value = "/work/searchym", method = RequestMethod.POST)
  public String displayListWorkSearchYM(@Validated @ModelAttribute WorkRequestSearch workRequestSearch, BindingResult result, Model model) {

	// ログイン情報の取得と設定。
	setAuthUser(model);

	// 勤退情報一覧画面(勤退年月)検索のために、検索条件の年月日のパラメータを渡しておく。
	// ここで作成しておかないと、HTML側でnullエラーになる。
//    model.addAttribute("workRequest", workRequest);

	if (result.hasErrors()) {
	      // 入力チェックエラーの場合
	      List<String> errorList = new ArrayList<String>();
	      for (ObjectError error : result.getAllErrors()) {
	        errorList.add(error.getDefaultMessage());
	      }
	      model.addAttribute("validationError", errorList);
	      return "work/list";
	}

	Date dateStart = null; // 勤退開始年月日
	Date dateEnd = null; // 勤退終了年月日

	boolean isDateYMD = false; // 日付チェック用(false：正しい日付形式ではない)
	String StrSearchCd = "ALL"; // 年月日の検索コード：
								// "ALL"：検索条件指定なし（検索条件の勤退年月が空白。）


	// 検索条件の入力チェック(検索年月)
//	if (workRequestSearch.getSearchDateY().isEmpty()
//			&& workRequestSearch.getSearchDateM().isEmpty() ) {
	if (workRequestSearch.getSearchDateY() == null
		&& workRequestSearch.getSearchDateM() == null ) {
		// 勤退開始(年)と勤退開始(月)が空白の場合は、エラーチェックの対象外。
		// StrSearchCd = "ALL"; // "ALL"：検索条件指定なし

	} else {
		isDateYMD = DateEdit.isDate(workRequestSearch.getSearchDateY(), workRequestSearch.getSearchDateM(), "01");
		if (!isDateYMD) {
	        model.addAttribute("validationError", "勤退年月に正しい日付を入力して下さい。");
	        return "work/list";
		}

		StrSearchCd = "LR"; // "LR"：勤退年月入力あり。
	}



	List<Work> worklist;
	
	if (StrSearchCd.equals("ALL")) {
		// "ALL"：検索条件指定なし（勤退年月が空白。）
		worklist = workService.findByUserID(authUser.getId());

	} else {
		// 勤退年月の入力があった場合

		// 範囲検索の開始日（該当月の初日）
		dateStart = DateEdit.getDate(workRequestSearch.getSearchDateY(), workRequestSearch.getSearchDateM(), "1");
		// 範囲検索の終了日(該当月の末日)
		dateEnd = DateEdit.getDateLastDay(workRequestSearch.getSearchDateY(), workRequestSearch.getSearchDateM());

		// 年月日の検索コードで検索条件を変更する。
		worklist = workService.findByDate(authUser.getId(), dateStart, dateEnd);
	}


	if (worklist.size() == 0) {
		// 該当データ無し。
		model.addAttribute("msgInfo", "該当データがありません。");
	}
	model.addAttribute("worklist", worklist);

	return "work/list";
  }

  /**
   * 勤退情報の新規登録画面を表示
   * @param model Model
   * @return 勤退情報一覧画面
   */
  @GetMapping(value = "/work/add")
  public String displayAddWork(Model model) {

	// ログイン情報の取得と設定。
	setAuthUser(model);

    model.addAttribute("workRequest", new WorkRequest());
	    
    return "work/add";
  }


  /**
   * 勤退情報の新規登録画面を表示
   * (勤退情報一覧のコピーボタンを押した時のイベント)
   * @param id 表示するワークID
   * @param model Model
   * @return 勤退情報の新規登録画面
   */
  @GetMapping("/work/addcopy{id}")
  public String displayAddCopyWork(@PathVariable Long id, Model model) {

	// ログイン情報の取得と設定。
	setAuthUser(model);
	
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


    model.addAttribute("workRequest", workRequest);

    return "work/add";
  }


  /**
   * 勤退情報新規登録
   * @param workRequest リクエストデータ
   * @param model Model
   * @return 勤退情報一覧画面
   */

  @RequestMapping(value = "/work/create", method = RequestMethod.POST)
  public String createWork(@Validated @ModelAttribute WorkRequest workRequest, BindingResult result, Model model) {

	// ログイン情報の取得と設定。
	setAuthUser(model);

	if (result.hasErrors()) {
      // 入力チェックエラーの場合
      List<String> errorList = new ArrayList<String>();
      for (ObjectError error : result.getAllErrors()) {
        errorList.add(error.getDefaultMessage());
      }
      model.addAttribute("validationError", errorList);
      return "work/add";
    }

	// 開始日時と終了日時の範囲チェック。
	String strStartDate = DateEdit.getFormatDateTime(workRequest.getStartDateY(), workRequest.getStartDateM(), workRequest.getStartDateD(),
			workRequest.getStartDateHH(), workRequest.getStartDateMI(), "00", "yyyy/MM/dd HH:mm:ss");
	String strEndDate = DateEdit.getFormatDateTime(workRequest.getEndDateY(), workRequest.getEndDateM(), workRequest.getEndDateD(),
			workRequest.getEndDateHH(), workRequest.getEndDateMI(), "00", "yyyy/MM/dd HH:mm:ss");
	boolean isDateYMD = DateTimeRange.isRangeChk(strStartDate, strEndDate);

	if (!isDateYMD) {
	    model.addAttribute("validationError", "退勤日が、出勤日より過去になっています。");
	    return "work/add";
	}


	Date DateStart = DateEdit.getDateTime(workRequest.getStartDateY(), workRequest.getStartDateM(), workRequest.getStartDateD(),
			"00", "00", "00");
	Date DateEnd = DateEdit.getDateTime(workRequest.getEndDateY(), workRequest.getEndDateM(), workRequest.getEndDateD(),
			"23", "59", "59");

	// 登録しようとしてる日付のデータが、既に存在してないかを検索。
	List<Work> worklist = workService.findByDate(authUser.getId(), DateStart, DateEnd);


	if (worklist.size() != 0) {
		// 該当データあり。
	    model.addAttribute("validationError", "同日の出退勤データが、既に登録されています。");
	    return "work/add";
	}


    // 勤退情報の新規登録
    workService.create(workRequest, authUser.getId()); // ログイン時のユーザーIDを設定。

	if (authUser.getBackId().equals("list")) {
		// 前画面IDが"list"(勤退一覧画面)。
	    return "redirect:/work/list";
	} else {

		// 前画面IDが"stamping"(打刻登録画面)。
	    return "redirect:/work/stamping";		
	}
//    return "redirect:/work/list";
  }


  /**
   * 打刻登録画面を表示
   * @param model Model
   * @return 打刻登録画面
   * @throws ParseException 
   */
  @GetMapping(value = "/work/stamping")
  public String displayStamping(Model model) {

	// ログイン情報の取得と設定。(このタイミングで、前画面IDも設定しておく)
	setAuthUser(model, "stamping");
///	setAuthUser(model);

	// 勤退情報一覧画面(勤退年月)検索のために、検索条件の年月日の空データを作っておく。
	// ここで作成しておかないと、HTML側でnullエラーになる。
    model.addAttribute("workRequest", new WorkRequestSearch());


    Date dateStart;
    Date dateEnd;
    
	List<Work> worklist = new ArrayList<>();



	// 今日の勤退情報を取得する。
    // 現在日を取得する
	dateStart = DateEdit.getDate(DateEdit.getSysDate("yyyy"), DateEdit.getSysDate("MM"), DateEdit.getSysDate("dd"));
	dateEnd = DateEdit.getDateTime(DateEdit.getSysDate("yyyy"), DateEdit.getSysDate("MM"), DateEdit.getSysDate("dd"), "23", "59", "59");
	// 今日の日付で検索。
	worklist = workService.findByDate(authUser.getId(), dateStart, dateEnd);


	if (worklist.size() == 0) {
		// 該当データ無し。(今日の勤退データが作成されてない場合)

	    // 本日の勤退情報（一件分の仮データを画面に渡す）
	    Work work = new Work();
	    // 新規の時は、仮データの勤退内容に未登録を追記する。
//	    work.setContent("未登録");
	    work.setContent("");
	    worklist.add(work);

		model.addAttribute("worklistNow", worklist);

	} else {
		// 今日の勤退データ一件あり
		// 取得したデータベースの情報を設定する。
		model.addAttribute("worklistNow", worklist);
	}



	// 今月分の勤退情報を取得する。
	// 今月の初日
	dateStart = DateEdit.getDate(DateEdit.getSysDate("yyyy"), DateEdit.getSysDate("MM"), "1");
	// 今月の末日
	dateEnd  = DateEdit.getDateLastDay(DateEdit.getSysDate("yyyy"), DateEdit.getSysDate("MM"));
	// 当月の１日～末日で勤退情報を検索。
	worklist = workService.findByDate(authUser.getId(), dateStart, dateEnd);


	if (worklist.size() == 0) {
		// 該当データ無し。
		model.addAttribute("msgInfo", "該当データがありません。");
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

	// ログイン情報の取得と設定。
	setAuthUser(model);

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

	// ログイン情報の取得と設定。
	setAuthUser(model);

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

	// ログイン情報の取得と設定。
	setAuthUser(model);

    // 打刻情報の(退勤)更新処理
    workService.updateStampOut(id);

    return "redirect:/work/stamping";
  }

  /**
   * 勤退情報詳細画面を表示
   * @param id 表示するワークID
   * @param model Model
   * @return 勤退情報詳細画面
   */
  @GetMapping("/work/{id}")
  public String displayViewWork(@PathVariable Long id, Model model) {

	// ログイン情報の取得と設定。
	setAuthUser(model);

    Work work = workService.findById(id);
    model.addAttribute("workData", work);
    return "work/view";
  }

  /**
   * 勤退情報編集画面を表示
   * @param id 表示するワークID
   * @param model Model
   * @return ユーザー編集画面
   */
  @GetMapping("/work/{id}/edit")
  public String displayEditWork(@PathVariable Long id, Model model) {

	// ログイン情報の取得と設定。
	setAuthUser(model);

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


    if (work.getEndDate() == null) {
    	// データベースから取得した終了日時がnullだった場合
    	// 画面表示データを空白で埋める。

	    // 終了日時(年)
	    workUpdateRequest.setEndDateY("");
	    // 終了日時(月)
	    workUpdateRequest.setEndDateM("");
	    // 終了日時(日)
	    workUpdateRequest.setEndDateD("");
	    // 終了日時(時間)
	    workUpdateRequest.setEndDateHH("");
	    // 終了日時(分)
	    workUpdateRequest.setEndDateMI("");
    } else {
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
    }


    model.addAttribute("workUpdateRequest", workUpdateRequest);
    return "work/edit";
  }

  /**
   * 勤退情報更新
   * @param workRequest リクエストデータ
   * @param model Model
   * @return 勤退情報詳細画面
   */
  @RequestMapping(value = "/work/update", method = RequestMethod.POST)
  public String updateWork(@Validated @ModelAttribute WorkUpdateRequest workUpdateRequest, BindingResult result, Model model) {

	// ログイン情報の取得と設定。
	setAuthUser(model);

    if (result.hasErrors()) {
      // 入力チェックエラーの場合
      List<String> errorList = new ArrayList<String>();
      for (ObjectError error : result.getAllErrors()) {
        errorList.add(error.getDefaultMessage());
      }
      model.addAttribute("validationError", errorList);
      return "work/edit";
    }

	// 開始日時と終了日時の範囲チェック。
	String strStartDate = DateEdit.getFormatDateTime(workUpdateRequest.getStartDateY(), workUpdateRequest.getStartDateM(), workUpdateRequest.getStartDateD(),
			workUpdateRequest.getStartDateHH(), workUpdateRequest.getStartDateMI(), "00", "yyyy/MM/dd HH:mm:ss");
	String strEndDate = DateEdit.getFormatDateTime(workUpdateRequest.getEndDateY(), workUpdateRequest.getEndDateM(), workUpdateRequest.getEndDateD(),
			workUpdateRequest.getEndDateHH(), workUpdateRequest.getEndDateMI(), "00", "yyyy/MM/dd HH:mm:ss");
	boolean isDateYMD = DateTimeRange.isRangeChk(strStartDate, strEndDate);


	if (!isDateYMD) {
	    model.addAttribute("validationError", "退勤日が、出勤日より過去になっています。");
	    return "work/edit";
	}


	Date DateStart = DateEdit.getDateTime(workUpdateRequest.getStartDateY(), workUpdateRequest.getStartDateM(), workUpdateRequest.getStartDateD(),
			"00", "00", "00");
	Date DateEnd = DateEdit.getDateTime(workUpdateRequest.getEndDateY(), workUpdateRequest.getEndDateM(), workUpdateRequest.getEndDateD(),
			"23", "59", "59");

	// 登録しようとしてる日付のデータが、既に存在してないかを検索。
	List<Work> worklist = workService.findByDateDuplicate(workUpdateRequest.getId(), authUser.getId(), DateStart, DateEnd);


	if (worklist.size() != 0) {
		// 該当データあり。
	    model.addAttribute("validationError", "同日の出退勤データが、既に登録されています。");
	    return "work/edit";
	}


    // 勤退情報の更新
	workService.update(workUpdateRequest, authUser.getId()); // ログイン時のユーザーIDをパラメータとして渡す。

    return String.format("redirect:/work/%d", workUpdateRequest.getId());
  }

  /**
   * 勤退情報削除
   * @param id 表示するユーザーID
   * @param model Model
   * @return 勤退情報詳細画面
   */
  @GetMapping("/work/{id}/delete")
  public String deleteWork(@PathVariable Long id, Model model) {

    // 勤退情報の削除
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

  }



  /**
   * 報酬計算画面を表示
   * @param model Model
   * @return 勤退情報一覧画面
   */
  @GetMapping(value = "/work/reward")
  public String displayReward(Model model) {

	// ログイン情報の取得と設定。(このタイミングで、前画面IDも設定しておく)
	setAuthUser(model, "reward");
//	setAuthUser(model);


	// 検索条件(勤退年月)のデータを作っておく。
	WorkRequestReward workRequestReward = new WorkRequestReward();
    
	workRequestReward.setSearchDateY(DateEdit.getSysDate("yyyy"));
	workRequestReward.setSearchDateM(DateEdit.getSysDate("M"));
 
	// 時間単価
	workRequestReward.setTanka("0");
	// 紹介手数料(％)
	workRequestReward.setMargin("0");
	// 労働時間数(合計)
	workRequestReward.setWorkSumHours("0.0");
	// 報酬金額(合計)
	workRequestReward.setSumReward("0");


//    model.addAttribute("workRequest", workRequestReward);
    model.addAttribute("workRequestReward", workRequestReward);
	// ↑ここはパラメータ名と、リクエストのクラス名を合わせる。
	// そうしないと、次のエラーメッセージの取得処理で、
    // 対象のリクエストのクラス名を自動検索する際に、エラーが発生する。



    Date dateStart;
    Date dateEnd;
    
	List<Work> worklist = new ArrayList<>();

	// 今月分の勤退情報を取得する。
	// 今月の初日
	dateStart = DateEdit.getDate(DateEdit.getSysDate("yyyy"), DateEdit.getSysDate("MM"), "1");
	// 今月の末日
	dateEnd  = DateEdit.getDateLastDay(DateEdit.getSysDate("yyyy"), DateEdit.getSysDate("MM"));
	// 当月の１日～末日で勤退情報を検索。
	worklist = workService.findByDate(authUser.getId(), dateStart, dateEnd);


	if (worklist.size() == 0) {
		// 該当データ無し。
		model.addAttribute("msgInfo", "該当データがありません。");
	}
	model.addAttribute("worklist", worklist);


	return "work/reward";
  }


  /**
   * 報酬計算画面　指定された(勤退年月)で報酬計算
   * @param WorkRequest workRequest（検索条件：勤退開始日）
   * @param model Model
   * @return 報酬計算画面
   * @throws ParseException 
   */
  @RequestMapping(value = "/work/rewardym", method = RequestMethod.POST)
  public String displayRewardYM(@Validated @ModelAttribute WorkRequestReward workRequestReward, BindingResult result, Model model) {

	// ログイン情報の取得と設定。
	setAuthUser(model);

	if (workRequestReward.getWorkSumHours() == null) {
		// 労働時間数(合計)
		workRequestReward.setWorkSumHours("0.0");
	}
	if (workRequestReward.getSumReward() == null) {
		// 報酬金額(合計)
		workRequestReward.setSumReward("0");
	}

	if (result.hasErrors()) {
	      // 入力チェックエラーの場合
	      List<String> errorList = new ArrayList<String>();
	      for (ObjectError error : result.getAllErrors()) {
	        errorList.add(error.getDefaultMessage());
	      }
	      model.addAttribute("validationError", errorList);
	      return "work/reward";
	}

	Date dateStart = null; // 勤退開始年月日
	Date dateEnd = null; // 勤退終了年月日

	boolean isDateYMD = false; // 日付チェック用(false：正しい日付形式ではない)


	// 検索条件の入力チェック(検索年月)
	isDateYMD = DateEdit.isDate(workRequestReward.getSearchDateY(), workRequestReward.getSearchDateM(), "01");
	if (!isDateYMD) {
        model.addAttribute("validationError", "勤退年月に正しい日付を入力して下さい。");
        return "work/reward";
	}


	List<Work> worklist;

	// 範囲検索の開始日（該当月の初日）
	dateStart = DateEdit.getDate(workRequestReward.getSearchDateY(), workRequestReward.getSearchDateM(), "1");
	// 範囲検索の終了日(該当月の末日)
	dateEnd = DateEdit.getDateLastDay(workRequestReward.getSearchDateY(), workRequestReward.getSearchDateM());

	// 年月日の検索コードで検索条件を変更する。
	worklist = workService.findByDate(authUser.getId(), dateStart, dateEnd);


	if (worklist.size() == 0) {
		// 該当データ無し。
		model.addAttribute("msgInfo", "該当データがありません。");
	}


	// 対象年月の報酬計算処理
	CalcRewardYM(worklist, workRequestReward);


	model.addAttribute("worklist", worklist);
    model.addAttribute("workRequest", workRequestReward);

	return "work/reward";
  }


  /**
   * 報酬金の計算処理
   * @param worklist 対象年月のWorkデータ
   * @return 
   */
  private void CalcRewardYM(List<Work> worklist, WorkRequestReward workRequest) {

	double dblHours = 0; // (小数点を含む、時間数を取得する。)
	double dblReward = 0; // 紹介手数料(％)
	double dblMargin = 0; // 報酬金額

	for (int i = 0; i < worklist.size(); i++){

		// 取得した労働時間を加算する。
		dblHours += worklist.get(i).getWorktime();
		// デバッグ用
		//System.out.println("i=" + i + "：" + worklist.get(i));
		// 実際の出力結果例
		//i=0：Work(id=1, userId=11, content=ゲスト用Ａ会社の打刻, startDate=2022-02-01 09:00:00.0, endDate=2022-02-01 18:30:00.0, worktime=8.5, updateDate=2022-02-05 18:26:52.493, createDate=2022-02-05 09:33:35.038797)
		//i=1：Work(id=2, userId=11, content=ゲスト用Ａ会社の打刻, startDate=2022-02-02 08:30:00.0, endDate=2022-02-02 19:00:00.0, worktime=9.5, updateDate=2022-02-05 18:26:56.489, createDate=2022-02-05 09:33:35.038797)
	}


	// 労働時間数(合計)
	workRequest.setWorkSumHours(String.valueOf(dblHours));

	// 報酬金額= 労働時間 × 時間単価
	dblReward = dblHours * Double.parseDouble(workRequest.getTanka());

	// 紹介手数料(円) = 報酬金額× 紹介手数料（％）
	// 例：画面で２０％を入力してる場合、0.2と計算される。
	dblMargin = dblReward * (Double.parseDouble(workRequest.getMargin()) / 100);

	// 紹介手数料を減算した、報酬金額(合計)を取得する。
	dblReward -= dblMargin;


    //NumberFormatインスタンスを生成
    NumberFormat nfNum = NumberFormat.getNumberInstance();
    // 報酬金額(合計)は、カンマ区切り形式(例：123,456)に変更する。
	workRequest.setSumReward(nfNum.format(dblReward));
	
//	return true; // 今回は戻り値なし。
  }



  /**
   * 数値入力チェック（勤退情報一覧画面の検索条件など）
   * @param StrChk チェックする文字列
   * @param StrMsg エラーチェック対象の項目名
   * @param model Model
   * @return 
   */
  private boolean isValueCheck(String StrChk, String StrMsg, Model model) {
/*
//		※メソッドの使用例
		String StrChk = workRequest.getEndDateY();
		if (!isValueCheck(StrChk, "勤退終了日(年)", model)) {
	          return "work/list";
		}
*/
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
