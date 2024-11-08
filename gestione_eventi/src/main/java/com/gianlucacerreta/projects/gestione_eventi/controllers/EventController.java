package com.gianlucacerreta.projects.gestione_eventi.controllers;


import com.gianlucacerreta.projects.gestione_eventi.entities.Event;
import com.gianlucacerreta.projects.gestione_eventi.entities.User;
import com.gianlucacerreta.projects.gestione_eventi.repositories.EventRepository;
import com.gianlucacerreta.projects.gestione_eventi.repositories.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/events")
public class EventController {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<Event>> getAllEvents() {
        List<Event> events = eventRepository.findAll();
        return ResponseEntity.ok(events);
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ORGANIZER')")
    public ResponseEntity<?> createEvent(@Valid @RequestBody Event event, Principal principal) {
        User organizer = userRepository.findByUsername(principal.getName());
        event.setOrganizer(organizer);
        Event savedEvent = eventRepository.save(event);
        return ResponseEntity.ok(savedEvent);
    }

    @PutMapping("/edit/{id}")
    @PreAuthorize("hasRole('ORGANIZER')")
    public ResponseEntity<?> editEvent(@PathVariable Long id, @Valid @RequestBody Event updatedEvent, Principal principal) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Evento non trovato!"));
        if (!event.getOrganizer().getUsername().equals(principal.getName())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Non sei autorizzato a modificare questo evento!");
        }
        event.setTitle(updatedEvent.getTitle());
        event.setDescription(updatedEvent.getDescription());
        event.setDate(updatedEvent.getDate());
        event.setLocation(updatedEvent.getLocation());
        event.setAvailableSeats(updatedEvent.getAvailableSeats());
        eventRepository.save(event);
        return ResponseEntity.ok(event);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ORGANIZER')")
    public ResponseEntity<?> deleteEvent(@PathVariable Long id, Principal principal) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Evento non trovato!"));
        if (!event.getOrganizer().getUsername().equals(principal.getName())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Non sei autorizzato a eliminare questo evento!");
        }
        eventRepository.delete(event);
        return ResponseEntity.ok("Evento eliminato con successo!");
    }
}

