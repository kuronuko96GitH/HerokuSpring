  const $window = window;
  const $doc = document;

  const $btnGuest = $doc.getElementById('guest_button');

  const $txtEmail = $doc.getElementById('email');
  const $txtPassword = $doc.getElementById('password');

  $btnGuest.addEventListener('click', (e) => {
    //ゲスト(Guest)ボタンをクリックした時、
    //ゲスト用のユーザー名とパスワードを設定する。
    $txtEmail.value= 'guest@example.com';
    $txtPassword.value= 'guest1234';
  });
