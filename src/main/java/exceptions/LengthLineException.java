package exceptions;

public class LengthLineException extends RuntimeException{
    public LengthLineException() {
        super();
    }

    public LengthLineException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }

    @Override
    public void printStackTrace() {
        super.printStackTrace();
    }
}
