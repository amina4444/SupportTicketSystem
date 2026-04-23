package com.example.SupportTicketSystem.controller;

import com.example.SupportTicketSystem.entity.Response;
import com.example.SupportTicketSystem.service.ResponseService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/responses")
public class ResponseController {

    private final ResponseService responseService;

    public ResponseController(ResponseService responseService) {
        this.responseService = responseService;
    }


    @PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
    @PostMapping("/{ticketId}")
    public ResponseEntity<Response> create(@PathVariable Long ticketId,
                                           @RequestBody String message) {
        return ResponseEntity.ok(responseService.createResponse(ticketId, message));
    }


    @GetMapping("/{ticketId}")
    public ResponseEntity<List<Response>> getByTicket(@PathVariable Long ticketId) {
        return ResponseEntity.ok(responseService.getByTicket(ticketId));
    }
}