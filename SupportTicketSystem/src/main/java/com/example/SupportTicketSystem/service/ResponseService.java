package com.example.SupportTicketSystem.service;

import com.example.SupportTicketSystem.entity.Response;
import com.example.SupportTicketSystem.entity.Ticket;
import com.example.SupportTicketSystem.entity.User;
import com.example.SupportTicketSystem.repository.ResponseRepository;
import com.example.SupportTicketSystem.repository.TicketRepository;
import com.example.SupportTicketSystem.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ResponseService {

    private final ResponseRepository responseRepository;
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;

    public ResponseService(ResponseRepository responseRepository,
                           TicketRepository ticketRepository,
                           UserRepository userRepository) {
        this.responseRepository = responseRepository;
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
    }

    public Response createResponse(Long ticketId, String message) {
        String username = SecurityContextHolder.getContext()
                .getAuthentication().getName();

        User manager = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        Response response = new Response();
        response.setMessage(message);
        response.setCreatedAt(LocalDateTime.now());
        response.setTicket(ticket);
        response.setManager(manager);

        return responseRepository.save(response);
    }

    public List<Response> getByTicket(Long ticketId) {
        return responseRepository.findByTicketId(ticketId);
    }
}