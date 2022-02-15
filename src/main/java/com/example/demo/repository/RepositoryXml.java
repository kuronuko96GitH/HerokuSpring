package com.example.demo.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.entity.User;

@Mapper
public interface RepositoryXml {

	// (xml版)動的SQLに対応
	public List<User> searchUser(String startAge, String endAge, Integer intMale, Integer intFemale);

}