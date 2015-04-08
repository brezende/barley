package barley.validation;

import java.util.ArrayList;
import java.util.List;

import barley.Error;

public class ValidationError extends Error {

	public static class ErrorData {
		private String source;
		private String field;
		private String code;

		public String getSource() {
			return source;
		}
		public void setSource(String source) {
			this.source = source;
		}
		public String getField() {
			return field;
		}
		public void setField(String field) {
			this.field = field;
		}
		public String getCode() {
			return code;
		}
		public void setCode(String code) {
			this.code = code;
		}
	}
	
	private List<ErrorData> errors;
	
	public ValidationError() {
		super("VALIDATION_FAILED", 400);
		this.errors = new ArrayList<>(); 
	}

	public List<ErrorData> getErrors() {
		return this.errors;
	}

	public void addError(String source, String field, String code) {
		ErrorData error = new ErrorData();
		error.setCode(code);
		error.setField(field);
		error.setSource(source);
		addError(error);
	}

	public void addError(ErrorData error) {
		this.errors.add(error);
	}
}
