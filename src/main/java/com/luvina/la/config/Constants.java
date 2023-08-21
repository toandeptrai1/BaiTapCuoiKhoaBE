package com.luvina.la.config;

public class Constants {

    private Constants() {
    }

    public static final String SPRING_PROFILE_DEVELOPMENT = "dev";
    public static final String SPRING_PROFILE_PRODUCTION = "prod";
    public static final boolean IS_CROSS_ALLOW = true;

    public static final String ER001 = "ER001";
    public static final String ER002 = "ER002";
    public static final String ER003 = "ER003";
    public static final String ER004 = "ER004";
    public static final String ER005 = "ER005";
    public static final String ER006 = "ER006";
    public static final String ER007 = "ER007";
    public static final String ER008 = "ER008";
    public static final String ER009 = "ER009";
    public static final String ER011 = "ER011";
    public static final String ER012 = "ER012";
    public static final String ER013 = "ER013";
    public static final String ER014 = "ER014";
    public static final String ER018 = " ER018";
    public static final String ER019 = " ER019";
    public static final String ER021 = " ER021";

    public static final String LABEL_ID = "ID";
    public static final String LABEL_EMP_LOGINID = "アカウント名";
    public static final String LABEL_EMP_NAME = "氏名";
    public static final String LABEL_EMP_NAMEKANA = "カタカナ氏名";

    public static final String LABEL_EMP_BIRTHDATE = "カタカナ氏名";
    public static final String LABEL_EMP_EMAIL = "メールアドレス";

    public static final String LABEL_EMP_PHONE = "電話番号";
    public static final String LABEL_EMP_PASSWORD = "パスワード";
    public static final String LABEL_EMP_DEPARTMENT = "グループ";
    public static final String LABEL_CER_ID = "資格";
    public static final String LABEL_CER_START_DATE = "資格交付日";
    public static final String LABEL_CER_END_DATE = "失効日";
    public static final String LABEL_CER_SCORE = "点数";


    public static final String JWT_SECRET = "Luvina-Academe";
    public static final long JWT_EXPIRATION = 160 * 60 * 60; // 7 day

    // config endpoints public
    public static final String[] ENDPOINTS_PUBLIC = new String[]{
            "/**",
            "/employee",
            "/login/**",
            "/error/**"
    };

    // config endpoints for USER role
    public static final String[] ENDPOINTS_WITH_ROLE = new String[]{
            "/user/**"
    };

    // user attributies put to token
    public static final String[] ATTRIBUTIES_TO_TOKEN = new String[]{
            "employeeId",
            "employeeName",
            "employeeLoginId",
            "employeeEmail"
    };
}
