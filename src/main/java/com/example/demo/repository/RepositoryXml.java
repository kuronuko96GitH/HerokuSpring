package com.example.demo.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.entity.Qboard;
import com.example.demo.entity.User;
import com.example.demo.entity.Work;

@Mapper
public interface RepositoryXml {

	// (xml版)動的SQLに対応
	public List<User> searchUser(String startAge, String endAge, Integer intMale, Integer intFemale);

	// (xml版)動的SQLに対応
	public List<Work> searchWork(Long userId, String startDate, String endDate, Integer fromIndex, Integer limitCnt);

	// 条件入力による検索画面の（ページング用）件数を取得
	public Integer countWork(Long userId, String startDate, String endDate);

	// (xml版)動的SQLに対応
	public List<Qboard> searchQboard(Long userId, String strContent1, Integer fromIndex, Integer limitCnt);

	// 条件入力による検索画面の（ページング用）件数を取得
	public Integer countQboard(Long userId, String strContent1);
}