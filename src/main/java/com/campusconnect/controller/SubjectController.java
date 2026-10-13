
package com.campusconnect.controller;

import com.campusconnect.dto.SubjectRequest;
import com.campusconnect.model.Subject;
import com.campusconnect.repository.SubjectRepository;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subjects")
public class SubjectController {

    private final SubjectRepository subjectRepository;

    public SubjectController(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    @GetMapping
    public List<Subject> getAllSubjects() {
        return subjectRepository.findAll();
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('FACULTY', 'ADMIN')")
    public Subject createSubject(@Valid @RequestBody SubjectRequest request) {
        if (subjectRepository.existsByName(request.name())) {
            throw new IllegalArgumentException("Subject already exists");
        }

        Subject subject = new Subject();
        subject.setName(request.name().trim());

        return subjectRepository.save(subject);
    }
}
