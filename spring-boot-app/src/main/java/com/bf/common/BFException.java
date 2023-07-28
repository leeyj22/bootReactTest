package com.bf.common;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class BFException extends Exception{

    private static final long serialVersionUID = 1L;
    private ResultCodes inCode;
    private HttpStatus hsCode;
    private ResultCodes outCode;
    private String detailMsg = null;

    public BFException(ResultCodes code) {
        super(code.getMessage());
        this.inCode = code;
    }

    public BFException(ResultCodes code, String detailMsg) {
        super(code.getMessage() + " : " + detailMsg);
        this.inCode      = code;
        this.detailMsg = detailMsg;
    }

    public String getDetailMsg() {
        return detailMsg;
    }

}
