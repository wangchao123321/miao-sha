package com.wangchao.miaosha.exception;

import com.wangchao.miaosha.result.CodeMsg;

public class GlobleException extends RuntimeException {

    private CodeMsg codeMsg;

    public GlobleException(CodeMsg cm) {
        super(cm.toString());
        this.codeMsg = cm;
    }



    public CodeMsg getCodeMsg() {
        return codeMsg;
    }

    public void setCodeMsg(CodeMsg codeMsg) {
        this.codeMsg = codeMsg;
    }
}
