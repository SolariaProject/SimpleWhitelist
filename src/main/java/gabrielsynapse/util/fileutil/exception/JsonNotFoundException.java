package gabrielsynapse.util.fileutil.exception;

public class JsonNotFoundException extends RuntimeException{
    public JsonNotFoundException(String message){
        super(message);
    }
    public JsonNotFoundException(String message,Throwable cause){
        super(message,cause);
    }
}