package com.example.SupportTicketSystem.repository;

import com.example.SupportTicketSystem.entity.Ticket;
import com.example.SupportTicketSystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByTitleContainingIgnoreCase(String keyword);
    List<Ticket> findByUser(User user);
}
