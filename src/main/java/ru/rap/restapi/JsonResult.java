package ru.rap.restapi;

/**
 * Created in project RiddlesAndPuzzles in 24.12.2016
 */
public class JsonResult {
	public enum ErrorLevel {
		info,
		warn,
		error
	};

	Boolean ok;
	String error;
	ErrorLevel level;
	Object result;

	public JsonResult(Boolean ok, String error, ErrorLevel level, Object result) {
		this.ok = ok;
		this.error = error;
		this.level = level;
		this.result = result;
	}

	public static JsonResult genOkResult(Object... args) {
		return new JsonResult(true, null, null,
				args.length == 0
						? null
						: args.length == 1
						? args[0]
						: args);
	}

	public static JsonResult genError(String error) {
		return new JsonResult(false, error, ErrorLevel.error, null);
	}

	public static JsonResult genError(String error, ErrorLevel level) {
		return new JsonResult(false, error, level, null);
	}
}
