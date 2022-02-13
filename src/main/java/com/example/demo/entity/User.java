package com.example.demo.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * ユーザー情報 Entity
 */
@Entity
@Data
@Table(name = "users")
public class User implements Serializable {
  /**
   * ID
   */
  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  /**
   * 名前
   */
  @Column(name = "name")
  private String name;
  /**
   * メールアドレス
   */
  @Column(name = "email")
  private String email;
  /**
   * パスワード
   */
  @Column(name = "password")
  private String password;
  /**
   * 性別
   */
  @Column(name = "gender")
  private Integer gender;


  public static List<String> newGenderList() {
	// ラジオボタン、セレクトボックスなどの画面表示用ArrayListを作成。
    List<String> GenderList = new ArrayList<String>();
    GenderList.add("非公開");
    GenderList.add("男性");
    GenderList.add("女性");

	return GenderList;
  }

  public static Integer getChgGender(String value) {
	// 取得した文字列情報をコードに変換する。
	Integer intRtn = 0; // 性別(非公開：０)

	if (value.equals("男性")) {
		intRtn = 1;
	} else if (value.equals("女性")) {
		intRtn = 2;
	}

	return intRtn;
  }

  public static String getChgGender(Integer value) {
	// 取得したコードを文字列情報に変換する。
	String strRtn = "非公開"; // 性別(非公開：０)

	if (value == 1) {
		strRtn = "男性";
	} else if (value == 2) {
		strRtn = "女性";
	}

	return strRtn;
  }


  /**
   * 住所
   */
  @Column(name = "address")
  private String address;
  /**
   * 電話番号
   */
  @Column(name = "phone")
  private String phone;

  /**
   * 生年月日
   */
  @Column(name = "birthday")
  private Date birthday;
  /**
   * 年齢
   */
  @Column(name = "age")
  private String age;
//  private Integer age;


  /**
   * 更新日時
   */
  @Column(name = "updated_at")
  private Date updateDate;
  /**
   * 登録日時
   */
  @Column(name = "created_at")
  private Date createDate;
}