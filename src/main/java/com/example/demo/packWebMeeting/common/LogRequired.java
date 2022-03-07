package com.example.demo.packWebMeeting.common;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Documented
@Retention(RUNTIME)
@Target(METHOD)
public @interface LogRequired {

	// アスペクト思考プログラミング？

	// @LogRequired　アノテーション　をメソッドに設定すれば、
	// メソッドの開始と終了のログを、（Eclipsデバッグ時のコンソールに）自動的に出力します。
}