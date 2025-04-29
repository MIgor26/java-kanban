package exception;

public class ErrorResponse {
    private String errorMessage;
    private int errorCode;
    private String url;

    public ErrorResponse(String errorMessage, int errorCode, String url) {
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
        this.url = url;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
