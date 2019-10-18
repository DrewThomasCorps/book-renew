package com.bookrenew.api.security;

class SecurityConstants {
    static final String SECRET = "JDK832kt89!#@gjdklsjgadsjkl*k4l3Gsdfjoi$!jiotgds89aSFSAJGO923qfgdasFDASLGdsajlg";
    static final long EXPIRATION_TIME = 864_000_000; // 10 days
    static final String TOKEN_PREFIX = "Bearer ";
    static final String HEADER_STRING = "Authorization";
    static final String SIGN_UP_URL = "/users";
}
