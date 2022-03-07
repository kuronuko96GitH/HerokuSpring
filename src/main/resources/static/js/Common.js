var setAddress = function(pref, city) {
	$('#pref').val(pref);
	$('#city').val(city);
}

var callbackFunction = function(data) {
	if (data.results == null) {
		alert('該当の住所が見つかりませんでした。');
		return;
	}
	var result = data.results[0];
	pref = result.address1;
	city = result.address2 + result.address3;
	
	setAddress(pref, city);
}

$(function() {
	//　テキストエリア（読み取り専用の時） レイアウト調整	
	$('textarea.readonly').each(function(index, element) {
		$(element).height(0).innerHeight(element.scrollHeight);
	});
	//　テキストエリア（入力時） レイアウト調整	
	$(document).on('input', 'textarea', function() {
		$(this).height(0).innerHeight(this.scrollHeight);
	})

	// CollapseのToggler設定
	// 『検索条件を表示』＆『隠す』の文字テキスト切り替え処理
	// 『トピック作成フォームを表示』＆『隠す』の文字テキスト切り替え処理
	$(document).on('click', '*[data-bs-toggle="collapse"]', function(e) {
		e.preventDefault();
		$(this).find('span').toggleClass('d-none');
	})
/* この処理は必要？
	// Collapse
	$(document).on('click', '#toggler', function() {
		$(this).find('.toggler-text').each(function(_index, element) {
			$(element).toggleClass('d-none');
		});
	});
*/

	// サイドナビゲーション
	$(document).on('click', '.sidenav-toggler, .cover', function() {
		$('.sidenav').toggleClass('visible');
		$('.cover').toggleClass('visible');
	});
/*
	// 画像ファイルのサムネイル取得
	$(document).on('change', '#photo-file', function() {
		var file = $(this).prop('files')[0];
		// 画像以外は処理を停止
		if (! file.type.match('image.*')) {
			// クリア
			$(this).val('');
			alert('画像ファイルを選択してください。');
			return;
		}

		// 画像表示
		var reader = new FileReader();
		reader.onload = function() {
			$('#thumbnail').attr('src', reader.result);
		}
		reader.readAsDataURL(file);
	});
*/
/*
	// 郵便番号検索
	$(document).on('click', '#search-address', function() {
		var baseURL = 'https://zipcloud.ibsnet.co.jp/api/search';
		var zip = $('#zipcode').val();
		
		// 郵便番号未入力チェック
		if (zip == null || zip.length != 7) {
			alert('郵便番号は7桁で入力してください。');
		}
		
		// Ajaxでzipcloudへアクセス
		$.ajax({
			type : 'GET',
			url : baseURL,
			dataType : 'jsonp',
			jsonp : callbackFunction,
			data : {
				zipcode : zip,
				callback : 'callbackFunction'
			}
		});
	});
*/



	// Toast
	// （トースト…ポップアップメージのようなものらしい）
	$(document).on('click', '#toast-close', function() {
		$('#toast').addClass('d-none');
	});
});