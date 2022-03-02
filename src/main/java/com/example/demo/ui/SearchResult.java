package com.example.demo.ui;

import java.util.List;

public class SearchResult<E> {

	private int pageFromIndex; // （画面に表示される）始まりのn件目情報（実際の件数データから-1された状態で格納されてます）

	private int pageFrom; // 選択できるページ数の始まりのページ数

	private int pageTo; // 選択できるページ数の終わりのページ数

	private int currentPage; // 現在のページ

	private int recordPerPage; // １ページあたりの表示件数

	private int totalRecordCount; // 検索結果の総件数

	private int totalPageCount;

	private List<E> entities;

	public SearchResult(int totalRecordCount, int recordPerPage) {

		this.totalRecordCount = totalRecordCount; // 検索結果の総件数
		this.recordPerPage = recordPerPage; // １ページあたりの表示件数

		this.totalPageCount = (this.totalRecordCount / this.recordPerPage) + 
								(this.totalRecordCount % this.recordPerPage > 0 ? 1 : 0);
								// （端数は切り上げとする）
								//  【補足説明】
								//	①（this.totalRecordCount % this.recordPerPage > 0 ? 1 ～）
								//	　余りが０より大きい場合（端数が出た場合）は、１を代入する。
								//  ②（～ 0 ? 1 : 0）
								//	　余りが０の場合は、０を代入する。
	}

	public void moveTo(int page) {
		// ページの切り替え

		// 現在のページ
		this.currentPage = page;

		this.pageFrom = Math.max(page - 1, 2);
		// 【補足説明】Math.min…２つのうち、どちらか小さい方を取得する。
		this.pageTo = Math.min(this.pageFrom + 2, totalPageCount -1);
		// 【補足説明】Math.max…２つのうち、どちらか大きい方を取得する。
		this.pageFrom = Math.max(this.pageTo - 2, 2);
	}

	public int getPageFromIndex() {
		return pageFromIndex;
	}

	public void setPageFromIndex(int pageFromIndex) {
		this.pageFromIndex = pageFromIndex;
	}

	public int getPageFrom() {
		return pageFrom;
	}

	public void setPageFrom(int pageFrom) {
		this.pageFrom = pageFrom;
	}

	public int getPageTo() {
		return pageTo;
	}

	public void setPageTo(int pageTo) {
		this.pageTo = pageTo;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getRecordPerPage() {
		return recordPerPage;
	}

	public void setRecordPerPage(int recordPerPage) {
		this.recordPerPage = recordPerPage;
	}

	public int getTotalRecordCount() {
		return totalRecordCount;
	}

	public void setTotalRecordCount(int totalRecordCount) {
		this.totalRecordCount = totalRecordCount;
	}

	public int getTotalPageCount() {
		return totalPageCount;
	}

	public void setTotalPageCount(int totalPageCount) {
		this.totalPageCount = totalPageCount;
	}

	public List<E> getEntities() {
		return entities;
	}

	public void setEntities(List<E> entities) {
		this.entities = entities;
	}
}
