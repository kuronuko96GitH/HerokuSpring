package com.example.demo.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.demo.dto.WorkRequest;
import com.example.demo.dto.WorkUpdateRequest;
import com.example.demo.entity.Work;
import com.example.demo.mdl.DateEdit;
import com.example.demo.repository.WorkRepository;

/**
 * 勤怠情報 Service
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class WorkService {
 
  /**
   * 勤怠情報 Repository
   */
  @Autowired
  private WorkRepository workRepository;

  /**
   * 勤怠情報 全検索
   * @return 検索結果
   */
  public List<Work> searchAll() {
	  // id(昇順)
	  return workRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
  }

  /**
   * 勤怠情報 新規登録
   * @param user 勤怠情報
   */
//  public void create(WorkRequest workRequest) {
  public void create(WorkRequest workRequest, Long userID) {
//  public void create(WorkRequest workRequest, Long userID) throws ParseException {
    Date now = new Date();
    Work work = new Work();

    work.setUserId(userID); // ユーザーID
    work.setContent(workRequest.getContent());


    // 開始日時
    Date dateS = DateEdit.getDateTime(workRequest.getStartDateY(), workRequest.getStartDateM(), workRequest.getStartDateD(),
    		workRequest.getStartDateHH(), workRequest.getStartDateMI(), "0");
    work.setStartDate(dateS);

    // 終了日時
    Date dateE = DateEdit.getDateTime(workRequest.getEndDateY(), workRequest.getEndDateM(), workRequest.getEndDateD(),
    		workRequest.getEndDateHH(), workRequest.getEndDateMI(), "0");
    work.setEndDate(dateE);
/*
    // 開始日時
    String strDateS = workRequest.getStartDateY() + "/"
    					+ String.format("%02d", Integer.parseInt(workRequest.getStartDateM())) + "/"
    					+ String.format("%02d", Integer.parseInt(workRequest.getStartDateD())) + " "
    					+ String.format("%02d", Integer.parseInt(workRequest.getStartDateHH())) + ":"
    					+ String.format("%02d", Integer.parseInt(workRequest.getStartDateMI())) + ":00";

    SimpleDateFormat sdFormatS = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
    Date dateS = sdFormatS.parse(strDateS);

    work.setStartDate(dateS);



    // 終了日時
    String strDateE = workRequest.getEndDateY() + "/"
			+ String.format("%02d", Integer.parseInt(workRequest.getEndDateM())) + "/"
			+ String.format("%02d", Integer.parseInt(workRequest.getEndDateD())) + " "
			+ String.format("%02d", Integer.parseInt(workRequest.getEndDateHH())) + ":"
			+ String.format("%02d", Integer.parseInt(workRequest.getEndDateMI())) + ":00";

    SimpleDateFormat sdFormatE = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
    Date dateE = sdFormatE.parse(strDateE);

    work.setEndDate(dateE);
*/
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
//    work.setContent(workRequest.getContent());
    work.setContent("未登録");

    // 開始日時
    work.setStartDate(now);
    // 終了日時
    work.setEndDate(now);

    work.setCreateDate(now);
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

	// 更新日
    work.setUpdateDate(new Date());

    workRepository.save(work);
  }


  /**
   * 勤怠情報 主キー検索
   * @return 検索結果
   */
  public Work findById(Long id) {
    return workRepository.findById(id).get();
  }

  /**
   * 勤怠情報 該当する『ユーザーID』で検索
   * @return 検索結果
   */
  public List<Work> findByUserID(@PathVariable("userId") Long userId) {
	return workRepository.findByUserID(userId);
  }

  /**
   * 勤怠情報 該当する『ユーザーID』と『勤怠開始日』で検索
   * @return 検索結果
   */
  public List<Work> findByDate(@PathVariable("userId") Long userId, Date startDate, Date endDate) {

		// 年月日の検索コードで検索条件を変更する。
		if (startDate != null && endDate == null) {
			// "LN"：(左側)勤怠開始日のみ。(右側)勤怠終了日は空白(Null)。
			return workRepository.findByStartDate(userId, startDate);
		} else if (startDate == null && endDate != null) {
			// "NR"：(右側)勤怠終了日のみ。(左側)勤怠開始日は空白(Null)。
			return workRepository.findByEndDate(userId, endDate);
		} else {
			// "LR"：(左右)勤怠開始日と勤怠終了日の両方入力あり。
			return workRepository.findByDate(userId, startDate, endDate);
		}

  }

  /**
   * 勤怠情報 更新
   * @param user 勤怠情報
   */
  public void update(WorkUpdateRequest workUpdateRequest, Long userID) {
    Work work = findById(workUpdateRequest.getId());

    work.setUserId(userID);
    work.setContent(workUpdateRequest.getContent());

    // 開始日時
    Date dateS = DateEdit.getDateTime(workUpdateRequest.getStartDateY(), workUpdateRequest.getStartDateM(), workUpdateRequest.getStartDateD(),
    		workUpdateRequest.getStartDateHH(), workUpdateRequest.getStartDateMI(), "0");
    work.setStartDate(dateS);

    // 終了日時
    Date dateE = DateEdit.getDateTime(workUpdateRequest.getEndDateY(), workUpdateRequest.getEndDateM(), workUpdateRequest.getEndDateD(),
    		workUpdateRequest.getEndDateHH(), workUpdateRequest.getEndDateMI(), "0");
    work.setEndDate(dateE);
/*
    // 開始日時
    String strDateS = workUpdateRequest.getStartDateY() + "/"
			+ String.format("%02d", Integer.parseInt(workUpdateRequest.getStartDateM())) + "/"
			+ String.format("%02d", Integer.parseInt(workUpdateRequest.getStartDateD())) + " "
			+ String.format("%02d", Integer.parseInt(workUpdateRequest.getStartDateHH())) + ":"
			+ String.format("%02d", Integer.parseInt(workUpdateRequest.getStartDateMI())) + ":00";

    SimpleDateFormat sdFormatS = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
    Date dateS = sdFormatS.parse(strDateS);

    work.setStartDate(dateS);


    // 終了日時
    String strDateE = workUpdateRequest.getEndDateY() + "/"
			+ String.format("%02d", Integer.parseInt(workUpdateRequest.getEndDateM())) + "/"
			+ String.format("%02d", Integer.parseInt(workUpdateRequest.getEndDateD())) + " "
			+ String.format("%02d", Integer.parseInt(workUpdateRequest.getEndDateHH())) + ":"
			+ String.format("%02d", Integer.parseInt(workUpdateRequest.getEndDateMI())) + ":00";
 
    SimpleDateFormat sdFormatE = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
    Date dateE = sdFormatE.parse(strDateE);

    work.setEndDate(dateE);
*/
	// 更新日
    work.setUpdateDate(new Date());

    workRepository.save(work);
  }

  /**
   * 勤怠情報 物理削除
   * @param id ユーザーID
   */
  public void delete(Long id) {
    Work work = findById(id);
    workRepository.delete(work);
  }
 
}