package com.example.demo.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.demo.dto.WorkRequest;
import com.example.demo.dto.WorkRequestSearch;
import com.example.demo.dto.WorkUpdateRequest;
import com.example.demo.entity.Work;
import com.example.demo.mdl.DateEdit;
import com.example.demo.mdl.DateTimeRange;
import com.example.demo.repository.RepositoryXml;
import com.example.demo.repository.WorkRepository;

/**
 * 勤退情報 Service
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class WorkService {

  /**
   * 勤退情報 WorkRepository
   */
  @Autowired
  private WorkRepository workRepository;

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
   * 勤退情報 全検索
   * @return 検索結果
   */
  public List<Work> searchAll() {
	  // id(昇順)
	  return workRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
  }

  /**
   * 勤退情報 新規登録
   * @param user 勤退情報
   */
  public void create(WorkRequest workRequest, Long userID) {

	Date now = new Date();
    Work work = new Work();

    work.setUserId(userID); // ユーザーID
    work.setContent(workRequest.getContent());

/*【旧版】年月日のテキストボックス３つが別々ver
    // 開始日時
    Date dateS = DateEdit.getDateTime(workRequest.getStartDateY(), workRequest.getStartDateM(), workRequest.getStartDateD(),
    		workRequest.getStartDateHH(), workRequest.getStartDateMI(), "0");
    work.setStartDate(dateS);

    // 終了日時
    Date dateE = DateEdit.getDateTime(workRequest.getEndDateY(), workRequest.getEndDateM(), workRequest.getEndDateD(),
    		workRequest.getEndDateHH(), workRequest.getEndDateMI(), "0");
    work.setEndDate(dateE);
*/
    // 開始日時
    Date dateS = DateEdit.getDateYMDTime(workRequest.getStartDateYMD(),
    		workRequest.getStartDateHH(), workRequest.getStartDateMI(), "0");
    work.setStartDate(dateS);

    // 終了日時
    Date dateE = DateEdit.getDateYMDTime(workRequest.getEndDateYMD(),
    		workRequest.getEndDateHH(), workRequest.getEndDateMI(), "0");
    work.setEndDate(dateE);

	// 労働時間数を取得。
    String strStartDate = DateEdit.getDate(dateS, "yyyy/MM/dd HH:mm:ss");
    String strEndDate = DateEdit.getDate(dateE, "yyyy/MM/dd HH:mm:ss");
	double dblHours = DateTimeRange.getRangeHours(strStartDate, strEndDate, DateTimeRange.INT_KIRISUTE);
    work.setWorktime(dblHours);

    // 登録日
    work.setCreateDate(now);
    // 更新日
    work.setUpdateDate(now);
 
    workRepository.save(work);
  }

  /**
   * 打刻(出勤)情報の新規登録
   * @param userID ユーザーID(ログインID)
   */
  public void createStampIn(Long userID) {

    Date now = new Date();
    Work work = new Work();

    work.setUserId(userID); // ユーザーID
    // 勤退内容
    work.setContent("");

    // 開始日時
    work.setStartDate(now);
    // 終了日時
//    work.setEndDate(now);

    // 登録日
    work.setCreateDate(now);
    // 更新日
    work.setUpdateDate(now);
 
    workRepository.save(work);
  }

  /**
   * 打刻(出勤)情報の更新処理
   * @param userID ユーザーID(ログインID)
   */
  public void updateStampIn(Long id) {

	Date now = new Date();
    Work work = findById(id); // 更新対象のワークID

    // 開始日時
    work.setStartDate(now);
    // 終了日時
    work.setEndDate(now);

	// 労働時間数を取得。
    work.setWorktime(0);

	// 更新日
    work.setUpdateDate(new Date());

    workRepository.save(work);
  }

  /**
   * 打刻(退勤)情報の更新処理
   * @param userID ユーザーID(ログインID)
   */
  public void updateStampOut(Long id) {

	Date now = new Date();
    Work work = findById(id); // 更新対象のワークID

//    // 開始日時
//    work.setStartDate(now);
    // 終了日時
    work.setEndDate(now);

	// 労働時間数を取得。
    String strStartDate = DateEdit.getDate(work.getStartDate(), "yyyy/MM/dd HH:mm:ss");
    String strEndDate = DateEdit.getDate(work.getEndDate(), "yyyy/MM/dd HH:mm:ss");
	double dblHours = DateTimeRange.getRangeHours(strStartDate, strEndDate, DateTimeRange.INT_KIRISUTE);
    work.setWorktime(dblHours);

	// 更新日
    work.setUpdateDate(new Date());

    workRepository.save(work);
  }


  /**
   * 勤退情報 主キー検索
   * @return 検索結果
   */
  public Work findById(Long id) {
    return workRepository.findById(id).get();
  }

  /**
   * 勤退情報 該当する『ユーザーID』で検索
   * @return 検索結果
   */
  public List<Work> findByUserID(@PathVariable("userId") Long userId) {
	return workRepository.findByUserID(userId);
  }

  /**
   * 勤退情報の検索（入力された条件で検索）
   * @return 検索結果
   */
  public List<Work> searchWork(Long userId, WorkRequestSearch workRequestSearch) {

	// 入力項目で検索条件を変更する。
	return repositoryXml.searchWork(userId, workRequestSearch.getStartDate(), workRequestSearch.getEndDate(), this.fromIndex, this.limitCnt);
//	return repositoryXml.searchWork(userId, workRequestSearch.getStartDate(), workRequestSearch.getEndDate());
  }

  /**
   * 勤退情報の検索結果の件数を取得（入力された条件で検索）
   * @return 検索結果
   */
  public Integer countWork(Long userId, WorkRequestSearch workRequestSearch) {

	// 入力項目で検索条件を変更する。
	return repositoryXml.countWork(userId, workRequestSearch.getStartDate(), workRequestSearch.getEndDate());
  }

  /**
   * 勤退情報 該当する『ユーザーID』と『勤退開始日』～『勤退終了日』で検索
   * @return 検索結果
   */
  public List<Work> findByDate(@PathVariable("userId") Long userId, Date startDate, Date endDate) {

		// 勤退開始日～勤退終了日で検索。
		return workRepository.findByDate(userId, startDate, endDate);
  }

  /**
   * 勤退情報 該当する『ユーザーID』と『出勤日』で同日の重複チェック
   * @return 検索結果
   */
  public List<Work> findByDateDuplicate(Long id, Long userId, Date startDate, Date endDate) {

	// 選択したデータのIDを除いた、同日のデータを検索。
	return workRepository.findByDateDuplicate(id, userId, startDate, endDate);
  }

  /**
   * 勤退情報 更新
   * @param 
   */
  public void update(WorkUpdateRequest workUpdateRequest, Long userID) {
    Work work = findById(workUpdateRequest.getId());

    work.setUserId(userID);
    work.setContent(workUpdateRequest.getContent());

/*【旧版】年月日のテキストボックス３つが別々ver
    // 開始日時
    Date dateS = DateEdit.getDateTime(workUpdateRequest.getStartDateY(), workUpdateRequest.getStartDateM(), workUpdateRequest.getStartDateD(),
    		workUpdateRequest.getStartDateHH(), workUpdateRequest.getStartDateMI(), "0");
    work.setStartDate(dateS);

    // 終了日時
    Date dateE = DateEdit.getDateTime(workUpdateRequest.getEndDateY(), workUpdateRequest.getEndDateM(), workUpdateRequest.getEndDateD(),
    		workUpdateRequest.getEndDateHH(), workUpdateRequest.getEndDateMI(), "0");
    work.setEndDate(dateE);
*/
    // 開始日時
    Date dateS = DateEdit.getDateYMDTime(workUpdateRequest.getStartDateYMD(),
    		workUpdateRequest.getStartDateHH(), workUpdateRequest.getStartDateMI(), "0");
    work.setStartDate(dateS);

    // 終了日時
    Date dateE = DateEdit.getDateYMDTime(workUpdateRequest.getEndDateYMD(),
    		workUpdateRequest.getEndDateHH(), workUpdateRequest.getEndDateMI(), "0");
    work.setEndDate(dateE);

	// 労働時間数を取得。
    String strStartDate = DateEdit.getDate(dateS, "yyyy/MM/dd HH:mm:ss");
    String strEndDate = DateEdit.getDate(dateE, "yyyy/MM/dd HH:mm:ss");
	double dblHours = DateTimeRange.getRangeHours(strStartDate, strEndDate, DateTimeRange.INT_KIRISUTE);
    work.setWorktime(dblHours);
 
	// 更新日
    work.setUpdateDate(new Date());

    workRepository.save(work);
  }

  /**
   * 勤退情報 物理削除
   * @param id ユーザーID
   */
  public void delete(Long id) {
    Work work = findById(id);
    workRepository.delete(work);
  }
}