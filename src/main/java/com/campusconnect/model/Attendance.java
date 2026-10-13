
package com.campusconnect.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "attendance",
       uniqueConstraints = @UniqueConstraint(
           columnNames = {"student_id", "subject_id", "class_date"}))
@Getter
@Setter
@NoArgsConstructor
public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "student_id")
    private User student;

    @ManyToOne(optional = false)
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @Column(name = "class_date")
    private LocalDate classDate;

    private boolean present;
}
