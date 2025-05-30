package student_portal.GTU.Dto;

public class ErrorResponse {
    private Object data;
    private int status;
    private String message;

    public ErrorResponse(int status, String message) {
        this.data = null;
        this.status = status;
        this.message = message;
    }

    // Add getters and setters (or use Lombok @Getter @Setter)

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
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
