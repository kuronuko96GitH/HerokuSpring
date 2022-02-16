  const $window = window;
  const $doc = document;

  const $btnClear = $doc.getElementById('clear_work_button');

  const $txtSearchDateY = $doc.getElementById('searchDateY');
  const $txtSearchDateM = $doc.getElementById('searchDateM');

  const $txtStartDateY = $doc.getElementById('startDateY');
  const $txtStartDateM = $doc.getElementById('startDateM');
  const $txtStartDateD = $doc.getElementById('startDateD');

  const $txtEndDateY = $doc.getElementById('endDateY');
  const $txtEndDateM = $doc.getElementById('endDateM');
  const $txtEndDateD = $doc.getElementById('endDateD');

  $btnClear.addEventListener('click', (e) => {
    //勤退情報一覧の検索条件クリアボタンをクリックした時、
    //検索条件の入力項目を初期化する。
    $txtSearchDateY.value= '';
    $txtSearchDateM.value= '';

    $txtStartDateY.value= '';
    $txtStartDateM.value= '';
    $txtStartDateD.value= '';
    $txtEndDateY.value= '';
    $txtEndDateM.value= '';
    $txtEndDateD.value= '';
  });
