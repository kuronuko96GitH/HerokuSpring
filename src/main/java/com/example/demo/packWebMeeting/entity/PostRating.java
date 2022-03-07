package com.example.demo.packWebMeeting.entity;

import java.util.Date;

import com.example.demo.entity.User;
//import com.rugbyaholic.techshare.auth.AuthenticatedUser;

/**
 * 投稿評価クラス Entity
 */
public class PostRating {

	// 評価者
	private User rater;
//	private AuthenticatedUser rater;

	// 評価情報
	private int rating;

	// 評価日
	private Date ratedAt;

	public User getRater() {
		return rater;
	}

	public void setRater(User rater) {
		this.rater = rater;
	}
/*
	public AuthenticatedUser getRater() {
		return rater;
	}

	public void setRater(AuthenticatedUser rater) {
		this.rater = rater;
	}
*/
	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public Date getRatedAt() {
		return ratedAt;
	}

	public void setRatedAt(Date ratedAt) {
		this.ratedAt = ratedAt;
	}
}