
package com.campusconnect.controller;

import com.campusconnect.model.Attendance;
import com.campusconnect.model.Subject;
import com.campusconnect.model.User;
import com.campusconnect.repository.AttendanceRepository;
import com.campusconnect.repository.SubjectRepository;
import com.campusconnect.repository.UserRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    private final AttendanceRepository attendanceRepository;
    private final UserRepository userRepository;
    private final SubjectRepository subjectRepository;

    public AttendanceController(
            AttendanceRepository attendanceRepository,
            UserRepository userRepository,
            SubjectRepository subjectRepository) {
        this.attendanceRepository = attendanceRepository;
        this.userRepository = userRepository;
        this.subjectRepository = subjectRepository;
    }

    @PostMapping("/mark")
    @PreAuthorize("hasAnyRole('FACULTY', 'ADMIN')")
    public Attendance markAttendance(@RequestParam Long studentId,
                                     @RequestParam Long subjectId,
                                     @RequestParam boolean present,
                                     @RequestParam(required = false) LocalDate date) {

        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));

        if (student.getRole() != com.campusconnect.model.Role.STUDENT) {
            throw new IllegalArgumentException("Selected user is not a student");
        }

        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new IllegalArgumentException("Subject not found"));

        LocalDate classDate = date == null ? LocalDate.now() : date;

        Attendance attendance = attendanceRepository
                .findByStudentIdAndSubjectIdAndClassDate(
                        studentId, subjectId, classDate)
                .orElseGet(Attendance::new);

        attendance.setStudent(student);
        attendance.setSubject(subject);
        attendance.setClassDate(classDate);
        attendance.setPresent(present);

        return attendanceRepository.save(attendance);
    }

    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasAnyRole('STUDENT', 'FACULTY', 'ADMIN')")
    public List<Attendance> getStudentAttendance(
            @PathVariable Long studentId,
            Authentication authentication) {

        User requester = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (requester.getRole() == com.campusconnect.model.Role.STUDENT
                && !requester.getId().equals(studentId)) {
            throw new IllegalArgumentException(
                    "Students can only view their own attendance");
        }

        return attendanceRepository.findAll().stream()
                .filter(a -> a.getStudent().getId().equals(studentId))
                .toList();
    }
}
