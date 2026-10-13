
package com.campusconnect.controller;

import com.campusconnect.model.Event;
import com.campusconnect.model.EventRegistration;
import com.campusconnect.model.User;
import com.campusconnect.repository.EventRegistrationRepository;
import com.campusconnect.repository.EventRepository;
import com.campusconnect.repository.UserRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventRepository eventRepository;
    private final EventRegistrationRepository registrationRepository;
    private final UserRepository userRepository;

    public EventController(
            EventRepository eventRepository,
            EventRegistrationRepository registrationRepository,
            UserRepository userRepository) {
        this.eventRepository = eventRepository;
        this.registrationRepository = registrationRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<Event> getAllEvents() {
        return eventRepository.findAllByOrderByEventDateAsc();
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('FACULTY', 'ADMIN')")
    public Event createEvent(@RequestBody Event request,
                             Authentication authentication) {
        User creator = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Event event = new Event();
        event.setName(request.getName());
        event.setDescription(request.getDescription());
        event.setCategory(request.getCategory());
        event.setEventDate(request.getEventDate());
        event.setCreatedBy(creator.getName());

        return eventRepository.save(event);
    }

    @PostMapping("/{eventId}/register")
    @PreAuthorize("hasRole('STUDENT')")
    public EventRegistration registerForEvent(
            @PathVariable Long eventId,
            Authentication authentication) {

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));

        User student = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (registrationRepository.existsByEventIdAndStudentId(
                eventId, student.getId())) {
            throw new IllegalArgumentException(
                    "You are already registered for this event");
        }

        EventRegistration registration = new EventRegistration();
        registration.setEvent(event);
        registration.setStudent(student);

        return registrationRepository.save(registration);
    }
}
