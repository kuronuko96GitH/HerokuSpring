package com.example.demo.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
   * 勤怠情報 RepositoryStr
   * (String型の項目指定が可能)
   */
//  @Autowired
//  private WorkRepositoryStr userRepositoryStr;


  /**
   * 勤怠情報 全検索
   * @return 検索結果
   */
  public List<Work> searchAll() {
	  // id(昇順)
	  return workRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
	  // id(降順)
//	  return userRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
	  // ソート指定なし
//	      return userRepository.findAll();
  }

  /**
   * 勤怠情報 新規登録
   * @param user 勤怠情報
   * @throws ParseException 
   */
//  public void create(WorkRequest workRequest) {
  public void create(WorkRequest workRequest, Long userID) throws ParseException {
    Date now = new Date();
    Work work = new Work();

    work.setUserId(userID);
//    work.setUserId(workRequest.getUserId());
    work.setContent(workRequest.getContent());
    
/*
    String strDate = "2017/03/02 01:23:45";
    SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
    Date date = sdFormat.parse(strDate);
*/

    // 開始日時
    String strDateS = workRequest.getStartDate() + ":00";
    SimpleDateFormat sdFormatS = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
    Date dateS = sdFormatS.parse(strDateS);

    work.setStartDate(dateS);


    // 終了日時
    String strDateE = workRequest.getEndDate() + ":00";
    SimpleDateFormat sdFormatE = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
    Date dateE = sdFormatE.parse(strDateE);

    work.setEndDate(dateE);


    work.setCreateDate(now);
    work.setUpdateDate(now);
 
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

//  public Work findByUserID(@PathVariable("userId") Long userId) {
  public List<Work> findByUserID(@PathVariable("userId") Long userId) {

//	  return userRepositoryStr.findByName(name).size();
	  
	return workRepository.findByUserID(userId);
  }

  /**
   * 勤怠情報 該当する『ユーザー名』で検索
   * @return 検索結果
   */
/*
  public User findByName(String name) {
	  
//	  return userRepositoryStr.findByName(name).size();
	  
		return userRepositoryStr.findByName(name).get(0);
  }
*/
  /**
   * 勤怠情報 該当する『ユーザー名』の件数を検索
   * @return 検索結果
   */
/*
  public Integer findByNameCnt(String name) {
	  
	  return userRepositoryStr.findByName(name).size();
  }
*/
  /**
   * 勤怠情報 該当する『メールアドレス』の件数を検索
   * @return 検索結果
   */
/*
  public Integer findByEmailCnt(String email) {
	  
	  return userRepositoryStr.findByEmail(email).size();
  }
*/

  /**
   * 勤怠情報 更新
   * @param user 勤怠情報
   */
//  public void update(WorkUpdateRequest workUpdateRequest, Long userID) {
  public void update(WorkUpdateRequest workUpdateRequest, Long userID) throws ParseException {
    Work work = findById(workUpdateRequest.getId());

    work.setUserId(userID);
    work.setContent(workUpdateRequest.getContent());

//  work.setStartDate(workUpdateRequest.getStartDate());
//  work.setEndDate(workUpdateRequest.getEndDate());

    // 開始日時
    String strDateS = workUpdateRequest.getStartDate() + ":00";
    SimpleDateFormat sdFormatS = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
    Date dateS = sdFormatS.parse(strDateS);

    work.setStartDate(dateS);


    // 終了日時
    String strDateE = workUpdateRequest.getEndDate() + ":00";
    SimpleDateFormat sdFormatE = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
    Date dateE = sdFormatE.parse(strDateE);

    work.setEndDate(dateE);
    
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