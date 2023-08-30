package gabriel.moraes.school.validations;

import gabriel.moraes.school.domain.classroom.ClassRoom;
import gabriel.moraes.school.domain.classroom.ClassStatus;
import gabriel.moraes.school.domain.coordinator.Coordinator;
import gabriel.moraes.school.domain.instructor.Instructor;
import gabriel.moraes.school.domain.scrummaster.ScrumMaster;
import gabriel.moraes.school.domain.student.Student;
import gabriel.moraes.school.exception.InsufficientStudentsException;
import gabriel.moraes.school.exception.InvalidClassStatusException;
import gabriel.moraes.school.exception.MaximumStudentsException;
import gabriel.moraes.school.exception.StudentAlreadyAssignedException;

import java.util.List;

import static gabriel.moraes.school.config.AppConfig.*;

public interface ValidationsClassRoom {

    static void validationsMaxStudents(List<Student> students) {
        if (students.size()  >= MAX_STUDENTS) {
            throw new MaximumStudentsException("A class can have a maximum of 30 students");
        }
    }

    static boolean validateStartClass(ClassRoom classRoom) {
        if (classRoom.getStudents().size() < MIN_STUDENTS) {
            throw new InsufficientStudentsException("A minimum of 15 students is required to start a class.");
        }
        if (classRoom.getStatus() != ClassStatus.WAITING) {
            throw new InvalidClassStatusException("To start a class you need the status in WAITING");
        }
        return true;
    }

    static boolean validateFinishClass(ClassRoom classRoom) {
        if (classRoom.getStatus() != ClassStatus.STARTED) {
            throw new InvalidClassStatusException("Classroom needs to be in STARTED status to be finished.");
        }
        return true;
    }

    static void validateClassRoomStatus(ClassStatus status) {
        if (status != ClassStatus.WAITING) {
            throw new InvalidClassStatusException("It is only possible to add new students when the class room status is in WAITING");
        }

    }
    static void validateAndAssignStudentsToClass(List<Student> students, ClassRoom classRoom) {
        for (Student student : students) {
            if (student.getClassRoom() != null) {
                throw new StudentAlreadyAssignedException("Student " + student.getFirstName() + "[ID: "+ student.getId()+"]"  + " is already assigned to a class.");
            }
            student.setClassRoom(classRoom);
        }
    }
    static void validateMaxInstructor(List<Instructor> instructors) {
        if (instructors.size() > MAX_INSTRUCTORS) {
            throw new IllegalArgumentException("A class can have a maximum of " + MAX_INSTRUCTORS +" instructor");
        }
    }

    static void validateMaxCoordinator(List<Coordinator> coordinators) {
        if (coordinators.size() > MAX_COORDINATOR) {
            throw new IllegalArgumentException("A class can have a maximum of " + MAX_COORDINATOR +" coordinator");
        }
    }

    static void validateMaxScrumMaster(List<ScrumMaster> scrumMasters) {
        if (scrumMasters.size() > MAX_SCRUM_MASTER) {
            throw new IllegalArgumentException("A class can have a maximum of " + MAX_SCRUM_MASTER +" scrum master");
        }
    }
}
