package tn.esprit.eventsproject;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tn.esprit.eventsproject.controllers.EventRestController;
import tn.esprit.eventsproject.entities.Event;
import tn.esprit.eventsproject.entities.Logistics;
import tn.esprit.eventsproject.entities.Participant;
import tn.esprit.eventsproject.services.IEventServices;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Collections;

class EventRestControllerTest {

    private MockMvc mockMvc;

    @Mock
    private IEventServices eventServices;

    @InjectMocks
    private EventRestController eventRestController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(eventRestController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testAddParticipant() throws Exception {
        // Création d'un participant de test
        Participant participant = new Participant();
        participant.setIdPart(1);

        // Mock de la méthode du service
        when(eventServices.addParticipant(any(Participant.class))).thenReturn(participant);

        // Test de l'appel HTTP POST
        mockMvc.perform(post("/event/addPart")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(participant)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idPart").value(1));

        verify(eventServices, times(1)).addParticipant(any(Participant.class));
    }

    @Test
    void testGetAllParticipants() throws Exception {
        // Création de la liste de participants
        Participant participant = new Participant();
        participant.setIdPart(1);
        when(eventServices.getAllParticipants()).thenReturn(Collections.singletonList(participant));

        // Test de l'appel HTTP GET
        mockMvc.perform(get("/event/getPart"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idPart").value(1));

        verify(eventServices, times(1)).getAllParticipants();
    }

    @Test
    void testAddEventPart() throws Exception {
        // Création d'un événement de test
        Event event = new Event();
        event.setIdEvent(1);

        Participant participant = new Participant();
        participant.setIdPart(1);

        // Mock de la méthode du service
        when(eventServices.addAffectEvenParticipant(any(Event.class), eq(1))).thenReturn(event);

        // Test de l'appel HTTP POST
        mockMvc.perform(post("/event/addEvent/{id}", 1)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(event)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idEvent").value(1));

        verify(eventServices, times(1)).addAffectEvenParticipant(any(Event.class), eq(1));
    }

    @Test
    void testAddEvent() throws Exception {
        // Création d'un événement de test
        Event event = new Event();
        event.setIdEvent(1);

        // Mock de la méthode du service
        when(eventServices.addAffectEvenParticipant(any(Event.class))).thenReturn(event);

        // Test de l'appel HTTP POST
        mockMvc.perform(post("/event/addEvent")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(event)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idEvent").value(1));

        verify(eventServices, times(1)).addAffectEvenParticipant(any(Event.class));
    }

    @Test
    void testAddAffectLog() throws Exception {
        // Création d'un objet Logistics
        Logistics logistics = new Logistics();
        logistics.setIdLog(1);

        // Mock de la méthode du service
        when(eventServices.addAffectLog(any(Logistics.class), eq("Test Event"))).thenReturn(logistics);

        // Test de l'appel HTTP PUT
        mockMvc.perform(put("/event/addAffectLog/{description}", "Test Event")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(logistics)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idLog").value(1));

        verify(eventServices, times(1)).addAffectLog(any(Logistics.class), eq("Test Event"));
    }


}
