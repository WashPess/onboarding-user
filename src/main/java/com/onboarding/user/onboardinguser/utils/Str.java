package com.onboarding.user.onboardinguser.utils;

public class Str {

	public static boolean Empty(String str) {
		if(str == null) {
			return true;
		}
		return str.length() == 0;
	}
}