package com.example.SupportTicketSystem.controller;

import com.example.SupportTicketSystem.dto.UpdateStatusRequest;
import com.example.SupportTicketSystem.entity.Ticket;
import com.example.SupportTicketSystem.service.TicketService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PreAuthorize("hasAnyRole('USER','MANAGER','ADMIN')")
    @PostMapping
    public ResponseEntity<Ticket> create(@RequestBody Ticket ticket) {
        return ResponseEntity.ok(ticketService.create(ticket));
    }

    @GetMapping
    public ResponseEntity<List<Ticket>> getAll() {
        return ResponseEntity.ok(ticketService.getAll());
    }

    @GetMapping("/my")
    public ResponseEntity<List<Ticket>> getMyTickets() {
        return ResponseEntity.ok(ticketService.getMyTickets());
    }

    @GetMapping("/search")
    public ResponseEntity<List<Ticket>> search(@RequestParam String keyword) {
        return ResponseEntity.ok(ticketService.search(keyword));
    }

    @PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
    @PutMapping("/{id}/status")
    public ResponseEntity<Ticket> updateStatus(@PathVariable Long id,
                                               @RequestBody UpdateStatusRequest request) {
        return ResponseEntity.ok(ticketService.updateStatus(id, request.status));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        ticketService.delete(id);
        return ResponseEntity.ok().build();
    }
}