package com.example.demo.packWebMeeting.controller;

import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.auth.AuthUser;
import com.example.demo.entity.SystemInfo;
import com.example.demo.entity.SystemMsg;
import com.example.demo.packWebMeeting.entity.Topic;
import com.example.demo.packWebMeeting.entity.forms.TopicCreationForm;
import com.example.demo.packWebMeeting.service.WebMeetingService;

@Controller
public class WebMeetingController {
	
	/**
	* セッション情報（複数のコンローラ間で共有したいログイン情報などを格納する）
	*/
	@Autowired
	private HttpSession session;

	/**
	* Web会議室(Topic)情報 Service
	*/
	@Autowired
	private WebMeetingService webMeetingService;

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



	@ModelAttribute
	public TopicCreationForm topicCreationForm() {
		// 初期表示用メソッド（新規投稿用のフォーム画面情報）
		// (get・set・入力項目エラーチェック情報クラス)
		return new TopicCreationForm();

		// 【補足説明】@ModelAttribute　とは？
		// 初期表示・画面遷移時に『各メソッドの呼び出し前に』、model.addAttribute("topicCreationForm", new TopicCreationForm());　を処理するのと同じことをやってます。 
	}

	@ModelAttribute("topics")
	public List<Topic> initializeTopics() {
		// 初期表示用メソッド（既に投稿済みの情報を取得）
		// (データベースから、『topics』『posts』『post_ratings』『users』の各テーブルを結合した情報を取得する)
		return webMeetingService.loadTopics();

		// 【補足説明】@ModelAttribute("topics")　とは？
		// 初期表示・画面遷移時に『各メソッドの呼び出し前に』、model.addAttribute("topics", webMeetingService.loadTopics());　を処理するのと同じことをやってます。 
	}

  /**
   * （初期表示）Web会議室画面を表示
   * @param model Model
   * @return Web会議室画面
   */
  @GetMapping(value = "/webMeeting/WebMeeting")
  public String displayWebMeeting(Model model) {

	// ログイン情報の取得と設定。
	if (setAuthUser(model, null) != null) {
		// セッション情報の取得に失敗した場合、システムエラー画面を表示
		return setAuthUser(model, null);
	}

	return "webMeeting/WebMeeting";

// 	// ◆デバッグ用２（Modelをメソッドの引数から削除する前のソース）
////public String displayWebMeeting(Model model) {　←　以下２つをmodel.addAttributeしなくなったので、メソッドの引数からModelを削除しました。
//model.addAttribute("topicCreationForm", new TopicCreationForm());  ←　public TopicCreationForm topicCreationForm()メソッドが作成されたので不要となりました。
//model.addAttribute("topics", webMeetingService.loadTopics());  ←　public List<Topic> initializeTopics()メソッドが作成されたので不要となりました。
//	return "webMeeting/WebMeeting";
  }



	// トピック新規作成
  	// （トピック番号の採番、データベースへの更新処理）
  	// 新規作成・対象テーブル『topics』『posts』
	@PostMapping("/webMeeting/CreateTopic")
	public String onTopicRegistrationRequested(@Valid TopicCreationForm form,
												BindingResult bindingResult, Model model,
												@AuthenticationPrincipal AuthUser user) {

		// ログイン情報の取得と設定。
		if (setAuthUser(model, null) != null) {
			// セッション情報の取得に失敗した場合、システムエラー画面を表示
			return setAuthUser(model, null);
		}

		if (bindingResult.hasErrors()) {
			// 新規投稿のトピックタイトルと投稿内容に、入力エラーが発生した場合。
			form.setError(true);
			// model.addAttribute("topicCreationForm", form);
			// model.addAttribute("topics", webMeetingService.loadTopics());
			return "webMeeting/WebMeeting";
		} else {
			// 入力エラーが発生しない場合、データベースへの新規登録処理を実行して、
			// 画面を再描画する。
			webMeetingService.registerNewTopic(form, user);
			return "redirect:/webMeeting/WebMeeting";
		}
	}

// 	// ◆デバッグ用（最初期モデル）
//
//	// トピック新規作成
//  // （トピック番号の採番、データベースへの更新処理）
//  // 新規作成・対象テーブル『topics』『posts』
//	@PostMapping("/webMeeting/CreateTopic")
//	public String onTopicRegistrationRequested(@Valid @ModelAttribute TopicCreationForm form,
//												BindingResult bindingResult, Model model,
//												@AuthenticationPrincipal AuthUser user) {
//		if (bindingResult.hasErrors()) {
//			form.setError(true);
//			model.addAttribute("topicCreationForm", form);
//			model.addAttribute("topics", webMeetingService.loadTopics());
//			return "webMeeting/WebMeeting";
//		} else {
//			webMeetingService.registerNewTopic(form, user);
//			return "redirect:/webMeeting/WebMeeting";
//		}
//	}


	// 投稿追加
	@PostMapping("/webMeeting/AppendPost")
	public String onAppendPostRequested(@Valid TopicCreationForm form,
										BindingResult bindingResult, Model model,
										@AuthenticationPrincipal AuthUser user) {

		// ログイン情報の取得と設定。
		if (setAuthUser(model, null) != null) {
			// セッション情報の取得に失敗した場合、システムエラー画面を表示
			return setAuthUser(model, null);
		}

		if (bindingResult.hasErrors()) {
			model.addAttribute("validationMessage",
							bindingResult.getFieldError().getDefaultMessage());
		} else {
			webMeetingService.appendPost(form, user);
		}
		model.addAttribute("topic", webMeetingService.reloadTopic(form.getTopicNo()));
		return "common/Topic :: topic";
	}


	// 投稿評価
	@PostMapping("/webMeeting/PostRating")
	public String onPostRatingRequested(@RequestParam("topicNo") String topicNo,
										@RequestParam("postNo") int postNo,
										@RequestParam("rating") int rating,
										Model model,
										@AuthenticationPrincipal AuthUser user) {
		// グッドボタンなどの評価情報を検索し、変更が発生した場合は評価データを追加・更新する。
		webMeetingService.postRating(topicNo, postNo, rating, user);
		model.addAttribute("topic", webMeetingService.reloadTopic(topicNo));
		return "common/Topic :: topic";
	}
}