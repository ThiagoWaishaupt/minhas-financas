package com.thiago.minhasfinancas.util;

public class Constants {

    // <-Error Messages->
    public static final String INVALID_DESCRIPTION = "Invalid Description.";
    public static final String INVALID_MONTH = "Invalid Month.";
    public static final String INVALID_YEAR = "Invalid Year.";
    public static final String INVALID_USER = "Inform a User.";
    public static final String INVALID_VALUE = "Invalid Value.";
    public static final String INVALID_RELEASE_TYPE = "Inform a Release Type.";


    // <-User Endpoints->
    public static final String USER_BASE_URL = "/api/users";
    public static final String USER_AUTHENTICATE = "/authenticate";
    public static final String USER_BALANCE = "/balance/{id}";
}
