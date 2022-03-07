package com.example.demo.packWebMeeting.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.demo.auth.AuthUser;
//import com.rugbyaholic.techshare.auth.AuthenticatedUser;

// 自動採番クラス（※採番テーブルを利用して、Java側で自動採番処理をするクラス）
@Mapper
public interface NumberingXml {

	// トピック番号コード
	public static final String NUMBERING_CODE_TOPICNO = "T00";

	public String issueNumber(@Param("numberingCode") String numberingCode, @Param("availYear") String availYear);

	public void next(@Param("numberingCode") String numberingCode, @Param("availYear") String availYear,
						@Param("user") AuthUser user);

//	public static final String NUMBERING_CODE_EMPNO = "E00";	// 社員番号
}
