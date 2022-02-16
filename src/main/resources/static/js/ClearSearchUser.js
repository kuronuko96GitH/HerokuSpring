  const $window = window;
  const $doc = document;

  const $btnClear = $doc.getElementById('clear_user_button');

  const $txtAge = $doc.getElementById('age');
  const $txtEndAge = $doc.getElementById('endAge');

  const $chkMale = $doc.getElementById('checkMale');
  const $chkFemale = $doc.getElementById('checkFemale');

  $btnClear.addEventListener('click', (e) => {
    //ユーザー情報一覧の検索条件クリアボタンをクリックした時、
    //検索条件の入力項目を初期化する。
    $txtAge.value= '';
    $txtEndAge.value= '';
    
    $chkMale.checked = false;
    $chkFemale.checked = false;
  });
