  const $window = window;
  const $doc = document;

  const $btnClear = $doc.getElementById('clear_work_button');

  const $txtSearchDateY = $doc.getElementById('searchDateY');
  const $txtSearchDateM = $doc.getElementById('searchDateM');

  const $txtStartDate = $doc.getElementById('startDate');
  const $txtEndDateYMD = $doc.getElementById('endDateYMD');
/*【旧版】年月日のテキストボックス３つが別々ver
  const $txtStartDateY = $doc.getElementById('startDateY');
  const $txtStartDateM = $doc.getElementById('startDateM');
  const $txtStartDateD = $doc.getElementById('startDateD');

  const $txtEndDateY = $doc.getElementById('endDateY');
  const $txtEndDateM = $doc.getElementById('endDateM');
  const $txtEndDateD = $doc.getElementById('endDateD');
*/

  $btnClear.addEventListener('click', (e) => {
    //勤退情報一覧の検索条件クリアボタンをクリックした時、
    //検索条件の入力項目を初期化する。
    $txtSearchDateY.value= '';
    $txtSearchDateM.value= '';

    $txtStartDate.value= '';
    $txtEndDateYMD.value= '';
/*【旧版】年月日のテキストボックス３つが別々ver
    $txtStartDateY.value= '';
    $txtStartDateM.value= '';
    $txtStartDateD.value= '';
    $txtEndDateY.value= '';
    $txtEndDateM.value= '';
    $txtEndDateD.value= '';
*/
  });
