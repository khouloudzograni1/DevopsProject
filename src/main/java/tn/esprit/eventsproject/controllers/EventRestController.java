package tn.esprit.eventsproject.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.eventsproject.entities.Event;
import tn.esprit.eventsproject.entities.EventDTO;
import tn.esprit.eventsproject.entities.Logistics;
import tn.esprit.eventsproject.entities.Participant;
import tn.esprit.eventsproject.services.IEventServices;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@RequestMapping("event")
@RestController
public class EventRestController {
    private final IEventServices eventServices;

    @PostMapping("/addPart")
    public Participant addParticipant(@RequestBody Participant participant){
        return eventServices.addParticipant(participant);
    }
    @GetMapping("/getPart")
    public List<Participant> getAllParticipants() {
        return eventServices.getAllParticipants();
    }
    @PostMapping("/addEvent/{id}")
    public Event addEventPart(@RequestBody EventDTO eventDTO, @PathVariable("id") int idPart) {
        // Convert DTO to Entity
        Event event = new Event();
        event.setDescription(eventDTO.getDescription());
        event.setDateDebut(eventDTO.getDateDebut());
        event.setDateFin(eventDTO.getDateFin());
        event.setCout(eventDTO.getCout());

        // Optionally, handle participant and logistics association
        // Convert participantIds to actual Participant entities
        Set<Participant> participants = new HashSet<>();
        for (Integer participantId : eventDTO.getParticipantIds()) {
            Participant participant = new Participant();
            participant.setIdPart(participantId); // Assuming Participant has an ID field
            participants.add(participant);
        }
        event.setParticipants(participants);

        // Convert logisticsIds to actual Logistics entities
        Set<Logistics> logistics = new HashSet<>();
        for (Integer logisticsId : eventDTO.getLogisticsIds()) {
            Logistics logistic = new Logistics();
            logistic.setIdLog(logisticsId); // Assuming Logistics has an ID field
            logistics.add(logistic);
        }
        event.setLogistics(logistics);

        // Call the service method with the converted Event entity
        return eventServices.addAffectEvenParticipant(event, idPart);
    }

    @PostMapping("/addEvent")
    public Event addEvent(@RequestBody Event event){
        return eventServices.addAffectEvenParticipant(event);
    }
    @PutMapping("/addAffectLog/{description}")
    public Logistics addAffectLog(@RequestBody Logistics logistics,@PathVariable("description") String descriptionEvent){
        return eventServices.addAffectLog(logistics,descriptionEvent);
    }
    @GetMapping("/getLogs/{d1}/{d2}")
    public List<Logistics> getLogistiquesDates (@PathVariable("d1") LocalDate dateDebut, @PathVariable("d2") LocalDate dateFin){
        return eventServices.getLogisticsDates(dateDebut,dateFin);
    }
}
