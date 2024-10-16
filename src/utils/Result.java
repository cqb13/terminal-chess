package utils;

public class Result<T> {
    private final T value;
    private final String errorMessage;

    private Result(T value, String errorMessage) {
        this.value = value;
        this.errorMessage = errorMessage;
    }

    public static <T> Result<T> success(T value) {
        return new Result<>(value, null);
    }

    public static <T> Result<T> error(String errorMessage) {
        return new Result<>(null, errorMessage);
    }

    public boolean isSuccess() {
        return errorMessage == null;
    }

    public boolean isError() {
        return errorMessage != null;
    }

    public T getValue() {
        if (isError()) {
            throw new IllegalStateException("Cannot get value from an error result.");
        }
        return value;
    }

    public String getErrorMessage() {
        if (isSuccess()) {
            throw new IllegalStateException("Cannot get error message from a success result.");
        }
        return errorMessage;
    }
}
