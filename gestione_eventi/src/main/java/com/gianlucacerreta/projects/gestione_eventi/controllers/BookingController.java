package com.gianlucacerreta.projects.gestione_eventi.controllers;


import com.gianlucacerreta.projects.gestione_eventi.entities.Booking;
import com.gianlucacerreta.projects.gestione_eventi.entities.Event;
import com.gianlucacerreta.projects.gestione_eventi.entities.User;
import com.gianlucacerreta.projects.gestione_eventi.repositories.BookingRepository;
import com.gianlucacerreta.projects.gestione_eventi.repositories.EventRepository;
import com.gianlucacerreta.projects.gestione_eventi.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/{eventId}")
    @PreAuthorize("hasAnyRole('USER', 'ORGANIZER')")
    public ResponseEntity<?> bookEvent(@PathVariable Long eventId, @RequestParam int seats, Principal principal) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Evento non trovato!"));
        if (event.getAvailableSeats() < seats) {
            return ResponseEntity.badRequest().body("Posti disponibili insufficienti!");
        }
        User user = userRepository.findByUsername(principal.getName());
        Booking booking = new Booking();
        booking.setEvent(event);
        booking.setUser(user);
        booking.setSeatsBooked(seats);
        bookingRepository.save(booking);
        event.setAvailableSeats(event.getAvailableSeats() - seats);
        eventRepository.save(event);
        return ResponseEntity.ok("Prenotazione effettuata con successo!");
    }
}

