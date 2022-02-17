package com.example.demo.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.ForwardAuthenticationFailureHandler;

import com.example.demo.auth.DatabaseUserDetailsService;

@EnableWebSecurity
public class SecurityConf extends WebSecurityConfigurerAdapter {

	@Autowired
	private DatabaseUserDetailsService userDetailsService;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {

		// ログイン画面で入力した情報と、データベースから取得したデータが正しいかのチェック処理。
		auth.userDetailsService(userDetailsService);
		
		// メモリ上で管理してるユーザー情報
		// （パスワードは平文ではなく、ハッシュ化された状態とする）
//		auth.inMemoryAuthentication()
//			.withUser("user").password(passwordEncoder().encode("password")).roles("USER");
		
//		String strDebug = "";

	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// Webアプリケーションが管理してるリソースへのアクセス制御
		// このメソッド内をコメントアウトしたら、ログインをしなくても全ページにアクセスできます。
/* (旧)ログイン処理
		http.authorizeRequests()
			.antMatchers("/css/**").permitAll()
			.anyRequest().authenticated()
			.and().formLogin()
			.loginPage("/login").usernameParameter("email").passwordParameter("password").permitAll();
*/

		http.authorizeRequests()
			.antMatchers("/css/**", "/js/**", "/img/**").permitAll()
			.antMatchers("/user/addlogin").permitAll()
			.antMatchers("/user/createlogin").permitAll()
			.antMatchers("/sysError").permitAll()
			.anyRequest().authenticated()
		// ログイン
		.and()
			.formLogin().loginPage("/login")
//			.loginProcessingUrl("/Login.do")	// HTML側でログインの実行処理を別名にしたい場合。
//			.formLogin().loginPage("/Login.html")
			.failureHandler(new ForwardAuthenticationFailureHandler("/loginerr"))
//			.failureHandler(new ForwardAuthenticationFailureHandler("/Login.err"))
			.usernameParameter("email").passwordParameter("password").permitAll()
		// ログアウト
		.and()
			.logout()
			.logoutUrl("/logout")
//			.logoutSuccessUrl("/login")
			.logoutSuccessUrl("/")
//			.logoutUrl("/Logout.do")	// HTML側でログアウトの実行処理を別名にしたい場合。
//			.logoutSuccessUrl("/Login.html")
			.invalidateHttpSession(true)
			.deleteCookies("JSESSIONID");
		
		
		
		// 補足説明：カスタムログインページを呼び出すためのリクエスト
		
//			.and().formLogin()
//			.loginPage("/login").permitAll();	

		
		// 補足説明：
//		.formLogin().loginPage("/login")
//		.loginProcessingUrl("/Login.do")

		// 上記の例の場合、本来はHTML側から("/login")のリクエストでログイン処理が実行されるが、
		// .loginProcessingUrlを追記することで、HTML側からは("/Login.do")の別名でログイン処理が実行されるようになります。
		
		
//		.antMatchers("/css/**", "/js/**", "/img/**").permitAll()
		//
		// antMatchers()　cssだけは、ログイン認証無しでも表示ができるように許可するメソッド
		//
		// permitAll()　ログイン後に、全てのリクエストを許可するメソッド
				
		// 補足説明：
		// anyRequest()…全てのリクエストに対して
		// authenticated()…認証済みであること
		// ログインしないとWebアプリケーションのリソースに一切アクセスができない

		
		
//		.failureHandler(new ForwardAuthenticationFailureHandler("/Login.err"))
		
		// 補足説明：
		// failureHandler()…認証が失敗した時の事後処理をするクラス

		
//		.and().httpBasic();
		
		//
		// 認証方式は .httpBasic()　→　htmlにアクセスしたら、ログイン用のダイアログ（ポップアップ）が表示される形式です。


//		.and().formLogin();
				
		// 認証方式は .formLogin()　→　フレームワークで用意されたログイン画面。ただし、デザインは変更できない。

		
		

		//　補足説明：ログアウト
//				.logout().logoutUrl("/logout")
//				
//				springframework.securityがログアウト処理を実行してくれる。
		//
//				.invalidateHttpSession(true)
//				ログアウトした後に、Httpセッションを破棄（無効にしている）
		//	
//				.deleteCookies("JSESSIONID");
//				クッキーのセッション情報を削除
				

		//　補足説明：logoutSuccessUrl()
		// ログアウト成功時の後処理をしておかないと、
		// 再ログイン後に、login?logout　にリクエストを飛ばして、ユーザーからログインエラーをしたように見えてしまうでの。

	}

	// 補足説明：Beanアノテーションは、あらゆるところから、この暗号化メソッドを呼び出せるようにしてる。
	//           このプロジェクト内では、共通の暗号化メソッドと決めている場合に、Beanを使う。
	
	@Bean
	public PasswordEncoder passwordEncoder() {

		// 補足説明：
		// 平文のパスワードから、暗号化（ハッシュ化）する。
		// 一方通行で、暗号化（ハッシュ化）→平文はできない。
		
		return new BCryptPasswordEncoder();
	}

}
