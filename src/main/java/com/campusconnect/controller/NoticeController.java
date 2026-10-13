
package com.campusconnect.controller;

import com.campusconnect.model.Notice;
import com.campusconnect.model.User;
import com.campusconnect.repository.NoticeRepository;
import com.campusconnect.repository.UserRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notices")
public class NoticeController {

    private final NoticeRepository noticeRepository;
    private final UserRepository userRepository;

    public NoticeController(
            NoticeRepository noticeRepository,
            UserRepository userRepository) {
        this.noticeRepository = noticeRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<Notice> getAllNotices() {
        return noticeRepository.findAllByOrderByCreatedAtDesc();
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('FACULTY', 'ADMIN')")
    public Notice createNotice(
            @RequestBody Notice request,
            Authentication authentication) {

        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow();

        Notice notice = new Notice();
        notice.setTitle(request.getTitle());
        notice.setDescription(request.getDescription());
        notice.setCreatedBy(user.getName());

        return noticeRepository.save(notice);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteNotice(@PathVariable Long id) {
        noticeRepository.deleteById(id);
    }
}
