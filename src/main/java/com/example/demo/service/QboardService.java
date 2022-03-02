package com.example.demo.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.QboardRequest;
import com.example.demo.dto.QboardRequestSearch;
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



  // 以下、検索画面のページング用
  private Integer fromIndex; // （画面に表示される）始まりのn件目情報（実際の件数データから-1された状態で格納されてます）
  private Integer limitCnt; // １ページあたりの表示件数

//  public Integer getPageFrom() {
//	return fromIndex;
//  }

  public void setFromIndex(Integer fromIndex) {
	this.fromIndex = fromIndex;
  }

//  public Integer getLimitCnt() {
//	return limitCnt;
//  }

  public void setLimitCnt(Integer limitCnt) {
	this.limitCnt = limitCnt;
  }





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
   * 質問板情報の検索（入力された条件で検索）
   * @return 検索結果
   */
  public List<Qboard> searchQboard(Long userId, QboardRequestSearch qboardRequestSearch) {

	// 入力項目で検索条件を変更する。

	// 検索画面が『指定なし』の場合は、検索条件のユーザーIDは０に設定する。
	Long keyUserId = getRadioUserId(userId, qboardRequestSearch);

	return repositoryXml.searchQboard(keyUserId, qboardRequestSearch.getContent1(), this.fromIndex, this.limitCnt);
  }

  /**
   * ユーザーIDの取得（入力項目で検索条件を変更する。）
   * @return 検索結果
   */
  public Long getRadioUserId(Long userId, QboardRequestSearch qboardRequestSearch) {

		// 検索画面が『指定なし』の場合は、検索条件のユーザーIDは０に設定する。
		Long keyUserId = 0L;

		if (qboardRequestSearch.getRadioKeyValue() != null) {
			 // 検索条件画面のラジオボタンが、指定なし以外の場合。

			if (qboardRequestSearch.getRadioKeyValue().equals("02")) {
				// 過去に自分がした質問投稿を検索する場合
				keyUserId = userId;
			}
		}
		return keyUserId;
  }


  /**
   * 質問板情報の検索結果の件数を取得（入力された条件で検索）
   * @return 検索結果
   */
  public Integer countQboard(Long userId, QboardRequestSearch qboardRequestSearch) {

	// 検索画面が『指定なし』の場合は、検索条件のユーザーIDは０に設定する。
	Long keyUserId = getRadioUserId(userId, qboardRequestSearch);

	// 入力項目で検索条件を変更する。
	return repositoryXml.countQboard(keyUserId, qboardRequestSearch.getContent1());
  }





  /**
   * 質問板情報 新規登録
   * @param user 質問板情報
   */
  public void create(QboardRequest qboardRequest, Long userID, Integer headId) {

	Date now = new Date();
	Qboard qboard = new Qboard();

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

	qboard.setStatusCode(1); // 1：新規(未削除)

	qboard.setName(qboardRequest.getName());
	qboard.setContent(qboardRequest.getContent());

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