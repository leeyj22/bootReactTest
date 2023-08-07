package com.bf.common;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum AuthType {
    LOGIN("LOGIN","로그인"),
    CERT("CERT","본인인증");

    @Getter
    private String code;
    @Getter
    private String desc;
}
