package com.example.demo.packWebMeeting.entity.forms;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class TopicCreationForm {
	
	@NotBlank(message = "トピックタイトルを入力して下さい。")
	@Size(max = 64, message = "トピックタイトルは64桁以内で入力して下さい。")
	private String subject;
	
	@NotBlank(message = "投稿内容を入力して下さい。")
	@Size(max = 640, message = "投稿内容は640桁以内で入力して下さい。")
	private String primaryPost;
	
	private String topicNo;
	
	private boolean error;

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getPrimaryPost() {
		return primaryPost;
	}

	public void setPrimaryPost(String primaryPost) {
		this.primaryPost = primaryPost;
	}

	public String getTopicNo() {
		return topicNo;
	}

	public void setTopicNo(String topicNo) {
		this.topicNo = topicNo;
	}

	public boolean isError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}
}