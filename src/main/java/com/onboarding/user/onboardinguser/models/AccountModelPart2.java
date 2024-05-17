package com.onboarding.user.onboardinguser.models;

import java.util.Date;

//learn donkey

enum Gender{
    Male, Female;
}

class AccountModelPart2 {

    String firstName = "";
    String lastName = "";
    String cpf = "";
    String wathsapp = "";
    String link = "";
    String uuid = "";
    Date birthday = new Date();
    Gender gender = Gender.Male;
    String currency = "";
    String language = "";
    String nationality = "";
    Boolean optin = false;
    Date createdAt = new Date();
    Date updateAt = new Date();



    

}