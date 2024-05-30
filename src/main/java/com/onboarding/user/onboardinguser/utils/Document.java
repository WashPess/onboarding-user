package com.onboarding.user.onboardinguser.utils;


public class Document {

	public static String pad(String document) {
		return String.format("%1$" + 14 + "s", document).replace(' ', 'X');
	}

	public static String clear(String document) {
		return document.replaceAll("[-+.^:,/]","");
	}

	public static String mask(String document) {
		boolean isCpf = document.contains("X");
		document = document.replaceAll("[^0-9]", "");
		if(isCpf) {
      		return document.replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
		}
		return document.replaceAll("(\\d{2})(\\d{3})(\\d{3})(\\d{4})(\\d{2})", "$1.$2.$3/$4-$5");
	}
}

