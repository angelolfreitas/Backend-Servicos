package com.elos.dashboard_service.dashboard;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "ticket_status")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TicketStatus {
    @Id
    private Long idTicket;
    private String status;
}