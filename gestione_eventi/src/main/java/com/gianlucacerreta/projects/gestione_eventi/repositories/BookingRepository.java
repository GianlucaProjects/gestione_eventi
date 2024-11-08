package com.gianlucacerreta.projects.gestione_eventi.repositories;

import com.gianlucacerreta.projects.gestione_eventi.entities.Booking;
import com.gianlucacerreta.projects.gestione_eventi.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUser(User user);
}

