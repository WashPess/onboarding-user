package com.onboarding.user.onboardinguser.utils;

@SuppressWarnings("squid:S6353")
public class Document {

	private Document() {
	}

	public static String pad(String document) {
		return ("%1$" + 14 + "s").formatted(document).replace(' ', 'X');
	}

	public static String clear(String document) {
		String doc = document.replaceAll("[-+.^:,/]","");
		doc = doc.replaceAll("[^0-9]", "");
		return doc;
	}

	public static String mask(String document) {
		boolean isCpf = document.contains("X");
		document = Document.clear(document);
		if(isCpf) {
      		return document.replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
		}
		return document.replaceAll("(\\d{2})(\\d{3})(\\d{3})(\\d{4})(\\d{2})", "$1.$2.$3/$4-$5");
	}
}

