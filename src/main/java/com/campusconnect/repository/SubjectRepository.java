
package com.campusconnect.repository;

import com.campusconnect.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubjectRepository extends JpaRepository<Subject, Long> {
    boolean existsByName(String name);
}
