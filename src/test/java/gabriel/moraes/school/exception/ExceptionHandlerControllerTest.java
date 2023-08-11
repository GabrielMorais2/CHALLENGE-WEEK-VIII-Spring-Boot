package gabriel.moraes.school.exception;

import gabriel.moraes.school.exception.validation.ValidationError;
import gabriel.moraes.school.exception.validation.ValidationErrorResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ExceptionHandlerControllerTest {

    @InjectMocks
    private ExceptionHandlerController exceptionHandlerController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void globalExceptionHandler() {
    }

    @Test
    void objectNotFoundException() {
        ResponseEntity<ErrorResponse> response = exceptionHandlerController
                .objectNotFoundException(
                        new ObjectNotFoundException("Object Not Found")
                );

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(ErrorResponse.class, response.getBody().getClass());
        assertEquals("Object Not Found", response.getBody().getMessage());
    }


    @Test
    void invalidClassStatusException() {
        ResponseEntity<ErrorResponse> response = exceptionHandlerController
                .invalidClassStatusException(
                        new InvalidClassStatusException("To start a class you need the status in WAITING")
                );

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(ErrorResponse.class, response.getBody().getClass());
        assertEquals("To start a class you need the status in WAITING", response.getBody().getMessage());
    }

    @Test
    void insufficientStudentsException() {
        ResponseEntity<ErrorResponse> response = exceptionHandlerController
                .insufficientStudentsException(
                        new InsufficientStudentsException("A minimum of 15 students is required to start a class.")
                );

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(ErrorResponse.class, response.getBody().getClass());
        assertEquals("A minimum of 15 students is required to start a class.", response.getBody().getMessage());
    }

    @Test
    void maximumStudentsException() {
        ResponseEntity<ErrorResponse> response = exceptionHandlerController
                .maximumStudentsException(
                        new MaximumStudentsException("A class can have a maximum of 30 students")
                );

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(ErrorResponse.class, response.getBody().getClass());
        assertEquals("A class can have a maximum of 30 students", response.getBody().getMessage());
    }

    @Test
    void studentAlreadyAssignedException() {
        ResponseEntity<ErrorResponse> response = exceptionHandlerController
                .studentAlreadyAssignedException(
                        new StudentAlreadyAssignedException("the student is already assigned to a class.")
                );

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(ErrorResponse.class, response.getBody().getClass());
        assertEquals("the student is already assigned to a class.", response.getBody().getMessage());
    }

    @Test
    void noRegisteredStudents() {
        ResponseEntity<ErrorResponse> response = exceptionHandlerController
                .noRegisteredStudentsException(
                        new NoRegisteredStudentsException("There are no registered students.")
                );

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(ErrorResponse.class, response.getBody().getClass());
        assertEquals("There are no registered students.", response.getBody().getMessage());
    }

    @Test
    void minimumInstructorsException() {
        ResponseEntity<ErrorResponse> response = exceptionHandlerController
                .minimumInstructorsException(
                        new MinimumInstructorsException("Requires a minimum of 3 instructors")
                );

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(ErrorResponse.class, response.getBody().getClass());
        assertEquals("Requires a minimum of 3 instructors", response.getBody().getMessage());
    }

    @Test
    void testHandleValidationException() {
        FieldError fieldError = new FieldError("objectName", "fieldName", "Field is required");
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));

        MethodArgumentNotValidException methodArgumentNotValidException = new MethodArgumentNotValidException((MethodParameter) null, bindingResult);
        
        ResponseEntity<ValidationErrorResponse> responseEntity = exceptionHandlerController
                .methodArgumentNotValidException(methodArgumentNotValidException
                );

        assertNotNull(responseEntity);
        assertNotNull(responseEntity.getBody());
        assertNotNull(responseEntity.getBody().getErrors());

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Validation failed: Invalid argument(s) in the request", responseEntity.getBody().getMessage());


        List<ValidationError> validationErrors = responseEntity.getBody().getErrors();
        ValidationError error = validationErrors.get(0);
        assertEquals("Field is required", error.getMessage());
        assertEquals("fieldName", error.getField());
    }
}