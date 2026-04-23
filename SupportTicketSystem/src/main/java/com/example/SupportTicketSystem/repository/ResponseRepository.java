package com.example.SupportTicketSystem.repository;

import com.example.SupportTicketSystem.entity.Response;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResponseRepository extends JpaRepository<Response, Long> {

    List<Response> findByTicketId(Long ticketId);
}