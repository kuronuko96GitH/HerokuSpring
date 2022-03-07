package com.example.demo.packWebMeeting.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.auth.AuthUser;
import com.example.demo.packWebMeeting.common.LogRequired;
import com.example.demo.packWebMeeting.entity.Topic;
import com.example.demo.packWebMeeting.entity.forms.TopicCreationForm;
import com.example.demo.packWebMeeting.repository.NumberingXml;
import com.example.demo.packWebMeeting.repository.WebMeetingXml;
//import com.example.demo.packWebMeeting.repository.WebMeetingRepository;

@Service
public class WebMeetingService {
	@Autowired
	private NumberingXml numberingRepository;
	
	@Autowired
	private WebMeetingXml webMeetingRepository;

	@LogRequired
	public List<Topic> loadTopics() {
		return webMeetingRepository.searchAllTopics();
	}
	
	@Transactional(rollbackFor = Throwable.class)
	@LogRequired
	public void registerNewTopic(TopicCreationForm form, AuthUser user) {
		// トピック番号の発番
		String availYear = new SimpleDateFormat("yyyy").format(new Date());
		
		// 最新のトピック番号を取得する。
		String NewTopicNo = numberingRepository.issueNumber(NumberingXml.NUMBERING_CODE_TOPICNO, availYear);
		// 左側をゼロ埋めし、５桁の文字列に変更する。
		NewTopicNo = String.format("%05d", Integer.parseInt(NewTopicNo));

		form.setTopicNo(availYear + NewTopicNo);
//		form.setTopicNo(availYear + numberingRepository.issueNumber(
//						NumberingXml.NUMBERING_CODE_TOPICNO, availYear));
		// 番号管理台帳の更新（採番用テーブルのトピック番号も最新の番号に更新する）
		numberingRepository.next(NumberingXml.NUMBERING_CODE_TOPICNO, availYear, user);
		// トピックテーブルへの新規登録
		webMeetingRepository.registerTopic(form, user);
		// 投稿テーブルへの新規登録
		webMeetingRepository.registerPost(form, user);
	}


	// 追加投稿
	@Transactional(rollbackFor = Throwable.class)
	@LogRequired
	public Topic appendPost(TopicCreationForm form, AuthUser user) {
		// 投稿テーブルへの新規登録
		webMeetingRepository.registerPost(form, user);
		return webMeetingRepository.findTopic(form.getTopicNo())
				.orElse(new Topic());
	}
	
	// トピック画面の『追加投稿した箇所だけ』、再描画する
	@Transactional(rollbackFor = Throwable.class)
	@LogRequired
	public Topic reloadTopic(String topicNo) {
		return webMeetingRepository.findTopic(topicNo)
				.orElse(new Topic());
	}
}