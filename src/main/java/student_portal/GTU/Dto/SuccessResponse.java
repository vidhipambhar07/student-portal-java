package student_portal.GTU.Dto;

public class SuccessResponse<T> {
    private T data;
    private int status;
    private String message;

    public SuccessResponse() {}

    public SuccessResponse(T data, int status, String message) {
        this.data = data;
        this.status = status;
        this.message = message;
    }

    // Getters and setters

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
