package com.example.demo.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.UserRequest;
import com.example.demo.dto.UserUpdateRequest;
import com.example.demo.dto.UserUpdateRequestPass;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.UserRepositoryStr;

/**
 * ユーザー情報 Service
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UserService {
 
  /**
   * ユーザー情報 Repository
   */
  @Autowired
  private UserRepository userRepository;
  
  /**
   * ユーザー情報 RepositoryStr
   * (String型の項目指定が可能)
   */
  @Autowired
  private UserRepositoryStr userRepositoryStr;

  
  /**
   * パスワードエンコーダ（暗号化） Repository
   */
  @Autowired
  private PasswordEncoder passwordEncoder;
  
  
  /**
   * ユーザー情報 全検索
   * @return 検索結果
   */
  public List<User> searchAll() {
	  // id(昇順)
	  return userRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
	  // id(降順)
//	  return userRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
	  // ソート指定なし
//	      return userRepository.findAll();
  }

  /**
   * ユーザー情報 新規登録
   * @param user ユーザー情報
   */
  public void create(UserRequest userRequest) {
    Date now = new Date();
    User user = new User();
    user.setName(userRequest.getName());
    user.setEmail(userRequest.getEmail());
    user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
//    user.setPassword(userRequest.getPassword());
    user.setAddress(userRequest.getAddress());
    user.setPhone(userRequest.getPhone());
    user.setCreateDate(now);
    user.setUpdateDate(now);
    userRepository.save(user);
  }

  /**
   * ユーザー情報 主キー検索
   * @return 検索結果
   */
  public User findById(Long id) {
    return userRepository.findById(id).get();
  }

  /**
   * ユーザー情報 該当する『メールアドレス』検索
   * @return 検索結果
   */

  public Integer findByEmail(String email) {
	  
	  return userRepositoryStr.findByEmail(email).size();
	  
//		return userRepositoryStr.findByEmail(email).get(0);
  }

  /**
   * ユーザー情報 該当する『メールアドレス』の件数を検索
   * @return 検索結果
   */
/*
  public User findByEmailCnt(String email) {
	    return userRepositoryStr.findByEmailCnt(email).get(0);
//  public Integer findByEmailCnt(String email) {
//    return Integer.parseInt(userRepositoryStr.findByEmailCnt(email).get(0).getEmail());
  }
*/
  /**
   * ユーザー情報 更新
   * @param user ユーザー情報
   */
  public void update(UserUpdateRequest userUpdateRequest) {
    User user = findById(userUpdateRequest.getId());
    user.setAddress(userUpdateRequest.getAddress());
    user.setName(userUpdateRequest.getName());
    user.setEmail(userUpdateRequest.getEmail());
    user.setPhone(userUpdateRequest.getPhone());
    user.setUpdateDate(new Date());
    userRepository.save(user);
  }

  /**
   * ユーザー情報(パスワードのみ) 更新
   * @param user ユーザー情報
   */
  public void updatePass(UserUpdateRequestPass userUpdateRequest) {
    User user = findById(userUpdateRequest.getId());
//  user.setPassword(userUpdateRequest.getPassword());
    // パスワードはデータベース登録時に暗号化する。
    user.setPassword(passwordEncoder.encode(userUpdateRequest.getPassword()));
    userRepository.save(user);
  }

  /**
   * ユーザー情報 物理削除
   * @param id ユーザーID
   */
  public void delete(Long id) {
    User user = findById(id);
    userRepository.delete(user);
  }
 
}