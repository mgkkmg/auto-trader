package com.mgkkmg.globalutil.util;

public class NumberUtils {

	public static boolean isNumeric(String strNum) {
		if (strNum == null) {
			return false;
		}
		return strNum.matches("-?\\d+(\\.\\d+)?");  // 정수 및 부동 소수점 숫자에 대한 패턴
	}

	public static double parseBalance(String balance) {
		if (balance == null || balance.isEmpty()) {
			return 0.0;
		}

		try {
			return Double.parseDouble(balance);
		} catch (NumberFormatException e) {
			return 0.0;
		}
	}
}
