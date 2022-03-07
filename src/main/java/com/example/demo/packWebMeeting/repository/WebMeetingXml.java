package com.example.demo.packWebMeeting.repository;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.demo.auth.AuthUser;
import com.example.demo.packWebMeeting.entity.Topic;
import com.example.demo.packWebMeeting.entity.forms.TopicCreationForm;

@Mapper
public interface WebMeetingXml {

	// (xml版)動的SQLに対応
	public List<Topic> searchAllTopics();

	// 『Topics』テーブルに新規登録
	public void registerTopic(@Param("form") TopicCreationForm form, @Param("user") AuthUser user);
	
	// 『Posts』テーブルに新規登録
	public void registerPost(@Param("form") TopicCreationForm form, @Param("user") AuthUser user);
	
	// 
	public Optional<Topic> findTopic(String topicNo);
}