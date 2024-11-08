package com.gianlucacerreta.projects.gestione_eventi.repositories;


import com.gianlucacerreta.projects.gestione_eventi.entities.Event;
import com.gianlucacerreta.projects.gestione_eventi.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByOrganizer(User organizer);
}
