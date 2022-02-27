package com.example.demo.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.QboardRequest;
import com.example.demo.entity.Qboard;
import com.example.demo.repository.QboardRepository;
import com.example.demo.repository.RepositoryXml;

/**
 * 質問板ヘッダ情報 Service
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class QboardService {

  /**
   * 質問板情報 QboardRepository
   */
  @Autowired
  private QboardRepository qboardRepository;

  /**
   * Repository(XML版・動的SQLに対応)
   */
  @Autowired
  private RepositoryXml repositoryXml;



  /**
   * ヘッダID（質問掲示板のみで使うID）
   */
  private Integer headId;

  // ヘッダID
  public Integer getHeadId() {
	return headId;
  }

  public void setHeadId(Integer headId) {
	this.headId = headId;
  }

 
  // （最新の）ヘッダID（新規登録時などに使用するメソッド）
  public Integer getMaxHeadId() {

	Integer maxHeadId = 0;
	// ソート順を降順にして、一件目のデータを取得する。
	maxHeadId = qboardRepository.findHeadIdMax().get(0).getHeadId();
	return maxHeadId;
  }



  /**
   * 質問板情報 全検索
   * @return 検索結果
   */
  public List<Qboard> searchAll() {
	  // id(昇順)
	  return qboardRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
  }

  /**
   * 質問板情報 主キー検索
   * @return 検索結果
   */
  public Qboard findById(Long id) {
    return qboardRepository.findById(id).get();
  }




  /**
   * 質問板情報 該当する条件で検索
   * @return 検索結果
   */
  public List<Qboard> findList() {
	return qboardRepository.findList();
  }



  /**
   * 質問板情報 指定されたヘッダIDに該当するボディデータを検索
   * @return 検索結果
   */
  public List<Qboard> findBodyList(Integer headId) {
	return qboardRepository.findBodyList(headId);
  }

  /**
   * 質問板ヘッダ情報 該当する『ユーザーID』で検索
   * @return 検索結果
   */
/*
  public List<QboardH> findByUserID(@PathVariable("userId") Long userId) {
	return qboardHRepository.findByUserID(userId);
  }
*/





  /**
   * 質問板情報 新規登録
   * @param user 質問板情報
   */
  public void create(QboardRequest qboardRequest, Long userID, Integer headId) {

	Date now = new Date();
	Qboard qboard = new Qboard();

//	qboard.setHeadId(3); // ヘッダID
	Integer maxHeadId = 0; // ヘッダID（新規の質問投稿時に必要なID）
	Integer maxBodyId = 0; // ボディID（返信投稿時に必要なID）

	if (headId == 0) {
		// ヘッダIDが０の場合（新規の質問投稿）

		// ソート順を降順にして、一件目のデータを取得する。
		maxHeadId = qboardRepository.findHeadIdMax().get(0).getHeadId();
		maxHeadId += 1;
		// ヘッダIDの設定。
		qboard.setHeadId(maxHeadId);

		// ボディIDの設定
		qboard.setBodyId(1);

	} else {
		// 追加投稿の場合

		// ヘッダIDの設定。
		qboard.setHeadId(headId);

		// ソート順を降順にして、一件目のデータを取得する。
		maxBodyId = qboardRepository.findBodyIdMax(headId).get(0).getBodyId();
		maxBodyId += 1;
		// ボディIDの設定
		qboard.setBodyId(maxBodyId);
	}


	qboard.setUserId(userID); // ユーザーID

	qboard.setStatusCode(1); // 1：新規

	qboard.setName(qboardRequest.getName());
	qboard.setContent(qboardRequest.getContent());

/*
    // 開始日時
    Date dateS = DateEdit.getDateTime(workRequest.getStartDateY(), workRequest.getStartDateM(), workRequest.getStartDateD(),
    		workRequest.getStartDateHH(), workRequest.getStartDateMI(), "0");
    work.setStartDate(dateS);

    // 終了日時
    Date dateE = DateEdit.getDateTime(workRequest.getEndDateY(), workRequest.getEndDateM(), workRequest.getEndDateD(),
    		workRequest.getEndDateHH(), workRequest.getEndDateMI(), "0");
    work.setEndDate(dateE);

	// 労働時間数を取得。
    String strStartDate = DateEdit.getDate(dateS, "yyyy/MM/dd HH:mm:ss");
    String strEndDate = DateEdit.getDate(dateE, "yyyy/MM/dd HH:mm:ss");
	double dblHours = DateTimeRange.getRangeHours(strStartDate, strEndDate, DateTimeRange.INT_KIRISUTE);
    work.setWorktime(dblHours);
*/
    // 登録日
	qboard.setCreateDate(now);
    // 更新日
	qboard.setUpdateDate(now);
 
    qboardRepository.save(qboard);
  }



  /**
   * 質問板情報 更新
   * @param 
   */
//  public void update(QboardUpdateRequest qboardUpdateRequest, Long userID) {
//	Qboard qboard = findById(qboardUpdateRequest.getId());
  public void delUpdate(Long Id, Long userID, Integer intStatusCode) {
	Qboard qboard = findById(Id);

	if ( qboard.getUserId() == userID) {
	// 投稿者と削除したログインユーザーが同じ場合
		qboard.setStatusCode(intStatusCode); // ステータスコードを削除(９)に設定。

	} else {
	// 投稿者の違反などによるペナルティとして、
	//管理者権限を持つ担当者が削除した場合。
		qboard.setStatusCode(intStatusCode); // ステータスコードを削除(８)に設定。
	}
	
/*
	qboard.setUserId(userID);
	qboard.setContent(qboardUpdateRequest.getContent());

    // 開始日時
    Date dateS = DateEdit.getDateTime(workUpdateRequest.getStartDateY(), workUpdateRequest.getStartDateM(), workUpdateRequest.getStartDateD(),
    		workUpdateRequest.getStartDateHH(), workUpdateRequest.getStartDateMI(), "0");
    work.setStartDate(dateS);

    // 終了日時
    Date dateE = DateEdit.getDateTime(workUpdateRequest.getEndDateY(), workUpdateRequest.getEndDateM(), workUpdateRequest.getEndDateD(),
    		workUpdateRequest.getEndDateHH(), workUpdateRequest.getEndDateMI(), "0");
    work.setEndDate(dateE);

	// 労働時間数を取得。
    String strStartDate = DateEdit.getDate(dateS, "yyyy/MM/dd HH:mm:ss");
    String strEndDate = DateEdit.getDate(dateE, "yyyy/MM/dd HH:mm:ss");
	double dblHours = DateTimeRange.getRangeHours(strStartDate, strEndDate, DateTimeRange.INT_KIRISUTE);
    work.setWorktime(dblHours);
 */
	// 更新日
	qboard.setUpdateDate(new Date());

	qboardRepository.save(qboard);
  }

  /**
   * 質問板情報 物理削除
   * @param id ユーザーID
   */
  public void delete(Long id) {
	Qboard qboard = findById(id);
    qboardRepository.delete(qboard);
  }
}