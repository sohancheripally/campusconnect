
package com.campusconnect.repository;

import com.campusconnect.model.Attendance;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.*;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    Optional<Attendance> findByStudentIdAndSubjectIdAndClassDate(
            Long studentId, Long subjectId, LocalDate date);

    long countByPresentTrue();

    @Query("select a.subject.name, count(a), " +
           "sum(case when a.present = true then 1 else 0 end) " +
           "from Attendance a where a.student.id = :sid " +
           "group by a.subject.name")
    List<Object[]> summaryForStudent(@Param("sid") Long sid);
}
