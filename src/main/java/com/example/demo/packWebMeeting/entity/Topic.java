package com.example.demo.packWebMeeting.entity;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import com.example.demo.auth.AuthUser;
//import com.rugbyaholic.techshare.auth.AuthenticatedUser;

/**
 * Topicクラス Entity
 */
public class Topic {
	
	private String topicNo;
	
	private String subject;

	private AuthUser owner;
//	private AuthenticatedUser owner;
	
	private Date createdAt;
	
	private List<Post> posts;

	public boolean isOwnedBy(AuthUser user) {
		return Objects.equals(user.getId(), owner.getId());
	}
/*
	public boolean isOwnedBy(AuthenticatedUser user) {
		return Objects.equals(user.getEmpNo(), owner.getEmpNo());
	}
*/
	public String getTopicNo() {
		return topicNo;
	}

	public void setTopicNo(String topicNo) {
		this.topicNo = topicNo;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}
	public AuthUser getOwner() {
		return owner;
	}

	public void setOwner(AuthUser owner) {
		this.owner = owner;
	}
/*
	public AuthenticatedUser getOwner() {
		return owner;
	}

	public void setOwner(AuthenticatedUser owner) {
		this.owner = owner;
	}
*/
	public List<Post> getPosts() {
		return posts;
	}

	public void setPosts(List<Post> posts) {
		this.posts = posts;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
}