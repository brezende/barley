package barley;

public class Error {
	private int code;
	private String message;

	public Error(String message, int code) {
		setCode(code);
		setMessage(message);
	}

	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}
