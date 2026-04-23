package com.example.SupportTicketSystem.service;

import com.example.SupportTicketSystem.entity.Status;
import com.example.SupportTicketSystem.entity.Ticket;
import com.example.SupportTicketSystem.entity.User;
import com.example.SupportTicketSystem.repository.TicketRepository;
import com.example.SupportTicketSystem.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;

    public TicketService(TicketRepository ticketRepository, UserRepository userRepository) {
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
    }

    public Ticket create(Ticket ticket) {
        String username = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        ticket.setUser(user);
        ticket.setStatus(Status.OPEN);
        ticket.setCreatedAt(LocalDateTime.now());
        return ticketRepository.save(ticket);
    }

    public List<Ticket> getAll() {
        return ticketRepository.findAll();
    }

    public List<Ticket> getMyTickets() {
        String username = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return ticketRepository.findByUser(user);
    }

    public List<Ticket> search(String keyword) {
        return ticketRepository.findByTitleContainingIgnoreCase(keyword);
    }

    public Ticket updateStatus(Long id, Status status) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));
        ticket.setStatus(status);
        return ticketRepository.save(ticket);
    }

    public void delete(Long id) {
        ticketRepository.deleteById(id);
    }
}