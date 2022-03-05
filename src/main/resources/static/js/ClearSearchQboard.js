  const $window = window;
  const $doc = document;

  const $btnClear = $doc.getElementById('clear_qboard_button');

  const $txtContent = $doc.getElementById('content1');

  const $radioKeyValue1 = $doc.getElementById('radioKeyValue1');

  $btnClear.addEventListener('click', (e) => {
    //質問板情報一覧の検索条件クリアボタンをクリックした時、
    //検索条件の入力項目を初期化する。
    $txtContent.value= '';
    
    $radioKeyValue1.checked = true;
  });
