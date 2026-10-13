
package com.campusconnect.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "event_registrations",
       uniqueConstraints = @UniqueConstraint(
           columnNames = {"event_id", "student_id"}))
@Getter
@Setter
@NoArgsConstructor
public class EventRegistration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "event_id")
    private Event event;

    @ManyToOne(optional = false)
    @JoinColumn(name = "student_id")
    private User student;
}
