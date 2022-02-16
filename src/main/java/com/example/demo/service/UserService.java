package com.example.demo.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.UserRequest;
import com.example.demo.dto.UserRequestSearch;
import com.example.demo.dto.UserUpdateRequest;
import com.example.demo.dto.UserUpdateRequestPass;
import com.example.demo.entity.User;
import com.example.demo.mdl.DateEdit;
import com.example.demo.repository.RepositoryXml;
import com.example.demo.repository.UserRepository;

/**
 * ユーザー情報 Service
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UserService {

  /**
   * ユーザー情報 UserRepository
   */
  @Autowired
  private UserRepository userRepository;

  /**
   * Repository(XML版・動的SQLに対応)
   */
  @Autowired
  private RepositoryXml repositoryXml;

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
   * ユーザー情報 主キー検索
   * @return 検索結果
   */
  public List<User> findByUserId(Long id) {
    return userRepository.findByUserId(id);
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
    user.setGender(0); // 性別（０：非公開）
//    user.setAddress(userRequest.getAddress());
//    user.setPhone(userRequest.getPhone());
    user.setCreateDate(now);
    user.setUpdateDate(now);
    userRepository.save(user);
  }

  /**
   * （ログイン⇒）ユーザー情報 新規登録
   * @param user ユーザー情報
   */
  public void createLogin(UserRequest userRequest) {
    Date now = new Date();
    User user = new User();
    user.setName(userRequest.getName());
    user.setEmail(userRequest.getEmail());
    user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
    user.setGender(0); // 性別（０：非公開）
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
   * ユーザー情報 該当する『メールアドレス』の件数を検索
   * @return 検索結果
   */
  public Integer findByEmailCnt(String email) {
	  
	  return userRepository.findByEmail(email).size();
  }

  /**
   * ユーザー情報 該当する『ユーザーID』＆『メールアドレス』の件数を検索
   * @return 検索結果
   */
  public Integer findByEmailCnt(Long id, String email) {
	  
	  return userRepository.findByEmail(id, email).size();
  }

  /**
   * ユーザー情報の検索（入力された条件で検索）
   * @return 検索結果
   */
  public List<User> searchUser(UserRequestSearch userRequestSearch) {

	Integer intMale = 0; // 初期値（０：非公開）
	Integer intFemale = 0; // 初期値（０：非公開）

	if (userRequestSearch.getCheckMale() == null) {
		intMale = 0;

	} else if (userRequestSearch.getCheckMale()) {
		// (チェックボックス)男性にチェックあり
		intMale = 1;
	}

	if (userRequestSearch.getCheckFemale() == null) {
		intFemale = 0;

	} else if (userRequestSearch.getCheckFemale()) {
		// (チェックボックス)女性にチェックあり
		intFemale = 2;
	}
	
	// 入力項目で検索条件を変更する。
	return repositoryXml.searchUser(userRequestSearch.getAge(), userRequestSearch.getEndAge(), intMale, intFemale);

  }


  /**
   * ユーザー情報 該当する『年齢』で検索
   * @return 検索結果
   */
  public List<User> findByAge(String startAge, String endAge) {

		// 年齢の入力項目で検索条件を変更する。
		if (startAge != null && endAge == null) {
			// 年齢(開始)のみ。年齢(終了)は空白(Null)。
			return userRepository.findByStartAge(startAge);
		} else if (startAge == null && endAge != null) {
			// 年齢(開始)は空白。年齢(終了)のみ。
			return userRepository.findByEndAge(endAge);
		} else {
			// 年齢(開始)と年齢(開終了)の両方入力あり。
			return userRepository.findByAge(startAge, endAge);
		}

//	  return workRepository.findByDate(userId, startDate, endDate);
  }


  /**
   * ユーザー情報 更新
   * @param user ユーザー情報
   */
  public void update(UserUpdateRequest userUpdateRequest) {
    User user = findById(userUpdateRequest.getId());
    user.setName(userUpdateRequest.getName());
    user.setEmail(userUpdateRequest.getEmail());

    // 性別
    user.setGender(User.getChgGender(userUpdateRequest.getGender()));
//    user.setGender(Integer.valueOf(userUpdateRequest.getGender()));

    user.setAddress(userUpdateRequest.getAddress());
    user.setPhone(userUpdateRequest.getPhone());

    // 生年月日
//	if (userUpdateRequest.getBirthdayY().isEmpty()
//			&& userUpdateRequest.getBirthdayM().isEmpty()
//			&& userUpdateRequest.getBirthdayD().isEmpty() ) {
	if (userUpdateRequest.getBirthdayY() == null
			&& userUpdateRequest.getBirthdayM() == null
			&& userUpdateRequest.getBirthdayD() == null ) {
		// 生年月日が空白の場合
	    user.setBirthday(null);
	} else {
	    Date dateBirthday = DateEdit.getDate(userUpdateRequest.getBirthdayY(), userUpdateRequest.getBirthdayM(), userUpdateRequest.getBirthdayD());
	    user.setBirthday(dateBirthday);
	}
    // 年齢age
    user.setAge(userUpdateRequest.getAge());

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