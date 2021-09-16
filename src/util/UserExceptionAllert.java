package util;

public class UserExceptionAllert extends Exception{
    private final String message;
    private final Exception ex;

    public UserExceptionAllert(String message, Exception ex) {
        this.message = message;
        this.ex = ex;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public Exception getEx() {
        return ex;
    }
    
    
    
}
