package com.motostiburon.utils;

public class StringUtils {

	public static boolean isNotNullOrWhiteSpaces(String s) {
		return !((s == null) || "".equals(s.trim()));
	}
}
