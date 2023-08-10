package gabriel.moraes.school.exception;

public class NoRegisteredStudents extends RuntimeException{
    public NoRegisteredStudents(String message){
        super(message);
    }
}
