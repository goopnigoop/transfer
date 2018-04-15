package com.demo.example.exception;

/**
 * The type App exception.
 */
public class AppException extends Exception {
    private static final long serialVersionUID = -8999932578270387947L;

    /**
     * The Status.
     */
    private Integer status;

    /**
     * The Code.
     */
    private int code;

    /**
     * The Developer message.
     */
    private String developerMessage;

    /**
     * Instantiates a new App exception.
     *
     * @param status           the status
     * @param code             the code
     * @param message          the message
     * @param developerMessage the developer message
     */
    public AppException(int status, int code, String message,
                        String developerMessage) {
        super(message);
        this.status = status;
        this.code = code;
        this.developerMessage = developerMessage;
    }

    /**
     * Instantiates a new App exception.
     */
    public AppException() { }

    /**
     * Gets status.
     *
     * @return the status
     */
    public int getStatus() {
        return status;
    }

    /**
     * Sets status.
     *
     * @param status the status
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * Gets code.
     *
     * @return the code
     */
    public int getCode() {
        return code;
    }

    /**
     * Sets code.
     *
     * @param code the code
     */
    public void setCode(int code) {
        this.code = code;
    }

    /**
     * Gets developer message.
     *
     * @return the developer message
     */
    public String getDeveloperMessage() {
        return developerMessage;
    }

    /**
     * Sets developer message.
     *
     * @param developerMessage the developer message
     */
    public void setDeveloperMessage(String developerMessage) {
        this.developerMessage = developerMessage;
    }


}
