package com.gjie.netty.http.resp;

/**
 * @author j_gong
 * @date 2019/8/23 4:51 PM
 */
public class HttpExceptionResponse {
    private Integer responseCode;
    private String responseMessage;
    private String errorMessage;



    public HttpExceptionResponse(Integer responseCode, String responseMessage, String errorMessage) {
        this.responseCode = responseCode;
        this.responseMessage = responseMessage;
        this.errorMessage = errorMessage;
    }

    public Integer getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(Integer responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
