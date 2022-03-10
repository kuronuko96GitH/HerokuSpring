package com.example.demo.packWebMeeting.entity;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.example.demo.auth.AuthUser;
import com.example.demo.entity.User;

/**
 * 投稿クラス Entity
 */
public class Post {
	
	private int postNo;
	
	private String postText;
	
	private Date postedAt;
	
	private User user;
	
	private List<PostRating> ratings;

	public boolean isFirstPost() {
		// 一番最初の投稿かをチェックするメソッド。
		// 一番最初の投稿の場合、Trueとして１を返す。
		return postNo == 1;
	}
	
	public PostRating getRateByUser(AuthUser authUser) {
		
		// ログインしたユーザーIDと同じユーザーを、評価情報リストの中からフィルター検索する。
		// 該当するデータがなかった場合、新しい評価情報を作成する。
		PostRating postRating = ratings.stream()
		.filter(p -> Objects.equals(p.getRater().getId(), authUser.getId()))
		.findFirst().orElse(new PostRating());
		return postRating;
	}
	// 【補足説明】Stream API
	// Streamとは、Listなどのコレクションに対する操作をサポートするクラスです。
	// オブジェクトのListに対して、条件による絞り込みや合計値や平均値などの集計処理を行うメソッドが用意されています。
	// for文の処理に比べて、処理がわかりやすいようにコードを記述できるのが特徴です。
	//
	//基本的な流れは次のとおりです。
	//①コレクションからStreamを取得
	//②Streamに対して、絞り込みや要素の変換などの中間処理を行う
	//③変換したStreamに対して結果の取得など行う終端処理を行う


	public List<PostRating> goodRatings() {
		// getRating()が１の時は高評価
		// Stream APIのフィルタ機能により、高評価データを取得する。
		return ratings.stream().filter(p -> p.getRating() == 1)
						.collect(Collectors.toList());
	}
	
	public List<PostRating> badRatings() {
		// getRating()がー１の時は低評価
		// Stream APIのフィルタ機能により、低評価データを取得する。
		return ratings.stream().filter(p -> p.getRating() == -1)
						.collect(Collectors.toList());
	}
	public boolean isPostedBy(AuthUser authUser) {
		// 既存の投稿情報のユーザーIDと、ログインしてるユーザーIDが同じかをチェック
		return Objects.equals(user.getId(), authUser.getId());
	}
	public int getPostNo() {
		return postNo;
	}

	public void setPostNo(int postNo) {
		this.postNo = postNo;
	}

	public String getPostText() {
		return postText;
	}

	public void setPostText(String postText) {
		this.postText = postText;
	}

	public Date getPostedAt() {
		return postedAt;
	}

	public void setPostedAt(Date postedAt) {
		this.postedAt = postedAt;
	}
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
/*
	public AuthenticatedUser getAuthor() {
		return author;
	}

	public void setAuthor(AuthenticatedUser author) {
		this.author = author;
	}
*/
	public List<PostRating> getRatings() {
		return ratings;
	}

	public void setRatings(List<PostRating> ratings) {
		this.ratings = ratings;
	}
}