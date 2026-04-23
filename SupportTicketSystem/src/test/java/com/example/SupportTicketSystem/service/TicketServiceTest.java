package com.example.SupportTicketSystem.service;

import com.example.SupportTicketSystem.entity.Role;
import com.example.SupportTicketSystem.entity.Status;
import com.example.SupportTicketSystem.entity.Ticket;
import com.example.SupportTicketSystem.entity.User;
import com.example.SupportTicketSystem.repository.TicketRepository;
import com.example.SupportTicketSystem.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TicketService ticketService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setPassword("encoded");
        testUser.setRole(Role.ROLE_USER);

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("testuser", null, List.of())
        );
    }

    @Test
    void createTicket_shouldSetStatusOpenAndAssignUser() {
        Ticket ticket = new Ticket();
        ticket.setTitle("Test ticket");
        ticket.setDescription("Some issue");

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(ticketRepository.save(any(Ticket.class))).thenAnswer(inv -> inv.getArgument(0));

        Ticket result = ticketService.create(ticket);

        assertEquals(Status.OPEN, result.getStatus());
        assertEquals(testUser, result.getUser());
        assertNotNull(result.getCreatedAt());
        verify(ticketRepository, times(1)).save(ticket);
    }

    @Test
    void updateStatus_shouldChangeTicketStatus() {
        Ticket ticket = new Ticket();
        ticket.setId(1L);
        ticket.setStatus(Status.OPEN);

        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));
        when(ticketRepository.save(any(Ticket.class))).thenAnswer(inv -> inv.getArgument(0));

        Ticket result = ticketService.updateStatus(1L, Status.IN_PROGRESS);

        assertEquals(Status.IN_PROGRESS, result.getStatus());
    }

    @Test
    void getAll_shouldReturnAllTickets() {
        Ticket t1 = new Ticket();
        t1.setTitle("Issue 1");
        Ticket t2 = new Ticket();
        t2.setTitle("Issue 2");

        when(ticketRepository.findAll()).thenReturn(List.of(t1, t2));

        List<Ticket> result = ticketService.getAll();

        assertEquals(2, result.size());
    }

    @Test
    void search_shouldReturnMatchingTickets() {
        Ticket ticket = new Ticket();
        ticket.setTitle("Login problem");

        when(ticketRepository.findByTitleContainingIgnoreCase("login"))
                .thenReturn(List.of(ticket));

        List<Ticket> result = ticketService.search("login");

        assertEquals(1, result.size());
        assertEquals("Login problem", result.get(0).getTitle());
    }

    @Test
    void delete_shouldCallRepositoryDelete() {
        ticketService.delete(1L);
        verify(ticketRepository, times(1)).deleteById(1L);
    }
}
