package gabriel.moraes.school.exception;

public class StudentAlreadyAssignedException extends RuntimeException{
    public StudentAlreadyAssignedException(String message){
        super(message);
    }
}
