package tn.esprit.eventsproject;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import tn.esprit.eventsproject.entities.Event;
import tn.esprit.eventsproject.entities.Logistics;
import tn.esprit.eventsproject.entities.Participant;
import tn.esprit.eventsproject.repositories.EventRepository;
import tn.esprit.eventsproject.repositories.LogisticsRepository;
import tn.esprit.eventsproject.repositories.ParticipantRepository;
import tn.esprit.eventsproject.services.EventServicesImpl;

import java.time.LocalDate;
import java.util.*;

public class EventServicesImplTest {

    @Mock
    private ParticipantRepository participantRepository;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private LogisticsRepository logisticsRepository;

    private EventServicesImpl eventServices;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        eventServices = new EventServicesImpl(eventRepository, participantRepository, logisticsRepository);
    }

    @Test
    public void testAddParticipant() {
        Participant participant = new Participant();
        participant.setIdPart(1);  // Assigner un ID fictif

        // Configurer le mock pour sauver le participant
        when(participantRepository.save(participant)).thenReturn(participant);

        // Appeler la méthode à tester
        Participant result = eventServices.addParticipant(participant);

        // Vérifier que la méthode renvoie bien le participant sauvegardé
        assertNotNull(result);
        assertEquals(1, result.getIdPart());

        // Vérifier que save() a bien été appelé
        verify(participantRepository).save(participant);
    }

    @Test
    public void testAddAffectEventParticipantById() {
        Event event = new Event();
        event.setIdEvent(1);  // Assigner un ID fictif
        Participant participant = new Participant();
        participant.setIdPart(1);

        // Simuler la récupération du participant
        when(participantRepository.findById(1)).thenReturn(Optional.of(participant));
        when(eventRepository.save(event)).thenReturn(event);

        // Appeler la méthode à tester
        Event result = eventServices.addAffectEvenParticipant(event, 1);

        // Vérifier que l'événement a été correctement ajouté au participant
        assertNotNull(result);
        assertTrue(participant.getEvents().contains(event));

        // Vérifier que save() a été appelé pour l'événement
        verify(eventRepository).save(event);
    }

    @Test
    public void testAddAffectEventParticipantByEvent() {
        Event event = new Event();
        event.setIdEvent(1);  // Assigner un ID fictif
        Participant participant1 = new Participant();
        participant1.setIdPart(1);
        Participant participant2 = new Participant();
        participant2.setIdPart(2);

        Set<Participant> participants = new HashSet<>(Arrays.asList(participant1, participant2));
        event.setParticipants(participants);

        // Simuler la récupération des participants
        when(participantRepository.findById(1)).thenReturn(Optional.of(participant1));
        when(participantRepository.findById(2)).thenReturn(Optional.of(participant2));
        when(eventRepository.save(event)).thenReturn(event);

        // Appeler la méthode à tester
        Event result = eventServices.addAffectEvenParticipant(event);

        // Vérifier que l'événement a été correctement ajouté à chaque participant
        assertNotNull(result);
        assertTrue(participant1.getEvents().contains(event));
        assertTrue(participant2.getEvents().contains(event));

        // Vérifier que save() a été appelé pour l'événement
        verify(eventRepository).save(event);
    }

    @Test
    public void testAddAffectLog() {
        Event event = new Event();
        event.setIdEvent(1);
        Logistics logistics = new Logistics();
        logistics.setIdLog(1);
        String descriptionEvent = "Test Event";

        when(eventRepository.findByDescription(descriptionEvent)).thenReturn(event);
        when(logisticsRepository.save(logistics)).thenReturn(logistics);
        when(eventRepository.save(event)).thenReturn(event);

        // Appeler la méthode à tester
        Logistics result = eventServices.addAffectLog(logistics, descriptionEvent);

        // Vérifier que la logistique a été ajoutée à l'événement
        assertNotNull(result);
        assertTrue(event.getLogistics().contains(logistics));

        // Vérifier que save() a bien été appelé pour la logistique et l'événement
        verify(logisticsRepository).save(logistics);
        verify(eventRepository).save(event);
    }

    @Test
    public void testGetLogisticsDates() {
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 12, 31);

        Event event = new Event();
        Logistics logistics = new Logistics();
        logistics.setReserve(true);
        event.setLogistics(new HashSet<>(Arrays.asList(logistics)));

        List<Event> events = Arrays.asList(event);
        when(eventRepository.findByDateDebutBetween(startDate, endDate)).thenReturn(events);

        List<Logistics> result = eventServices.getLogisticsDates(startDate, endDate);

        // Vérifier que la logistique est bien dans la liste
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertTrue(result.contains(logistics));

        // Vérifier que findByDateDebutBetween() a bien été appelée
        verify(eventRepository).findByDateDebutBetween(startDate, endDate);
    }

    @Test
    public void testAddAffectEventParticipant_ThrowsExceptionWhenParticipantNotFound() {
        Event event = new Event();
        event.setIdEvent(1);
        int nonExistentParticipantId = 999;

        when(participantRepository.findById(nonExistentParticipantId)).thenReturn(Optional.empty());

        // Vérifier que la méthode lance une exception lorsque le participant n'est pas trouvé
        assertThrows(IllegalArgumentException.class, () -> {
            eventServices.addAffectEvenParticipant(event, nonExistentParticipantId);
        });
    }

    @Test
    public void testAddAffectLog_EventNotFound() {
        Logistics logistics = new Logistics();
        logistics.setIdLog(1);
        String nonExistentEventDescription = "NonExistentEvent";

        when(eventRepository.findByDescription(nonExistentEventDescription)).thenReturn(null);

        // Vérifier que la méthode lance une exception si l'événement n'est pas trouvé
        assertThrows(NullPointerException.class, () -> {
            eventServices.addAffectLog(logistics, nonExistentEventDescription);
        });
    }

    @Test
    public void testGetLogisticsDates_EmptyLogistics() {
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 12, 31);

        Event event = new Event();
        event.setLogistics(new HashSet<>());  // Pas de logistique
        List<Event> events = Arrays.asList(event);

        when(eventRepository.findByDateDebutBetween(startDate, endDate)).thenReturn(events);

        List<Logistics> result = eventServices.getLogisticsDates(startDate, endDate);

        // Vérifier que le résultat est une liste vide
        assertNotNull(result);
        assertTrue(result.isEmpty());

        // Vérifier que findByDateDebutBetween() a bien été appelée
        verify(eventRepository).findByDateDebutBetween(startDate, endDate);
    }

    @Test
    public void testGetAllParticipants() {
        Participant participant1 = new Participant();
        participant1.setIdPart(1);
        Participant participant2 = new Participant();
        participant2.setIdPart(2);

        List<Participant> participants = Arrays.asList(participant1, participant2);
        when(participantRepository.findAll()).thenReturn(participants);

        List<Participant> result = eventServices.getAllParticipants();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(participant1));
        assertTrue(result.contains(participant2));

        verify(participantRepository).findAll();
    }
}
