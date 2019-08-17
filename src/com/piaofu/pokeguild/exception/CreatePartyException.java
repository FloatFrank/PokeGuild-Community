package com.piaofu.pokeguild.exception;
/**
 * 公会创建异常
 */
public class CreatePartyException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private String errorCode;

    private boolean propertiesKey = true;

    public CreatePartyException(String info) {
        super(info);
    }
    public CreatePartyException(String errorCode, String info) {
        this(errorCode, info, true);
    }
    public CreatePartyException(String errorCode, String message, Throwable cause) {
        this(errorCode, message, cause, true);
    }
    public CreatePartyException(String errorCode, String message, boolean propertiesKey) {
        super(message);
        this.setErrorCode(errorCode);
        this.setPropertiesKey(propertiesKey);
    }
    public CreatePartyException(String errorCode, String message, Throwable cause, boolean propertiesKey) {
        super(message, cause);
        this.setErrorCode(errorCode);
        this.setPropertiesKey(propertiesKey);
    }
    public CreatePartyException(String message, Throwable cause) {
        super(message, cause);
    }
    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public boolean isPropertiesKey() {
        return propertiesKey;
    }

    public void setPropertiesKey(boolean propertiesKey) {
        this.propertiesKey = propertiesKey;
    }
}

