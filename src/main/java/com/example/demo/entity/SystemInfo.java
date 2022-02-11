package com.example.demo.entity;

public class SystemInfo {

	private String title;

	private String sysdateYMD;

	private String sysdateYoubi;

	// 戻るボタンを作成する時に必要な、画面遷移一つ前の画面ID情報を格納
//	private String backId;


	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	// 現在日
	public String getSysdateYMD() {
		return sysdateYMD;
	}

	public void setSysdateYMD(String sysdateYMD) {
		this.sysdateYMD = sysdateYMD;
	}

	// 現在日の曜日
	public String getSysdateYoubi() {
		return sysdateYoubi;
	}

	public void setSysdateYoubi(String sysdateYoubi) {
		this.sysdateYoubi = sysdateYoubi;
	}
}