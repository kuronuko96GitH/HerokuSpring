package com.example.demo.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 勤退情報 Entity
 */
@Entity
@Data
@Table(name = "works")
public class Work implements Serializable {
  /**
   * ID
   */
  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  /**
   * ユーザーID
   */
  @Column(name = "user_id")
  private Long userId;
  /**
   * コンテンツ
   */
  @Column(name = "content")
  private String content;
  /**
   * 開始日時
   */
  @Column(name = "start_at")
  private Date startDate;
  /**
   * 終了日時
   */
  @Column(name = "end_at")
  private Date endDate;
  /**
   * 労働時間数
   */
  @Column(name = "worktime")
  private double worktime;
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