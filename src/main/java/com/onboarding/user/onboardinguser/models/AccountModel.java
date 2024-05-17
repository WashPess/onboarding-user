package com.onboarding.user.onboardinguser.models;

import java.util.Date;

enum Gender {
	Male, Female, Void;
}

class AccountModel {
	
	String marital = "";
	String nickname = "";
	Date birthday = new Date();
	String currency= "";
	String language = "";
	String rg = "";
	String cpf = "";
	String uuid= "";
	String nationality = "";
	String picturePart = "";
	String document = "";
	boolean optin = false;
	Gender gender = Gender.Male;
    Date createdAt = new Date();
    Date updateAt = new Date();

}