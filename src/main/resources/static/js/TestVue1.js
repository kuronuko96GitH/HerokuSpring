   var app = new Vue({
     el: '#app',
     data: {
       message: 'Hello Vue!',
       name: 'tarou',
     }
   })




   var app = new Vue({
     el: '#app2',
     data: {
		rawHtml01: '<span style="color: red;">Hello World!</span>',
     }
   })






// (component)コンポーネントは、名前付きの再利用可能な Vue インスタンスです。
 Vue.component('todo-item01', {
   template: '<li>This is a todo</li>'
 });
 
 var app = new Vue({
   el: '#app-todo01',
 })





 Vue.component('todo-item02', {
   // (component)コンポーネントはカスタム属性のような プロパティ で値を受け取れる
   // 今回は、todo プロパティで受け取ることを宣言
   props: ['todo'],
   template: '<li>{{ todo.text }}</li>',
 });
 
 var app = new Vue({
   el: '#app-todo02',
   data: {
     studyList: [
       { id: 0, text: 'Vue.js' },
       { id: 1, text: 'Vim' },
       { id: 2, text: 'Python' },
     ],
   },
 })
 
 
 
 
 
//①テンプレートはシンプルにする。
//②複雑なロジックは、(computed)算出プロパティ を使い、そちらに処理をまとめる。
//　Html側で使用する時は、名前を指定するだけにできるよう、意識して開発する。
  let vm3 = new Vue({
   el: '#app3',
 	data: {
   	msg: 'hello world!'
   },
   computed: {
     reversedMessage: function() {
		// split…文字列を分割する。
		// 　　　今回の場合、例『Hello』→ ["H", "e", "l", "l", "o"]
		// reverse…順番を昇順→降順に変える（反転させる）
		// 　　　今回の場合、 ["o", "l", "l", "e", "H"]
		// join…連結(結合)する。
       return this.msg.split('').reverse().join('')
     }
   }
 })
 
 
 
//computedのfunctionで、戻り値があるタイプのサンプルソース
  let vm4 = new Vue({
   el: '#app4',
   data: {
     firstName: '佐藤',
     lastName: '太郎',
   },
   computed: {
     fullName: function () {
       return this.firstName + ' ' + this.lastName
     }
   }
 })
 
//if文
  let vm5 = new Vue({
   el: '#app5',
   data: {
     ok: true,
//     type: 'A', // IF文の'A'に該当するHTMLが表示されます。
//     type: 'B', // IF文の'B'に該当するHTMLが表示されます。
     type: 'C', // IF文のelseに該当するHTMLが表示されます。
   },
 })

  let vm6 = new Vue({
   el: '#app6',
	data: {
	   items2: [
	     {message: 'Foo'},
	     {message: 'Bar'},
	   ]
   },
 })
 