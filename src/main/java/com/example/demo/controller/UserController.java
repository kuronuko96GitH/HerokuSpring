package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.demo.dto.UserRequest;
import com.example.demo.dto.UserUpdateRequest;
import com.example.demo.dto.UserUpdateRequestPass;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;

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
   * トップページを表示
   * @param model Model
   * @return トップページ
   * 補足(ローカル環境)：URL…http://localhost:8080/
   */
  @GetMapping(value = "/")
  public String root() {
	  return "index";
  }

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
   * サンプル（テンプレート用）画面を表示
   * @param model Model
   * @return トップページ
   * 補足(ローカル環境)：URL…http://localhost:8080/sample.html
   */
  @GetMapping(value = "/sample")
  public String sample(HttpServletRequest request, Model model){
      // セッションIDと、Cookieに設定されたセッションIDを設定する
      HttpSession session = request.getSession(false);
      model.addAttribute("sessionId"
          , session == null ? "sessionは未作成" : session.getId());
      model.addAttribute("cookieSessionId", getCookieSessionId(request));
      return "sample";
  }
/*
  @GetMapping(value = "/sample")
  public String sample() {
	  return "sample";
  }
*/



  /**
   * About Me画面を表示
   * @param model Model
   * @return トップページ
   * 補足(ローカル環境)：URL…http://localhost:8080/others/aboutme.html
   */
  @GetMapping(value = "/others/aboutme")
  public String aboutme() {
	  return "others/aboutme";
  }

  /**
   * Link画面を表示
   * @param model Model
   * @return トップページ
   * 補足(ローカル環境)：URL…http://localhost:8080/others/aboutme.html
   */
  @GetMapping(value = "/others/portfolio")
  public String portfolio() {
	  return "others/portfolio";
  }




  /**
   * ユーザー情報一覧画面を表示
   * @param model Model
   * @return ユーザー情報一覧画面
   */
  @GetMapping(value = "/user/list")
  public String displayList(Model model) {
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
    if (result.hasErrors()) {
      // 入力チェックエラーの場合
      List<String> errorList = new ArrayList<String>();
      for (ObjectError error : result.getAllErrors()) {
        errorList.add(error.getDefaultMessage());
      }
      model.addAttribute("validationError", errorList);
      return "user/add";
    }
    // ユーザー情報の登録
    userService.create(userRequest);
    return "login"; // 管理者ではないユーザーの新規登録の場合は、ログイン画面に戻る？
//    return "redirect:/user/list";
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
    if (result.hasErrors()) {
      List<String> errorList = new ArrayList<String>();
      for (ObjectError error : result.getAllErrors()) {
        errorList.add(error.getDefaultMessage());
      }
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
    // ユーザー情報の削除
    userService.delete(id);
    return "redirect:/user/list";
  }




  /**
   * CookieのセッションIDを取得する
   * @param request HttpServletリクエスト
   * @return CookieのセッションID
   */
  private String getCookieSessionId(HttpServletRequest request){
      if(request == null || request.getCookies() == null){
          return "CookieのセッションIDは未設定";
      }
      Cookie cookie[] = request.getCookies();
      for (int i = 0 ; i < cookie.length ; i++) {
          if ("JSESSIONID".equals(cookie[i].getName())) {
              return cookie[i].getValue();
          }
      }
      return "CookieのセッションIDは未設定";
  }
}