package gabriel.moraes.school.exception;

public class NoRegisteredStudentsException extends RuntimeException{
    public NoRegisteredStudentsException(String message){
        super(message);
    }
}
