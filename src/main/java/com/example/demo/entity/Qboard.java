package com.example.demo.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 質問板情報 Entity
 */
@Entity
@Data
@Table(name = "qboards")
public class Qboard implements Serializable {
  /**
   * ID
   */
  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  /**
   * ヘッダID
   */
  @Column(name = "head_id")
  private Integer headId;
  /**
   * ボディID
   */
  @Column(name = "body_id")
  private Integer bodyId;
  /**
   * ユーザーID
   */
  @Column(name = "user_id")
  private Long userId;
  /**
   * 状態コード
   */
  @Column(name = "status_code")
  private Integer statusCode;
  /**
   * 投稿者名
   */
  @Column(name = "name")
  private String name;
  /**
   * 投稿内容
   */
  @Column(name = "content")
  private String content;
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


  public static Map<String, String> newRadioList() {
    // ラジオボタンの画面表示用のHashMap作成
    Map<String, String> radioList = new LinkedHashMap<String, String>();

    radioList.put("01", "指定なし");
    radioList.put("02", "過去に自分が質問した投稿");

	return radioList;
  }
}