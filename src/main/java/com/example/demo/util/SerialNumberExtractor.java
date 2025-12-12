package com.example.demo.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SerialNumberExtractor {

	// シリアル番号抽出用の正規表現
	private static final Pattern SERIAL_PATTERN = Pattern.compile("\\(serial_number\\)=\\((.*?)\\)");

	/**
	 * エラーメッセージなどから serial_number を抽出する
	 * 例: "Key (serial_number)=(デスクトップPC-010)"
	 *
	 * @param message 元メッセージ
	 * @return 抽出したシリアル番号 or "不明"
	 */
	public static String extract(String message) {
		if (message == null)
			return "不明";

		Matcher matcher = SERIAL_PATTERN.matcher(message);

		if (matcher.find()) {
			return matcher.group(1);
		}

		return "不明";
	}
}