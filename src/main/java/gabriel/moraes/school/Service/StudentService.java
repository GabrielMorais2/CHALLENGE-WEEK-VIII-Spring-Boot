package gabriel.moraes.school.Service;

import gabriel.moraes.school.Model.Student;
import gabriel.moraes.school.Model.employee.DtoRequest.StudentDtoRequest;
import gabriel.moraes.school.Model.employee.DtoResponse.StudentDtoResponse;
import gabriel.moraes.school.exception.ObjectNotFoundException;
import gabriel.moraes.school.repository.StudentRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final ModelMapper mapper;

    public StudentService(StudentRepository studentRepository, ModelMapper mapper) {
        this.studentRepository = studentRepository;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public StudentDtoResponse getStudentById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Student not found with id: " + id));

        return mapper.map(student, StudentDtoResponse.class);
    }

    @Transactional(readOnly = true)
    public List<StudentDtoResponse> getAllStudents() {
        List<Student> students = studentRepository.findAll();
        return students.stream()
                .map(student -> mapper.map(student, StudentDtoResponse.class))
                .collect(Collectors.toList());
    }

    @Transactional
    public StudentDtoResponse save(StudentDtoRequest studentDtoRequest) {
        Student student = mapper.map(studentDtoRequest, Student.class);
        Student studentSaved = studentRepository.save(student);
        return mapper.map(studentSaved, StudentDtoResponse.class);
    }

    public void deleteStudentById(Long id) {
        Student student = studentRepository.findById(id)
                        .orElseThrow(() -> new ObjectNotFoundException("Student not found"));

        studentRepository.delete(student);
    }
}
