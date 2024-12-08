package tn.esprit.eventsproject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.eventsproject.entities.Event;
import tn.esprit.eventsproject.entities.Logistics;
import tn.esprit.eventsproject.entities.Participant;
import tn.esprit.eventsproject.repositories.EventRepository;
import tn.esprit.eventsproject.repositories.LogisticsRepository;
import tn.esprit.eventsproject.repositories.ParticipantRepository;
import tn.esprit.eventsproject.services.EventServicesImpl;

import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EventServicesImplTest {

    private static final Logger logger = LogManager.getLogger(EventServicesImplTest.class);

    @InjectMocks
    private EventServicesImpl eventServices;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private ParticipantRepository participantRepository;

    @Mock
    private LogisticsRepository logisticsRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        logger.info("Setup completed for EventServicesImplTest");
    }

    @Test
    void testAddParticipant() {
        logger.info("Starting testAddParticipant...");

        Participant participant = new Participant();
        participant.setIdPart(1);

        try {
            when(participantRepository.save(any(Participant.class))).thenReturn(participant);

            Participant result = eventServices.addParticipant(participant);

            assertNotNull(result);
            assertEquals(1, result.getIdPart());
            verify(participantRepository, times(1)).save(participant);

            logger.info("testAddParticipant completed successfully.");
        } catch (Exception e) {
            logger.error("Error during testAddParticipant: {}", e.getMessage());
            fail("Exception occurred during testAddParticipant.");
        }
    }

    @Test
    void testAddAffectEvenParticipant() {
        logger.info("Starting testAddAffectEvenParticipant...");

        Participant participant = new Participant();
        participant.setIdPart(1);
        participant.setEvents(new HashSet<>());

        Event event = new Event();
        event.setIdEvent(1);
        event.setDescription("Test Event");

        try {
            when(participantRepository.findById(1)).thenReturn(Optional.of(participant));
            when(eventRepository.save(event)).thenReturn(event);

            Event result = eventServices.addAffectEvenParticipant(event, 1);

            assertNotNull(result);
            assertTrue(participant.getEvents().contains(event));
            verify(participantRepository, times(1)).findById(1);
            verify(eventRepository, times(1)).save(event);

            logger.info("testAddAffectEvenParticipant completed successfully.");
        } catch (Exception e) {
            logger.error("Error during testAddAffectEvenParticipant: {}", e.getMessage());
            fail("Exception occurred during testAddAffectEvenParticipant.");
        }
    }

    @Test
    void testAddAffectLog() {
        logger.info("Starting testAddAffectLog...");

        Logistics logistics = new Logistics();
        logistics.setIdLog(1);
        logistics.setDescription("Logistics Test");

        Event event = new Event();
        event.setIdEvent(1);
        event.setDescription("Test Event");
        event.setLogistics(new HashSet<>());

        try {
            when(eventRepository.findByDescription("Test Event")).thenReturn(event);
            when(logisticsRepository.save(logistics)).thenReturn(logistics);

            Logistics result = eventServices.addAffectLog(logistics, "Test Event");

            assertNotNull(result);
            assertTrue(event.getLogistics().contains(logistics));
            verify(eventRepository, times(1)).findByDescription("Test Event");
            verify(logisticsRepository, times(1)).save(logistics);

            logger.info("testAddAffectLog completed successfully.");
        } catch (Exception e) {
            logger.error("Error during testAddAffectLog: {}", e.getMessage());
            fail("Exception occurred during testAddAffectLog.");
        }
    }

    @Test
    void testParticipantNotFound() {
        logger.info("Starting testParticipantNotFound...");

        when(participantRepository.findById(99)).thenReturn(Optional.empty());

        try {
            eventServices.addAffectEvenParticipant(new Event(), 99);
            fail("Expected exception not thrown");
        } catch (Exception e) {
            logger.warn("Participant with ID 99 not found.");
            assertTrue(e.getMessage().contains("Participant not found"));
        }
    }
}
