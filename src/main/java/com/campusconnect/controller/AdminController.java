
package com.campusconnect.controller;

import com.campusconnect.model.Role;
import com.campusconnect.repository.AttendanceRepository;
import com.campusconnect.repository.EventRepository;
import com.campusconnect.repository.NoticeRepository;
import com.campusconnect.repository.PostRepository;
import com.campusconnect.repository.SubjectRepository;
import com.campusconnect.repository.UserRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final UserRepository userRepository;
    private final SubjectRepository subjectRepository;
    private final NoticeRepository noticeRepository;
    private final EventRepository eventRepository;
    private final PostRepository postRepository;
    private final AttendanceRepository attendanceRepository;

    public AdminController(
            UserRepository userRepository,
            SubjectRepository subjectRepository,
            NoticeRepository noticeRepository,
            EventRepository eventRepository,
            PostRepository postRepository,
            AttendanceRepository attendanceRepository) {
        this.userRepository = userRepository;
        this.subjectRepository = subjectRepository;
        this.noticeRepository = noticeRepository;
        this.eventRepository = eventRepository;
        this.postRepository = postRepository;
        this.attendanceRepository = attendanceRepository;
    }

    @GetMapping("/dashboard")
    public Map<String, Long> getDashboardStats() {
        Map<String, Long> stats = new LinkedHashMap<>();

        stats.put("totalUsers", userRepository.count());
        stats.put("totalStudents", userRepository.countByRole(Role.STUDENT));
        stats.put("totalFaculty", userRepository.countByRole(Role.FACULTY));
        stats.put("totalSubjects", subjectRepository.count());
        stats.put("totalNotices", noticeRepository.count());
        stats.put("totalEvents", eventRepository.count());
        stats.put("totalDiscussionPosts", postRepository.count());
        stats.put("totalPresentAttendanceRecords",
                attendanceRepository.countByPresentTrue());

        return stats;
    }
}
