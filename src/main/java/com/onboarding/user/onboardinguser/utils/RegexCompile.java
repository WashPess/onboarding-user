package com.onboarding.user.onboardinguser.utils;

import java.util.regex.Pattern;

public class RegexCompile {

	private RegexCompile() {}
	
	public static final Pattern HasCharSpecialForEmail = Pattern.compile("[$!&%+=<>(){ }*~^¨º°ª|\\/`:;]", Pattern.UNICODE_CASE);
	public static final Pattern HasCharSpecialForDocument = Pattern.compile("[$!&%+=<>(){}*~^¨º°ª|`\\´:;]", Pattern.UNICODE_CASE);
	public static final Pattern HasCharSpecialSimple = Pattern.compile("[~!@#$%^&*=:;'<>,?|]", Pattern.UNICODE_CASE);
	public static final Pattern OnlyNumberForDocument = Pattern.compile("^[0-9-./]+$", Pattern.CASE_INSENSITIVE);
	public static final Pattern OnlyLetter = Pattern.compile("^[a-zA-Z ]+$", Pattern.CASE_INSENSITIVE);
	public static final Pattern OnlyLetterForNickName = Pattern.compile("^[a-zA-Z0-9]+$", Pattern.CASE_INSENSITIVE);
	public static final Pattern Password = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&!+=])(?=\\S+$).{8,60}$");
	public static final Pattern HasLetterUpperCase = Pattern.compile(".*[A-Z].*");
	public static final Pattern HasLetterLowerCase = Pattern.compile(".*[a-z].*");
}